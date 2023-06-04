package com.radonlab.tungsten.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.radonlab.tungsten.constant.AppConstant;

@Database(version = 1, entities = ScriptDO.class)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance = null;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, AppConstant.DATABASE_NAME).build();
        }
        return instance;
    }

    public abstract ScriptDAO scriptDAO();
}