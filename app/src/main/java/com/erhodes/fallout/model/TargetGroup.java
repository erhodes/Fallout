package com.erhodes.fallout.model;

import java.util.ArrayList;

/**
 * The idea with a target group is that it is a group of objects that will be effected in some way by the results of a skill check.
 * Usually a target group is going to be dynamic, with the actual group of GameObjects provided in an ArrayList at the time of the check.
 * Sometimes a target group has a predefined group of GameObjects it will affect. For example, a skill that affects the user, or a weapon reload action.
 */
public class TargetGroup {
    public static int TARGET_PERFORMER = -1;
    public static int TARGET_PRIMARY = 1;
    public static int TARGET_SECONDARY = 2;
    public static int TARGET_GRANTING_ITEM = 3;

    private String mName;
    int mMinTargets, mMaxTargets;
    public ArrayList<GameObject> mTargets;
    private boolean mStaticTargets = false;

    public TargetGroup() {
        this("Default Group", -1, -1);
    }
    public TargetGroup(String name, int min, int max) {
        mName = name;
        mMinTargets = min;
        mMaxTargets = max;
        mTargets = new ArrayList<>();
    }

    public TargetGroup(GameObject target) {
        mTargets = new ArrayList<>();
        mTargets.add(target);
        setupStatic();
    }
    // this is for when the targets will always be the same.
    public TargetGroup(ArrayList<GameObject> targets) {
        mTargets = targets;
        setupStatic();
    }

    private void setupStatic() {
        mStaticTargets = true;
        mMinTargets = mTargets.size();
        mMaxTargets = mTargets.size();
    }

    public String getName() { return mName; }
    public int getMinTargets() { return mMinTargets; }
    public int getMaxTargets() { return mMaxTargets; }

    public int getCurrentTargetCount() {
        return mTargets.size();
    }

    public boolean requiresTarget() {
        return mTargets.size() < mMinTargets;
        //return !mStaticTargets;
    }

    public boolean isDynamic() {
        return !mStaticTargets;
    }

    public void addTarget(GameObject newTarget) {
        if (mMaxTargets != -1 && mTargets.size() < mMaxTargets) {
            mTargets.add(newTarget);
        }
    }

    public boolean removeTarget(GameObject target) {
        return mTargets.remove(target);
    }
    public boolean contains(GameObject target) {
        return mTargets.contains(target);
    }
    public void resetTargets() {
        if (!mStaticTargets)
            mTargets = new ArrayList<>();
    }
}
