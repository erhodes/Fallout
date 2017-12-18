package com.erhodes.fallout.view.encounter;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.erhodes.fallout.CharacterRepository;
import com.erhodes.fallout.MyApplication;
import com.erhodes.fallout.model.Character;

import javax.inject.Inject;

/**
 * Created by e_rho on 12/18/2017.
 */

public class EncounterViewModel extends ViewModel {
    private CharacterRepository mCharacterRepository;
    private LiveData<Character> mActiveCharacter;

    @Inject
    public EncounterViewModel() {
        mCharacterRepository = MyApplication.getComponent().getCharacterRepo();
        mActiveCharacter = mCharacterRepository.getActiveCharacter();
    }

    public CharacterRepository getRepository() {
        return mCharacterRepository;
    }
}
