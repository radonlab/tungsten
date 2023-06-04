package com.radonlab.tungsten.dao;

import androidx.room.Database;

@Database()
public abstract class AppDatabase {
    public abstract ScriptDAO scriptDAO();
}