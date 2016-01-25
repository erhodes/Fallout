package com.erhodes.fallout;

import android.util.Log;

/**
 * A SkillCheck made against a target's attribute.
 */
public class OpposedStaticSkillCheck extends SkillCheck {
    private String mOpposedSkillKey;

    OpposedStaticSkillCheck(String skill, String opposedSkill) {
        super(skill);
        mOpposedSkillKey = opposedSkill;
    }

    @Override
    public int makeCheck(Character performer, Character target) {
        int skillValue = performer.getAttribute(mSkillKey);
        int roll = roll(skillValue);
        if (roll >= target.getAttribute(mOpposedSkillKey)) {
            return resolvePass(performer, target);
        } else {
            return resolveFail(performer, target);
        }
    }
}
