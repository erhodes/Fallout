package com.erhodes.fallout;

/**
 * Created by Eric on 20/10/2015.
 */
public abstract class DerivedAttribute extends Attribute {

    Attribute mBase;

    DerivedAttribute(String n, String k, Attribute bse) {
        name = n;
        key = k;
        mBase = bse;
    }

    @Override
    public abstract void calculateFinalValue();
}
