package com.radonlab.tungsten.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Upsert;

import java.util.List;
import java.util.Optional;

@Dao
public interface ScriptDAO {
    @Query("SELECT * FROM userscript")
    List<ScriptDO> getAll();

    @Query("SELECT * FROM userscript WHERE id = :scriptId")
    Optional<ScriptDO> findById(int scriptId);

    @Insert
    void insert(ScriptDO script);

    @Upsert
    void upsert(ScriptDO script);

    @Delete
    void delete(ScriptDO script);
}