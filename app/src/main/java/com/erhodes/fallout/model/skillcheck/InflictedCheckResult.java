package com.erhodes.fallout.model.skillcheck;

import android.util.SparseArray;

import com.erhodes.fallout.model.*;
import com.erhodes.fallout.model.Character;

import java.lang.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Forces the target to make a skill check.
 */
public class InflictedCheckResult extends CheckResult {
    SkillCheck mNextCheck;

    public InflictedCheckResult(SkillCheck nextCheck) {
        mNextCheck = nextCheck;
    }

    @Override
    public void applyResult(Character performer, HashMap<Integer, TargetGroup> targetGroups) {
        for (int i = 0; i < mAffectedTargetGroups.size(); i++) {
            for (GameObject gameObject : targetGroups.get(mAffectedTargetGroups.get(i)).mTargets) {
                if (gameObject instanceof com.erhodes.fallout.model.Character) {
                    mNextCheck.makeCheck((com.erhodes.fallout.model.Character)gameObject, targetGroups);
                }
            }
        }
    }


}
