package com.radonlab.tungsten.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Upsert;

import java.util.List;

@Dao
public interface ScriptDAO {
    @Query("SELECT * FROM `script`")
    List<ScriptDO> getAll();

    @Query("SELECT * FROM `script` WHERE `id` = :scriptId")
    ScriptDO findById(int scriptId);

    @Insert
    void insert(ScriptDO script);

    @Upsert
    void upsert(ScriptDO script);

    @Delete
    void delete(ScriptDO script);
}