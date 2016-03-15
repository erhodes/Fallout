package com.erhodes.fallout.model.skillcheck;

import com.erhodes.fallout.model.*;

import java.lang.*;

/**
 * A SkillCheck made against a target's attribute.
 */
public class OpposedStaticSkillCheck extends SkillCheck {
    private String mOpposedSkillKey;

    public OpposedStaticSkillCheck(String skill, String opposedSkill) {
        this(skill, opposedSkill, new TargetGroup());
    }

    /**
     * At least one TargetGroup is needed to oppose
     * @param skill
     * @param opposedSkill
     * @param targets
     */
    public OpposedStaticSkillCheck(String skill, String opposedSkill, TargetGroup targets) {
        super(skill);
        mOpposedSkillKey = opposedSkill;
        mTargetGroups.add(targets);
    }

    @Override
    public int roll(com.erhodes.fallout.model.Character performer) {
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
