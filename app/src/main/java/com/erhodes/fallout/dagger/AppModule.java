package com.erhodes.fallout.dagger;

import com.erhodes.fallout.CharacterRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by e_rho on 12/16/2017.
 */

@Module
public class AppModule {

    @Singleton
    @Provides
    static CharacterRepository provideCharacterService() {
        return new CharacterRepository();
    };
}
