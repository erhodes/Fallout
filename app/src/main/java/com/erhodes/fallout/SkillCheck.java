package com.erhodes.fallout;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class isn't very well named, since you could do attribute checks as well. Doesn't sound right though.
 */
public class SkillCheck {
    int mDifficulty;
    String mSkillKey;
    ArrayList<CheckResult> mPassResults, mFailResults;

    public SkillCheck(String skill, int difficulty) {
        mSkillKey = skill;
        mDifficulty = difficulty;
        mPassResults = new ArrayList<>();
        mFailResults = new ArrayList<>();
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

    public int makeCheck(Character performer, Character target) {
        Random random = new Random();
        int skillValue = performer.getAttribute(mSkillKey);
        int roll = random.nextInt(20) + 1 + skillValue;
        Log.d("Eric",performer.name + " rolled a " + roll + "(" + (roll-skillValue) + "+" + skillValue + ") against difficulty " + mDifficulty);
        if (roll >= mDifficulty) {
            // yay, you passed!

            // apply the results of passing a skill check
            for (CheckResult checkResult : mPassResults) {
                checkResult.applyResult(performer, target);
            }
            return Action.RESULT_PASSED;
        } else {
            // boo, failed
            for (CheckResult checkResult : mFailResults) {
                checkResult.applyResult(performer, target);
            }
            return Action.RESULT_FAILED_CHECK;
        }
    }
}
