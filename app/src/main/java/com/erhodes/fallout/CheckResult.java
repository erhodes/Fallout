package com.erhodes.fallout;

import android.util.SparseIntArray;

import java.util.ArrayList;

/**
 * This class represents the result of passing (or failing) a skill check as part of an Action.
 */
public abstract class CheckResult {
    SparseIntArray mAffectedTargetGroups;

    CheckResult() {
        mAffectedTargetGroups = new SparseIntArray();
    }

    // pass in the targets of the skill in question
    public abstract void applyResult(Character performer, ArrayList<TargetGroup> mTargetGroups);

    // in theory, I could just say that the highest target group is always last in the array, but this is safer
    public int requiredTargets() {
        int highestTarget = 0;
        for (int i = 0; i < mAffectedTargetGroups.size(); i++) {
            if (mAffectedTargetGroups.get(i) > highestTarget)
                highestTarget = mAffectedTargetGroups.get(i);
        }
        return highestTarget;
    }
}
