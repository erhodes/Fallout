package com.erhodes.fallout.view.skills;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.erhodes.fallout.CharacterRepository;
import com.erhodes.fallout.MyApplication;
import com.erhodes.fallout.model.Character;

import javax.inject.Inject;

/**
 * Created by e_rho on 12/18/2017.
 */

public class SkillsViewModel extends ViewModel {
    private CharacterRepository mCharacterRepository;

    @Inject
    public SkillsViewModel() {
        mCharacterRepository = MyApplication.getComponent().getCharacterRepo();
    }

    public LiveData<Character> getCharacter() {
        return mCharacterRepository.getActiveCharacter();
    }

    public void addRank(Character character, String skill) {
        mCharacterRepository.addSkillRankToCharacter(character, skill);
    }
}
