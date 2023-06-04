package com.radonlab.tungsten;

import android.annotation.SuppressLint;
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
import com.radonlab.tungsten.dao.AppDatabase;
import com.radonlab.tungsten.dao.ScriptDO;
import com.radonlab.tungsten.dto.ScriptDTO;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class EditActivity extends AppCompatActivity {

    private View contentView;

    private CodeView codeView;

    private ScriptDTO script;

    private AppDatabase db;

    private Disposable ds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        contentView = findViewById(R.id.content_view);
        codeView = findViewById(R.id.code_view);
        db = AppDatabase.getInstance(this);
        ds = loadScriptData(getIntent().getIntExtra(AppConstant.SCRIPT_ID, -1));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!ds.isDisposed()) {
            ds.dispose();
        }
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
        return super.onOptionsItemSelected(item);
    }

    private Disposable loadScriptData(int scriptId) {
        return Observable.fromCallable(() -> {
                    if (scriptId != -1) {
                        ScriptDO record = db.scriptDAO().findById(scriptId);
                        if (record != null) {
                            return record;
                        }
                    }
                    return ScriptDO.NO_RESULT;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    script = result != ScriptDO.NO_RESULT ? ScriptDTO.fromDO(result) : new ScriptDTO("undefined", "");
                    Log.d("EditActivity", "loaded script: " + script.getId() + "#" + script.getName());
                    setTitle(script.getName());
                    codeView.setText(script.getContent());
                }, e -> {
                    Log.e("EditActivity", "fatal", e);
                });
    }

    @SuppressLint("CheckResult")
    private void saveScriptData() {
        Observable.fromCallable(() -> {
                    db.scriptDAO().upsert(script.toDO());
                    return true;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
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
}