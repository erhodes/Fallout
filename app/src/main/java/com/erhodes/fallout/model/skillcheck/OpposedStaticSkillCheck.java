package com.erhodes.fallout.model.skillcheck;

import com.erhodes.fallout.model.*;
import com.erhodes.fallout.model.Character;

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
        mTargetGroups.put(TargetGroup.TARGET_PRIMARY, targets);
    }

    @Override
    public int roll(Character performer) {
        int skillValue = performer.getAttributeValue(mSkillKey);
        int roll;
        for (GameObject target : mTargetGroups.get(TargetGroup.TARGET_PRIMARY).mTargets) {
            roll = rollDice(skillValue);
            GameLog.getInstance().addRollEvent(performer.name, roll, mSkillKey, skillValue, target.getAttributeValue(mOpposedSkillKey));
            if (roll >= target.getAttributeValue(mOpposedSkillKey)) {
                resolvePass(performer);
            } else {
                resolveFail(performer);
            }
        }
        return Action.RESULT_PASSED;
    }
}
