package com.erhodes.fallout;

import android.util.Log;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class isn't very well named, since you could do attribute checks as well. Doesn't sound right though.
 */
public abstract class SkillCheck {
    String mSkillKey;
    protected Random mRandom;
    ArrayList<TargetGroup> mTargetGroups; //the first target group is always the character making the check?
    ArrayList<CheckResult> mPassResults;
    ArrayList<CheckResult> mFailResults;

    public SkillCheck(String skill) {
        mSkillKey = skill;
        mTargetGroups = new ArrayList<>();
        mPassResults = new ArrayList<>();
        mFailResults = new ArrayList<>();
        mRandom = new Random();
    }

    /**
     * How many targets does this SkillCheck require to properly resolve?
     * @return
     */
    public int requiredTargets() {
        int result = 0;
        for (TargetGroup targetGroup : mTargetGroups) {
            if (targetGroup.requiresTarget())
                result++;
        }
        return result;
    }

    public int rollDice(int bonus) {
        return mRandom.nextInt(20) + 1 + bonus;
    }

    public int makeCheck(Character performer, GameObject target) {
        TargetGroup targetGroup = new TargetGroup();
        targetGroup.mTargets.add(target);
        ArrayList<TargetGroup> result = new ArrayList<>();
        result.add(targetGroup);
        return makeCheck(performer, result);
    }
    public int makeCheck(Character performer, ArrayList<TargetGroup> targetGroups) {
        if (targetGroups.size() < requiredTargets()) {
            return Action.RESULT_MISSING_TARGETS;
        }
        int i = 0;
        int j = 0;
        for (TargetGroup targetGroup : mTargetGroups) {
            if (targetGroup.requiresTarget()) {
                targetGroup.mTargets = targetGroups.get(i).mTargets;
            }
        }
        int result = roll(performer);

        return result;
    }

    protected abstract int roll(Character performer);

    protected int resolvePass(Character performer) {
        // yay, you passed!
        Log.d("Eric","pass, applying results");
        for (CheckResult checkResult : mPassResults) {
            checkResult.applyResult(performer, mTargetGroups);
        }
        for (TargetGroup targetGroup : mTargetGroups) {
            targetGroup.resetTargets();
        }
        return Action.RESULT_PASSED;
    }

    protected int resolveFail(Character performer) {
        // boo, failed
        Log.d("Eric","failed the check");
        for (CheckResult checkResult : mFailResults) {
            checkResult.applyResult(performer, mTargetGroups);
        }
        for (TargetGroup targetGroup : mTargetGroups) {
            targetGroup.resetTargets();
        }
        return Action.RESULT_FAILED_CHECK;
    }
}
