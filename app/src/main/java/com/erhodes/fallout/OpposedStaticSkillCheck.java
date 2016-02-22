package com.erhodes.fallout;

import android.util.Log;

/**
 * A SkillCheck made against a target's attribute.
 */
public class OpposedStaticSkillCheck extends SkillCheck {
    private String mOpposedSkillKey;

    OpposedStaticSkillCheck(String skill, String opposedSkill) {
        this(skill, opposedSkill, new TargetGroup());
    }

    /**
     * At least one TargetGroup is needed to oppose
     * @param skill
     * @param opposedSkill
     * @param targets
     */
    OpposedStaticSkillCheck(String skill, String opposedSkill, TargetGroup targets) {
        super(skill);
        mOpposedSkillKey = opposedSkill;
        mTargetGroups.add(targets);
    }

    @Override
    public int roll(Character performer) {
        int skillValue = performer.getAttribute(mSkillKey);
        int roll;
        for (GameObject target : mTargetGroups.get(0).mTargets) {
            roll = rollDice(skillValue);
            if (roll >= target.getAttribute(mOpposedSkillKey)) {
                resolvePass(performer);
            } else {
                resolveFail(performer);
            }
        }
        return Action.RESULT_PASSED;
    }
}
