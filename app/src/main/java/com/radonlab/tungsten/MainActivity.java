package com.radonlab.tungsten;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.radonlab.tungsten.constant.AppConstant;
import com.radonlab.tungsten.dao.ScriptRepo;
import com.radonlab.tungsten.dto.ScriptDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.disposables.Disposable;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;

    private RecyclerView listView;

    private FloatingActionButton fab;

    private Disposable ds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list_view);
        fab = findViewById(R.id.fab);
        ds = initScriptList();
        initEventListener();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (Context.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {
//            Activity.requestPermissions(this, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, PERMISSION_REQUEST_CODE);
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!ds.isDisposed()) {
            ds.dispose();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.help_menu) {
            showHelpInfo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showHelpInfo() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.about, getString(R.string.app_name)))
                .setView(R.layout.dialog_help)
                .setPositiveButton(R.string.ok, null)
                .show();
        TextView helpText = dialog.findViewById(R.id.help_text);
        String helpString = getString(R.string.app_intro, getString(R.string.app_name));
        helpText.setText(Html.fromHtml(helpString, Html.FROM_HTML_MODE_COMPACT));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            initScreenService();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private Disposable initScriptList() {
        List<ScriptDTO> dataSource = new ArrayList<>();
        RecyclerView.Adapter<ViewHolder> adapter = new RecyclerView.Adapter<ViewHolder>() {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View listItem = getLayoutInflater().inflate(R.layout.list_item, parent, false);
                return new ViewHolder(listItem);
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                ScriptDTO dataItem = dataSource.get(position);
                holder.scriptName.setText(dataItem.getName());
                CharSequence timeLabel = "";
                if (dataItem.getTimestamp() != null) {
                    timeLabel = DateFormat.format("yy/MM/dd HH:mm", dataItem.getTimestamp());
                }
                holder.modifiedTime.setText(timeLabel);
                holder.editButton.setOnClickListener((View view) -> {
                    openScriptViewer(dataItem, view);
                });
            }

            @Override
            public int getItemCount() {
                Log.d("MainActivity", "item count: " + dataSource.size());
                return dataSource.size();
            }
        };
        listView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(layoutManager);
        // Load data source
        return ScriptRepo.getInstance(this)
                .getAll()
                .subscribe((result) -> {
                    List<ScriptDTO> newDataSource = result.stream().map(ScriptDTO::fromDO).collect(Collectors.toList());
                    dataSource.clear();
                    dataSource.addAll(newDataSource);
                    adapter.notifyDataSetChanged();
                }, e -> {
                    Log.e("MainActivity", "fatal", e);
                });
    }

    private void initEventListener() {
        fab.setOnClickListener((View view) -> {
            openScriptViewer(null, view);
        });
    }

    private void openScriptViewer(@Nullable ScriptDTO dataItem, @Nullable View triggerView) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(AppConstant.SCRIPT_ID, dataItem != null ? dataItem.getId() : null);
        Bundle bundle = null;
        if (triggerView != null) {
            int width = triggerView.getWidth();
            int height = triggerView.getHeight();
            int centerX = width / 2;
            int centerY = height / 2;
            ActivityOptions options = ActivityOptions.makeScaleUpAnimation(triggerView, centerX, centerY, width, height);
            bundle = options.toBundle();
        }
        startActivity(intent, bundle);
    }

    private void initScreenService() {
        Intent intent = new Intent(this, ScreenService.class);
        startService(intent);
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView scriptName;

        public TextView modifiedTime;

        public Button editButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            scriptName = itemView.findViewById(R.id.script_name);
            modifiedTime = itemView.findViewById(R.id.modified_time);
            editButton = itemView.findViewById(R.id.edit_button);
        }
    }
}