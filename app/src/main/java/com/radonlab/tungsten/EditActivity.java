package com.radonlab.tungsten;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.amrdeveloper.codeview.CodeView;
import com.google.android.material.snackbar.Snackbar;
import com.radonlab.tungsten.constant.AppConstant;
import com.radonlab.tungsten.dao.ScriptRepo;
import com.radonlab.tungsten.dto.ScriptDTO;

public class EditActivity extends AppCompatActivity {

    private View contentView;

    private CodeView codeView;

    private ScriptDTO script;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        contentView = findViewById(R.id.content_view);
        codeView = findViewById(R.id.code_view);
        loadScriptData(getIntent().getIntExtra(AppConstant.SCRIPT_ID, -1));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_menu) {
            saveScriptData();
            return true;
        }
        if (item.getItemId() == R.id.delete_menu) {
            confirmDeleteScriptData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("CheckResult")
    private void loadScriptData(int scriptId) {
        ScriptRepo.getInstance(this)
                .findById(scriptId)
                .subscribe(result -> {
                    script = result.map(ScriptDTO::fromDO)
                            .orElseGet(() -> new ScriptDTO("undefined", ""));
                    Log.d("EditActivity", "loaded script: " + script.getId() + "#" + script.getName());
                    setTitle(script.getName());
                    codeView.setText(script.getContent());
                }, e -> {
                    Log.e("EditActivity", "fatal", e);
                });
    }

    @SuppressLint("CheckResult")
    private void saveScriptData() {
        ScriptRepo.getInstance(this)
                .upsert(script.toDO())
                .subscribe(() -> {
                    Snackbar.make(contentView, R.string.saved, Snackbar.LENGTH_LONG).show();
                }, e -> {
                    Log.e("EditActivity", "fatal", e);
                    String reason = getString(R.string.failed);
                    if (e instanceof SQLiteConstraintException) {
                        reason = getString(R.string.failed_check);
                    }
                    Snackbar.make(contentView, reason, Snackbar.LENGTH_LONG).show();
                });
    }

    private void confirmDeleteScriptData() {
        String title = getString(R.string.confirm_delete) + " " + script.getName();
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setIcon(R.drawable.ic_notice_inverse)
                .setPositiveButton(R.string.ok, (DialogInterface dialog, int which) -> {
                    if (script.getId() != null) {
                        deleteScriptData();
                    } else {
                        Snackbar.make(contentView, R.string.unsaved, Snackbar.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @SuppressLint("CheckResult")
    private void deleteScriptData() {
        ScriptRepo.getInstance(this)
                .delete(script.toDO())
                .subscribe(() -> {
                    Snackbar.make(contentView, R.string.deleted, Snackbar.LENGTH_LONG).show();
                }, e -> {
                    Log.e("EditActivity", "fatal", e);
                    Snackbar.make(contentView, R.string.failed, Snackbar.LENGTH_LONG).show();
                });
    }
}