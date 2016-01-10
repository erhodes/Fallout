package com.erhodes.fallout;

import android.util.Log;

import java.util.ArrayList;

/**
 * Actions are performed by spending Action Points (AP). When a character performs an Action, all of it's effects are applied to that character.
 * All the effects of an action should be temporary.
 */
public class Action {
    private static String TAG = "Action";
    String name, description;
    int cost;
    ArrayList<Effect> performerEffects, mTargetEffects, mSecondaryTargetEffects;

    public Action() {}

    public Action(String n, String d, int c) {
        name = n;
        description = d;
        cost = c;
        performerEffects = new ArrayList<>();
        mTargetEffects = new ArrayList<>();
    }

    public boolean requiresTarget() {
        return mTargetEffects.size() > 0;
    }
    /**
     * Performs the action on the given character. This will apply all of this action's effects
     * to that character.
     * @param performer
     */
    public void performAction(Character performer) {
        if (requiresTarget()) {
            Log.d(TAG, "This action requires a target to perform");
            return;
        }
        performAction(performer, null);
    }

    /** Perform an action that requires a single target
     *
     * @param performer
     * @param primaryTarget
     */
    public void performAction(Character performer, Character primaryTarget) {
        for (Effect e : performerEffects) {
            performer.applyEffect(e);
        }
        for (Effect e : mTargetEffects) {
            primaryTarget.applyEffect(e);
        }
    }
}
