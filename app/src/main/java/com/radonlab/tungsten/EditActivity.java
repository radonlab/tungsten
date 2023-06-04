package com.radonlab.tungsten;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.amrdeveloper.codeview.CodeView;
import com.radonlab.tungsten.constant.AppConstant;
import com.radonlab.tungsten.dao.AppDatabase;
import com.radonlab.tungsten.dto.ScriptDTO;

public class EditActivity extends AppCompatActivity {

    private CodeView codeView;

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        codeView = findViewById(R.id.code_view);
        db = AppDatabase.getInstance(this);
        loadScriptData(savedInstanceState.getInt(AppConstant.SCRIPT_ID));
    }

    private void loadScriptData(Integer scriptId) {
        ScriptDTO script;
        if (scriptId != null) {
            script = ScriptDTO.fromDO(db.scriptDAO().findById(scriptId));
        } else {
            script = new ScriptDTO("undefined", "");
        }
        Log.d("EditActivity", "loaded script: " + script.getId() + "#" + script.getName());
        codeView.setText(script.getContent());
    }
}