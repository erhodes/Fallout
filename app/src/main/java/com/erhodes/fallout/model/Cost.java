package com.erhodes.fallout.model;

import android.util.Log;

import java.lang.*;
import java.util.ArrayList;

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

    private ArrayList<GameObject> buildPayingTargets(Character performer, ArrayList<TargetGroup> mTargets) {
        ArrayList<GameObject> payingTargets;
        if (mPayeeGroup == -1) {
            payingTargets = new ArrayList<>();
            payingTargets.add(performer);
        } else {
            payingTargets = mTargets.get(mPayeeGroup).mTargets;
        }
        return payingTargets;
    }

    public boolean canPayCost(Character performer, ArrayList<TargetGroup> mTargets) {
        boolean allCanPay = true;
        ArrayList<GameObject> payingTargets = buildPayingTargets(performer, mTargets);
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
     * @param mTargets
     */
    public void payCost(Character performer, ArrayList<TargetGroup> mTargets) {
        ArrayList<GameObject> payingTargets = buildPayingTargets(performer, mTargets);
        for (GameObject gameObject : payingTargets) {
            gameObject.modifyAttribute(mAttributeKey, -mCost);
        }
    }
}
