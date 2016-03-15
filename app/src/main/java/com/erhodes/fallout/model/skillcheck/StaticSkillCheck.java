package com.erhodes.fallout.model.skillcheck;

import android.util.Log;

import java.lang.*;

/**
 * Created by Eric on 25/01/2016.
 */
public class StaticSkillCheck extends SkillCheck {
    private int mDifficulty;

    public StaticSkillCheck(String skill, int difficulty) {
        super(skill);
        mDifficulty = difficulty;
    }

    @Override
    public int roll(com.erhodes.fallout.model.Character performer) {
        int skillValue = performer.getAttribute(mSkillKey);
        int roll = rollDice(skillValue);
        Log.d("Eric", performer.name + " rolled a " + roll + "(" + (roll - skillValue) + "+" + skillValue + ") against difficulty " + mDifficulty);
        if (roll >= mDifficulty) {
            return resolvePass(performer);
        } else {
            return resolveFail(performer);
        }
    }
}
