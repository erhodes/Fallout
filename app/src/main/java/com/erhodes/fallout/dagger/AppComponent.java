package com.erhodes.fallout.dagger;

import com.erhodes.fallout.CharacterRepository;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by e_rho on 12/16/2017.
 */
@Component(modules = {
        AppModule.class
})
@Singleton
public interface AppComponent {
    CharacterRepository getCharacterRepo();
}
