package com.erhodes.fallout;

/**
 * The result of a skill check. Forces a further skill check to occur.
 */
public class FurtherCheckResult extends CheckResult {
    SkillCheck mNextCheck;

    public FurtherCheckResult(SkillCheck nextCheck) {
        mNextCheck = nextCheck;
    }
    @Override
    public void applyResult(Character performer, Character target) {
        mNextCheck.makeCheck(performer, target);
    }

    public boolean requiresTarget() {
        return mNextCheck.requiresTarget();
    }
}
