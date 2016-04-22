package com.erhodes.fallout.model.skillcheck;

import com.erhodes.fallout.model.*;
import com.erhodes.fallout.model.Character;

import java.lang.*;

/**
 * Created by Eric on 24/01/2016.
 */
public class OpposedSkillCheck extends SkillCheck {
    private String mOpposedSkillKey;
    OpposedSkillCheck(String skill, String opposedSkill, TargetGroup targets) {
        super(skill);
        mOpposedSkillKey = opposedSkill;
        mTargetGroups.put(TargetGroup.TARGET_PRIMARY, targets);
    }

    @Override
    public int roll(Character performer) {
        int skillValue = performer.getAttributeValue(mSkillKey);
        for (GameObject target : mTargetGroups.get(0).mTargets) {
            if (rollOff(performer.getName(), skillValue, target.getName(), target.getAttributeValue(mOpposedSkillKey))) {
                resolvePass(performer);
            } else {
                resolveFail(performer);
            }
        }
        return Action.RESULT_PASSED;
    }

    private boolean rollOff(String performerName, int performerSkillValue, String targetName, int targetSkillValue) {
        int a = rollDice(performerSkillValue);
        int b = rollDice(targetSkillValue);
        if (a == b) {
            return rollOff(performerName, performerSkillValue, targetName, targetSkillValue);
        } else {
            GameLog.getInstance().addOpposedRollEvent(performerName, a, targetName, b, mOpposedSkillKey);
            return a > b;
        }
    }
}
