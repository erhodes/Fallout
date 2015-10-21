package com.erhodes.fallout;

import java.util.ArrayList;

/**
 * Created by Eric on 20/10/2015.
 */
public abstract class Attributes {
    // primary attributes
    public static final String STRENGTH = "strength";
    public static final String RESOLVE = "resolve";

    // derived attributes
    public static final String MORALE = "morale";

    public static ArrayList<String> getPrimaryAttributes() {
        ArrayList<String> attributes = new ArrayList<String>();
        attributes.add(STRENGTH);
        attributes.add(RESOLVE);
        return attributes;
    }

    public static ArrayList<String> getDerivedAttributes() {
        ArrayList<String> attributes = new ArrayList<String>();
        attributes.add(MORALE);
        return attributes;
    }

    public static ArrayList<String> getAllAttributes() {
        ArrayList<String> attributes = new ArrayList<String>();
        attributes.addAll(getPrimaryAttributes());
        attributes.addAll(getDerivedAttributes());
        return attributes;
    }
}
