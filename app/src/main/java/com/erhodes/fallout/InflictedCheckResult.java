package com.erhodes.fallout;

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
    public void applyResult(Character performer, ArrayList<TargetGroup> mTargetGroups) {
        for (int i = 0; i < mAffectedTargetGroups.size(); i++) {
            for (GameObject gameObject : mTargetGroups.get(i).mTargets) {
                if (gameObject instanceof  Character) {
                    mNextCheck.makeCheck((Character)gameObject, mTargetGroups);
                }
            }
        }
    }


}
