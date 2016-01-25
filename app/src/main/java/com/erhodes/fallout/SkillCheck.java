package com.erhodes.fallout;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class isn't very well named, since you could do attribute checks as well. Doesn't sound right though.
 */
public abstract class SkillCheck {
    String mSkillKey;
    protected Random mRandom;
    ArrayList<CheckResult> mPassResults, mFailResults;

    public SkillCheck(String skill) {
        mSkillKey = skill;
        mPassResults = new ArrayList<>();
        mFailResults = new ArrayList<>();
        mRandom = new Random();
    }

    /**
     * Does this SkillCheck require a target to properly resolve?
     * @return
     */
    public boolean requiresTarget() {
        boolean requiresTarget = false;
        for (CheckResult checkResult : mPassResults) {
            if (checkResult.requiresTarget()) {
                return true;
            }
        }
        for (CheckResult checkResult : mFailResults) {
            if (checkResult.requiresTarget()) {
                return true;
            }
        }
        return false;
    }

    public int roll(int bonus) {
        return mRandom.nextInt(20) + 1 + bonus;
    }

    public abstract int makeCheck(Character performer, Character target);

    protected int resolvePass(Character performer, Character target) {
        // yay, you passed!

        // apply the results of passing a skill check
        for (CheckResult checkResult : mPassResults) {
            checkResult.applyResult(performer, target);
        }
        return Action.RESULT_PASSED;
    }

    protected int resolveFail(Character performer, Character target) {
        // boo, failed
        for (CheckResult checkResult : mFailResults) {
            checkResult.applyResult(performer, target);
        }
        return Action.RESULT_FAILED_CHECK;
    }
}
