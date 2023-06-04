package com.radonlab.tungsten;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.amrdeveloper.codeview.CodeView;
import com.radonlab.tungsten.constant.AppConstant;
import com.radonlab.tungsten.dao.AppDatabase;
import com.radonlab.tungsten.dao.ScriptDO;
import com.radonlab.tungsten.dto.ScriptDTO;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class EditActivity extends AppCompatActivity {

    private CodeView codeView;

    private AppDatabase db;

    private Disposable ds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
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
                    ScriptDTO script = result != ScriptDO.NO_RESULT ? ScriptDTO.fromDO(result) : new ScriptDTO("undefined", "");
                    Log.d("EditActivity", "loaded script: " + script.getId() + "#" + script.getName());
                    codeView.setText(script.getContent());
                });
    }
}