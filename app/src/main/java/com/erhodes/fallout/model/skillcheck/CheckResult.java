package com.erhodes.fallout.model.skillcheck;

import android.util.SparseIntArray;

import com.erhodes.fallout.model.*;

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
    public abstract void applyResult(com.erhodes.fallout.model.Character performer, ArrayList<TargetGroup> mTargetGroups);

    public void addAffectedTargetGroup(int groupPosition) {
        mAffectedTargetGroups.put(mAffectedTargetGroups.size(), groupPosition);
    }

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
