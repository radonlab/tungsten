package com.radonlab.tungsten;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.radonlab.tungsten.constant.AppConstant;
import com.radonlab.tungsten.dao.AppDatabase;
import com.radonlab.tungsten.dto.ScriptDTO;

import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;

    private RecyclerView listView;

    private FloatingActionButton fab;

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list_view);
        fab = findViewById(R.id.fab);
        db = AppDatabase.getInstance(this);
        initScriptList();
        initEventListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            initScreenService();
        }
    }

    private void initScriptList() {
        List<ScriptDTO> dataSource = db.scriptDAO().getAll().stream().map(ScriptDTO::fromDO).collect(Collectors.toList());
        listView.setAdapter(new RecyclerView.Adapter<ViewHolder>() {
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
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(layoutManager);
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