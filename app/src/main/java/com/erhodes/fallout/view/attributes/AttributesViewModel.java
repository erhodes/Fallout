package com.erhodes.fallout.view.attributes;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.erhodes.fallout.CharacterRepository;
import com.erhodes.fallout.MyApplication;
import com.erhodes.fallout.model.Character;

import javax.inject.Inject;

/**
 * Created by e_rho on 12/17/2017.
 */

public class AttributesViewModel extends ViewModel {
    private CharacterRepository mCharacterRepository;

    public AttributesViewModel() {
        mCharacterRepository = MyApplication.getComponent().getCharacterRepo();
    }

    public LiveData<Character> getCharacter() {
        return mCharacterRepository.getActiveCharacter();
    }
}
