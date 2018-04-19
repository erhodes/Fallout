package com.erhodes.fallout;

import android.app.Application;

import com.erhodes.fallout.dagger.AppComponent;
import com.erhodes.fallout.dagger.AppModule;
import com.erhodes.fallout.dagger.DaggerAppComponent;

/**
 * Created by e_rho on 12/18/2017.
 */

public class MyApplication extends Application {
    private static AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }

    public static AppComponent getComponent() {
        return mAppComponent;
    }
}
