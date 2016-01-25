package com.erhodes.fallout;

/**
 * Skills are what most checks are made against. Skills have ranks, which represents the amount
 * of training a character has in that skill. A skill can also be specialized, meaning it gains ranks
 * at twice the normal rate.
 */
public class Skill extends DerivedAttribute {
    boolean mSpecialized = false;

    Skill(String n, String k, Attribute bse) {
        super(n, k, bse, 0.5f, 0);
    }

    public void addRank() {
        int increase =  mSpecialized? 2 : 1;
        mBaseValue += increase;
    }

    public int getRanks() {
        return mBaseValue;
    }
}
