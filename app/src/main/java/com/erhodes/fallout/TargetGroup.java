package com.erhodes.fallout;

import java.util.ArrayList;

/**
 * The idea with a target group is that it is a group of objects that will be effected in some way by the results of a skill check.
 * Usually a target group is going to be dynamic, with the actual group of GameObjects provided in an ArrayList at the time of the check.
 * Sometimes a target group has a predefined group of GameObjects it will affect. For example, a skill that affects the user, or a weapon reload action.
 */
public class TargetGroup {
    ArrayList<GameObject> mTargets;
    private boolean mStaticTargets = false;

    TargetGroup() {
        mTargets = new ArrayList<>();
    }

    // this is for when the targets will always be the same.
    TargetGroup(ArrayList<GameObject> targets) {
        mTargets = targets;
        mStaticTargets = true;
    }

    //A TargetGroup needs a target if it's targets size is 0
    public boolean requiresTarget() {
        return mTargets.size() == 0;
    }

    public void resetTargets() {
        if (!mStaticTargets)
            mTargets = new ArrayList<>();
    }
}
