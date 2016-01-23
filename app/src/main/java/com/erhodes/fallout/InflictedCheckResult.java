package com.erhodes.fallout;

/**
 * Forces the target to make a skill check.
 */
public class InflictedCheckResult extends CheckResult {
    SkillCheck mNextCheck;

    public InflictedCheckResult(SkillCheck nextCheck) {
        mNextCheck = nextCheck;
    }

    @Override
    public void applyResult(Character performer, Character target) {
        mNextCheck.makeCheck(target, performer);
    }

    public boolean requiresTarget() {
        return true;
    }
}
