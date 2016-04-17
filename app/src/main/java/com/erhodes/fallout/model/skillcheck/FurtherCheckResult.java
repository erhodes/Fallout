package com.erhodes.fallout.model.skillcheck;

import android.util.SparseArray;

import com.erhodes.fallout.model.*;
import com.erhodes.fallout.model.Character;

import java.lang.*;
import java.util.ArrayList;
import java.util.HashMap;

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
    public void applyResult(Character performer, HashMap<Integer, TargetGroup> targetGroups) {
        mNextCheck.makeCheck(performer, targetGroups);
    }
}
