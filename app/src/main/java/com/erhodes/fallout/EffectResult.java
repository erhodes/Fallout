package com.erhodes.fallout;

import java.util.ArrayList;

/**
 * The result of a SkillCheck. Applies a number of effects to a group of targets.
 */
public class EffectResult extends CheckResult {
    ArrayList<Effect> mPerformerEffects, mTargetEffects;  // what happens to that character?

    /**
     * Useful for when only a single effect is applied to the target and/or performer.
     * Either effect can be null, indicating no effect on that character.
     * @param performerEffect
     * @param targetEffect
     */
    EffectResult(Effect performerEffect, Effect targetEffect) {
        mPerformerEffects = new ArrayList<>();
        mTargetEffects = new ArrayList<>();

        if (performerEffect != null) {
            mPerformerEffects.add(performerEffect);
        }
        if (targetEffect != null) {
            mTargetEffects.add(targetEffect);
        }
    }

    EffectResult(ArrayList<Effect> performerEffects, ArrayList<Effect> targetEffects) {
        mPerformerEffects = performerEffects;
        mTargetEffects = targetEffects;
    }

    @Override
    public void applyResult(Character performer, Character target) {
        for (Effect e : mPerformerEffects) {
            performer.applyEffect(e);
        }
        for (Effect e : mTargetEffects) {
            target.applyEffect(e);
        }
    }

    @Override
    public boolean requiresTarget() {
        if (mTargetEffects.size() > 0) {
            return true;
        }
        return false;
    }
}
