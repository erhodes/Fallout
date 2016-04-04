package com.erhodes.fallout.presenter;

import com.erhodes.fallout.model.Perk;
import com.erhodes.fallout.model.Character;

/**
 * Created by Eric on 03/04/2016.
 */
public class PerkPresenter implements PerkContract.UserActionListener {
    Character mCharacter;
    PerkContract.View mView;

    public PerkPresenter(Character character, PerkContract.View view) {
        mCharacter = character;
        mView = view;
    }

    @Override
    public void acquirePerk(Perk perk) {
        mCharacter.acquirePerk(perk);
        mView.update();
    }
}
