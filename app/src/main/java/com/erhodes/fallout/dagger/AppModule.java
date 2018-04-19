package com.erhodes.fallout.dagger;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.erhodes.fallout.CharacterRepository;
import com.erhodes.fallout.database.MyDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by e_rho on 12/16/2017.
 */

@Module
public class AppModule {
    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    public Context provideContext() {
        return context;
    }

    @Provides
    public SharedPreferences provieSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Singleton
    @Provides
    static MyDatabase provideMyDatabase(Context context) {
        return Room.databaseBuilder(context, MyDatabase.class, "database-name").build();
    }
}
