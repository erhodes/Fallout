package com.erhodes.fallout.model.skillcheck;

import android.util.Log;
import android.util.SparseArray;

import com.erhodes.fallout.model.*;
import com.erhodes.fallout.model.Character;

import java.lang.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The result of a SkillCheck. Applies a number of effects to a group of targets.
 */
public class EffectResult extends CheckResult {
    ArrayList<Effect> mTargetEffects;
    boolean mAffectsPerformer;

    public EffectResult(String attributeKey, int magnitude) {
        this(new Effect(attributeKey, magnitude), false);
    }
    public EffectResult(String attributeKey, int magnitude, boolean affectsPerformer) {
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
    public void addAffectedTargetGroup(int groupid) {
        super.addAffectedTargetGroup(groupid);
    }

    @Override
    public void applyResult(Character performer, HashMap<Integer, TargetGroup> targetGroups) {
        if (mAffectsPerformer) {
            for (Effect e : mTargetEffects) {
                performer.applyEffect(e);
            }
        }
        for (int i = 0; i < mAffectedTargetGroups.size(); i++) {
            applyEffects(targetGroups.get(mAffectedTargetGroups.get(i)));
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
