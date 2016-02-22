package com.erhodes.fallout;

import java.util.ArrayList;

/**
 * The result of a skill check. Forces a further skill check to occur.
 */
public class FurtherCheckResult extends CheckResult {
    SkillCheck mNextCheck;

    public FurtherCheckResult(SkillCheck nextCheck) {
        super();
        mNextCheck = nextCheck;
    }

    @Override
    public void applyResult(Character performer, ArrayList<TargetGroup> mTargetGroups) {
        mNextCheck.makeCheck(performer, mTargetGroups);
    }
}
