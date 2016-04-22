package com.erhodes.fallout.model.skillcheck;

import android.util.Log;
import com.erhodes.fallout.model.Character;
import com.erhodes.fallout.model.GameLog;

import java.lang.*;

/**
 * A skill check against a predefined, static target.
 */
public class StaticSkillCheck extends SkillCheck {
    private int mDifficulty;

    public StaticSkillCheck(String skill, int difficulty) {
        super(skill);
        mDifficulty = difficulty;
    }

    @Override
    public int roll(Character performer) {
        int skillValue = performer.getAttributeValue(mSkillKey);
        int roll = rollDice(skillValue);
        GameLog.getInstance().addRollEvent(performer.name, roll, mSkillKey, skillValue, mDifficulty);
        if (roll >= mDifficulty) {
            return resolvePass(performer);
        } else {
            return resolveFail(performer);
        }
    }
}
