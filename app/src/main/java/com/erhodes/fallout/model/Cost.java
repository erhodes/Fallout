package com.erhodes.fallout.model;

import android.util.Log;
import android.util.SparseArray;

import java.lang.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Eric on 02/03/2016.
 */
public class Cost {
    int mPayeeGroup, mCost;
    String mAttributeKey;

    public Cost(String attributeKey, int cost, int payeeGroup) {
        mAttributeKey = attributeKey;
        mPayeeGroup = payeeGroup;
        mCost = cost;
    }

    // This is really only necessary because the performer isn't in a target group.
    private ArrayList<GameObject> buildPayingTargets(Character performer, HashMap<Integer, TargetGroup> targets) {
        ArrayList<GameObject> payingTargets;
        if (mPayeeGroup == TargetGroup.TARGET_PERFORMER) {
            payingTargets = new ArrayList<>();
            payingTargets.add(performer);
        } else {
            payingTargets = targets.get(mPayeeGroup).mTargets;
        }
        return payingTargets;
    }

    /**
     * Determine if the character can pay all costs. Sometimes the cost is paid by a target other than the performer
     * (ex, a gun must pay a cost in ammunition), so the targets for the skill check must also be supplied.
     * @param performer
     * @param targets
     * @return
     */
    public boolean canPayCost(Character performer, HashMap<Integer, TargetGroup> targets) {
        boolean allCanPay = true;
        ArrayList<GameObject> payingTargets = buildPayingTargets(performer, targets);
        for (GameObject gameObject : payingTargets) {
            Log.d("Eric", "object " + gameObject.getName() + " needs to pay " + mCost + " " + mAttributeKey + " and has " + gameObject.getAttributeValue(mAttributeKey));
            if (gameObject.getAttributeValue(mAttributeKey) < mCost) {
                return false;
            }
        }
        return true;
    }

    /**
     * You should run canPayCost first to make sure that everyone can actually pay the cost
     * @param performer
     * @param targets
     */
    public void payCost(Character performer, HashMap<Integer, TargetGroup> targets) {
        ArrayList<GameObject> payingTargets = buildPayingTargets(performer, targets);
        for (GameObject gameObject : payingTargets) {
            gameObject.modifyAttribute(mAttributeKey, -mCost);
        }
    }
}
