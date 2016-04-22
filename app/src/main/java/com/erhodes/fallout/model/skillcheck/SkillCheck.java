package com.erhodes.fallout.model.skillcheck;

import android.util.Log;
import android.util.SparseArray;

import com.erhodes.fallout.model.*;
import com.erhodes.fallout.model.Character;

import java.lang.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * This class isn't very well named, since you could do attribute checks as well. Doesn't sound right though.
 */
public abstract class SkillCheck {
    String mSkillKey;
    protected Random mRandom;
    HashMap<Integer, TargetGroup> mTargetGroups; //the first target group is always the character making the check?
    public ArrayList<Cost> mCosts;
    ArrayList<CheckResult> mPassResults;
    ArrayList<CheckResult> mFailResults;

    public SkillCheck(String skill) {
        mSkillKey = skill;
        mTargetGroups = new HashMap<>();
        mPassResults = new ArrayList<>();
        mFailResults = new ArrayList<>();
        mCosts = new ArrayList<>();
        mRandom = new Random();
    }

    /** get all target groups that are going to need to have targets assigned to them
     *
     * @return
     */
    public ArrayList<TargetGroup> getDynamicTargetGroups() {
        ArrayList<TargetGroup> result = new ArrayList<>();
        for (TargetGroup targetGroup : mTargetGroups.values()) {
            if (targetGroup.isDynamic())
                result.add(targetGroup);
        }
        return result;
    }

    public ArrayList<TargetGroup> getUnfilledTargetGroups() {
        ArrayList<TargetGroup> result = new ArrayList<>();
        for (TargetGroup targetGroup : mTargetGroups.values()) {
            if (targetGroup.requiresTarget())
                result.add(targetGroup);
        }
        return result;
    }

    public void addTargetGroup(int targetType, TargetGroup targetGroup) {
        mTargetGroups.put(targetType, targetGroup);
    }

    public void addPassResult(CheckResult checkResult) {
        mPassResults.add(checkResult);
    }

    public void addFailResult(CheckResult checkResult) {
        mFailResults.add(checkResult);
    }

    public int rollDice(int bonus) {
        return mRandom.nextInt(20) + 1 + bonus;
    }

    public int makeCheck(Character performer) {
        HashMap<Integer, TargetGroup> result = new HashMap<>();
        return makeCheck(performer, result);
    }
    public int makeCheck(Character performer, HashMap<Integer, TargetGroup> targetGroups) {
        Log.d("Eric","given targets: " + targetGroups.size() + "; missing targets: " + getUnfilledTargetGroups().size());
        if (targetGroups.size() < getUnfilledTargetGroups().size()) {
            return Action.RESULT_MISSING_TARGETS;
        }

        for (Integer i : mTargetGroups.keySet()) {
            if (mTargetGroups.get(i).requiresTarget()) {
                mTargetGroups.get(i).mTargets = targetGroups.get(i).mTargets;
            }
        }

        GameLog gameLog = GameLog.getInstance();
        // check the costs for this skill check
        boolean allCanPay = true;
        for (Cost cost : mCosts) {
            if (!cost.canPayCost(performer, mTargetGroups)) {
                resetTargets();
                return Action.RESULT_UNABLE_TO_PAY_COSTS;
            }
        }
        for (Cost cost : mCosts) {
            cost.payCost(performer, mTargetGroups);
        }
        int result = roll(performer);

        return result;
    }

    protected abstract int roll(com.erhodes.fallout.model.Character performer);

    protected void resetTargets() {
        for (TargetGroup targetGroup : mTargetGroups.values()) {
            targetGroup.resetTargets();
        }
    }

    protected int resolvePass(Character performer) {
        // yay, you passed!
        Log.d("Eric","pass, applying results");
        for (CheckResult checkResult : mPassResults) {
            checkResult.applyResult(performer, mTargetGroups);
        }
        resetTargets();
        return Action.RESULT_PASSED;
    }

    protected int resolveFail(Character performer) {
        // boo, failed
        Log.d("Eric","failed the check");
        for (CheckResult checkResult : mFailResults) {
            checkResult.applyResult(performer, mTargetGroups);
        }
        resetTargets();
        return Action.RESULT_FAILED_CHECK;
    }
}
