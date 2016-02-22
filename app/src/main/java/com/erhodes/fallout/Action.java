package com.erhodes.fallout;

import android.util.Log;

import java.util.ArrayList;

/**
 * Actions are performed by spending Action Points (AP). When a character performs an Action, all of it's effects are applied to that character.
 * All the effects of an action should be temporary.
 */
public class Action {
    private static String TAG = "Action";

    public static final int RESULT_PASSED  = 0;
    public static final int RESULT_INSUFFICIENT_AP = 1;
    public static final int RESULT_FAILED_CHECK = 2;
    public static final int RESULT_MISSING_TARGETS = 3;

    String name, description;
    int cost;
    SkillCheck skillCheck = null;
    ArrayList<Effect> performerEffects, mTargetEffects, mSecondaryTargetEffects;

    public Action() {}

    public Action(String n, String d, int c) {
        name = n;
        description = d;
        cost = c;
        performerEffects = new ArrayList<>();
        mTargetEffects = new ArrayList<>();
    }

    public int requiredTargets() {
        if (skillCheck == null) {
            return 0;
        } else {
            return skillCheck.requiredTargets();
        }
    }
    /**
     * Performs the action on the given character. This will apply all of this action's effects
     * to that character.
     * @param performer
     */
    public int  performAction(Character performer) {
        if (requiredTargets() > 0) {
            Log.d(TAG, "This action requires a target to perform");
            return RESULT_MISSING_TARGETS;
        }
        return performAction(performer, null);
    }

    /** Perform an action that requires a single target
     *
     * @param performer
     * @param primaryTarget
     */
    public int performAction(Character performer, Character primaryTarget) {
        if (performer.mActionPoints < cost) {
            return RESULT_INSUFFICIENT_AP;
        }
        performer.mActionPoints -= cost;

        for (Effect e : performerEffects) {
            performer.applyEffect(e);
        }
        for (Effect e : mTargetEffects) {
            primaryTarget.applyEffect(e);
        }

        if (skillCheck != null) {
            return skillCheck.makeCheck(performer, primaryTarget);
        }
        return RESULT_PASSED;
    }
}
