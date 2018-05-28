package com.erhodes.fallout.view.perk;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.erhodes.fallout.MyApplication;
import com.erhodes.fallout.model.Character;
import com.erhodes.fallout.CharacterRepository;
import com.erhodes.fallout.model.Perk;

import javax.inject.Inject;

/**
 * Created by e_rho on 12/16/2017.
 */

public class PerkViewModel extends ViewModel {
    private CharacterRepository mCharacterRepository;

    @Inject
    public PerkViewModel() {
        mCharacterRepository = MyApplication.getComponent().getCharacterRepo();
    }

    public LiveData<Character> getCharacter() {
        return mCharacterRepository.getActiveCharacter();
    }
    public void acquirePerk(Character character, Perk perk) {
        mCharacterRepository.addPerkToCharacter(character, perk);
    }
}
