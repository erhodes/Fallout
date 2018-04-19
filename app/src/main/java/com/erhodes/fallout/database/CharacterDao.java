package com.erhodes.fallout.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;


import com.erhodes.fallout.model.Character;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by e_rho on 1/12/2018.
 */
@Dao
public interface CharacterDao {
    @Insert(onConflict = REPLACE)
    long save(Character character);

    @Query("SELECT * FROM character WHERE name = :name")
    LiveData<Character> load(String name);
    @Query("SELECT * FROM character WHERE id = :id")
    LiveData<Character> load(long id);

    @Query("SELECT * FROM character")
    LiveData<List<Character>> loadAll();

    @Delete
    void delete(Character character);
}
