package com.erhodes.fallout.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.erhodes.fallout.model.Character;

/**
 * Created by e_rho on 1/12/2018.
 */
@Database(entities = {Character.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class MyDatabase extends RoomDatabase {
    public abstract CharacterDao characterDao();
}
