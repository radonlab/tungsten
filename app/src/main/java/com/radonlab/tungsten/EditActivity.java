package com.radonlab.tungsten;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;

import com.amrdeveloper.codeview.CodeView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.radonlab.tungsten.base.BaseActivity;
import com.radonlab.tungsten.constant.AppConstant;
import com.radonlab.tungsten.dao.ScriptRepo;
import com.radonlab.tungsten.dto.ScriptDTO;
import com.radonlab.tungsten.util.RequestUtil;

import java.util.Optional;

import io.reactivex.rxjava3.disposables.Disposable;

public class EditActivity extends BaseActivity {

    private View contentView;

    private CodeView codeView;

    private TextInputEditText editText;

    private int scriptId;

    private ScriptDTO script;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        contentView = findViewById(R.id.content_view);
        codeView = findViewById(R.id.code_view);
        scriptId = getIntent().getIntExtra(AppConstant.SCRIPT_ID, -1);
        codeView.setEnableLineNumber(true);
        initScriptData(scriptId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        MenuItem importMenuItem = menu.findItem(R.id.import_menu);
        importMenuItem.setVisible(scriptId == -1);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.import_menu) {
            importScriptData();
            return true;
        }
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

    private void initScriptData(int scriptId) {
        Disposable disposable = ScriptRepo.getInstance(this)
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
        addDisposable(disposable);
    }

    private void importScriptData() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.import_file)
                .setView(R.layout.dialog_edit)
                .setPositiveButton(R.string.ok, null)
                .show();
        editText = alertDialog.findViewById(R.id.edit_text);
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                .setOnClickListener(view -> {
                    String url = Optional.ofNullable(editText.getText())
                            .map(Object::toString)
                            .map(String::trim)
                            .orElse("");
                    if (url.isEmpty()) {
                        editText.setError(getString(R.string.required));
                        return;
                    } else {
                        alertDialog.dismiss();
                    }
                    Disposable disposable = RequestUtil.get(url)
                            .subscribe(content -> {
                                codeView.setText(content);
                            }, e -> {
                                String reason = e.getMessage();
                                Snackbar.make(contentView, reason, Snackbar.LENGTH_LONG).show();
                            });
                    addDisposable(disposable);
                });
    }

    private void saveScriptData() {
        Disposable disposable = ScriptRepo.getInstance(this)
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
        addDisposable(disposable);
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

    private void deleteScriptData() {
        Disposable disposable = ScriptRepo.getInstance(this)
                .delete(script.toDO())
                .subscribe(() -> {
                    Snackbar.make(contentView, R.string.deleted, Snackbar.LENGTH_LONG).show();
                }, e -> {
                    Log.e("EditActivity", "fatal", e);
                    Snackbar.make(contentView, R.string.failed, Snackbar.LENGTH_LONG).show();
                });
        addDisposable(disposable);
    }
}