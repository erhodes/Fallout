package com.erhodes.fallout.model.skillcheck;

import com.erhodes.fallout.model.*;

import java.lang.*;
import java.util.ArrayList;

/**
 * Forces the target to make a skill check.
 */
public class InflictedCheckResult extends CheckResult {
    SkillCheck mNextCheck;

    public InflictedCheckResult(SkillCheck nextCheck) {
        mNextCheck = nextCheck;
    }

    @Override
    public void applyResult(com.erhodes.fallout.model.Character performer, ArrayList<TargetGroup> mTargetGroups) {
        for (int i = 0; i < mAffectedTargetGroups.size(); i++) {
            for (GameObject gameObject : mTargetGroups.get(i).mTargets) {
                if (gameObject instanceof com.erhodes.fallout.model.Character) {
                    mNextCheck.makeCheck((com.erhodes.fallout.model.Character)gameObject, mTargetGroups);
                }
            }
        }
    }


}
