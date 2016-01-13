package com.erhodes.fallout;

/**
 * Skills are what most checks are made against. Skills have ranks, which represents the amount
 * of training a character has in that skill. A skill can also be specialized, meaning it gains ranks
 * at twice the normal rate.
 */
public class Skill extends DerivedAttribute {
    int mRanks;
    boolean mSpecialized = false;

    Skill(String n, String k, Attribute bse) {
        super(n, k, bse);
        mRanks = 0;
    }

    @Override
    public void calculateFinalValue() {
        finalValue = mBase.getFinalValue()/2 + mRanks + modifier;
    }

    public void addRank() {
        int increase =  mSpecialized? 2 : 1;
        mRanks += increase;
    }
}
