package com.radonlab.tungsten;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.radonlab.tungsten.dto.ScriptDTO;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list_view);
        initScriptList();
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
        List<ScriptDTO> dataSource = new ArrayList<>();
        listView.setAdapter(new ArrayAdapter<>(this, R.layout.list_item, dataSource));
    }

    private void initScreenService() {
        Intent intent = new Intent(this, ScreenService.class);
        startService(intent);
    }
}