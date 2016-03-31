package com.erhodes.fallout.presenter;

import com.erhodes.fallout.CharacterService;
import com.erhodes.fallout.model.Skills;

/**
 * Created by Eric on 29/03/2016.
 */
public class SkillsPresenter implements SkillsContract.UserActionsListener{
    CharacterService mCharacterService;
    SkillsContract.View mView;

    public SkillsPresenter(CharacterService characterService, SkillsContract.View view) {
        mCharacterService = characterService;
        mView = view;
    }

    @Override
    public void addRank(String skill) {
        mCharacterService.getActiveCharacter().addRank(skill);
        mView.update();
    }
}
