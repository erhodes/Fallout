package com.erhodes.fallout;

import java.util.ArrayList;

/**
 * The idea with a target group is that it is a group of objects that will be effected in some way by the results of a skill check.
 * Usually a target group is going to be dynamic, with the actual group of GameObjects provided in an ArrayList at the time of the check.
 * Sometimes a target group has a predefined group of GameObjects it will affect. For example, a skill that affects the user, or a weapon reload action.
 */
public class TargetGroup {
    private String mName;
    int mMinTargets, mMaxTargets;
    ArrayList<GameObject> mTargets;
    private boolean mStaticTargets = false;

    TargetGroup() {
        this("Default Group", -1, -1);
    }
    TargetGroup(String name, int min, int max) {
        mName = name;
        mMinTargets = min;
        mMaxTargets = max;
        mTargets = new ArrayList<>();
    }

    TargetGroup(GameObject target) {
        mTargets = new ArrayList<>();
        mTargets.add(target);
        setupStatic();
    }
    // this is for when the targets will always be the same.
    TargetGroup(ArrayList<GameObject> targets) {
        mTargets = targets;
        setupStatic();
    }

    private void setupStatic() {
        mStaticTargets = true;
        mMinTargets = mTargets.size();
        mMaxTargets = mTargets.size();
    }

    public String getName() { return mName; }
    //A TargetGroup needs a target if it is not static
    public boolean requiresTarget() {
        return mTargets.size() == 0;
        //return !mStaticTargets;
    }

    public void addTarget(GameObject newTarget) {
        if (mMaxTargets != -1 && mTargets.size() < mMaxTargets) {
            mTargets.add(newTarget);
        }
    }

    public void resetTargets() {
        if (!mStaticTargets)
            mTargets = new ArrayList<>();
    }
}
