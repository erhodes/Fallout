package com.erhodes.fallout;

import java.util.ArrayList;

/**
 * The result of a SkillCheck. Applies a number of effects to a group of targets.
 */
public class EffectResult extends CheckResult {
    ArrayList<Effect> mTargetEffects;
    boolean mAffectsPerformer;

    EffectResult(String attributeKey, int magnitude) {
        this(new Effect(attributeKey, magnitude), false);
    }
    EffectResult(String attributeKey, int magnitude, boolean affectsPerformer) {
        this(new Effect(attributeKey, magnitude), affectsPerformer);
    }
    EffectResult(Effect effect, boolean affectsPerformer) {
        super();
        mAffectsPerformer = affectsPerformer;
        mTargetEffects = new ArrayList<>();
        mTargetEffects.add(effect);
    }

    EffectResult(ArrayList<Effect> targetEffects) {
        super();
        mTargetEffects = targetEffects;
    }

    @Override
    public void applyResult(Character performer, ArrayList<TargetGroup> mTargetGroups) {
        if (mAffectsPerformer) {
            for (Effect e : mTargetEffects) {
                performer.applyEffect(e);
            }
        }
        for (int i = 0; i < mAffectedTargetGroups.size(); i++) {
            applyEffects(mTargetGroups.get(mAffectedTargetGroups.get(i)));
        }
    }

    private void applyEffects(TargetGroup targetGroup) {
        for (GameObject gameObject : targetGroup.mTargets) {
            for (Effect e: mTargetEffects) {
                gameObject.applyEffect(e);
            }
        }
    }
}
