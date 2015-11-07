package com.erhodes.fallout;

import java.util.ArrayList;

/**
 * Created by Eric on 20/10/2015.
 */
public abstract class Attributes {
    // primary attributes
    public static final String STRENGTH = "strength";
    public static final String ENDURANCE = "endurance";
    public static final String AGILITY = "agility";
    public static final String RESOLVE = "resolve";

    // derived attributes
    public static final String ACTION_POINTS = "action_points";
    public static final String DEFENCE = "defence";
    public static final String HEALTH = "health";
    public static final String MAX_HEALTH = "max_health";
    public static final String MORALE = "morale";
    public static final String TOUGHNESS = "toughness";
    public static final String WEIGHT_LIMIT = "weight_limit";

    public static ArrayList<String> getPrimaryAttributes() {
        ArrayList<String> attributes = new ArrayList<String>();
        attributes.add(STRENGTH);
        attributes.add(ENDURANCE);
        attributes.add(AGILITY);
        attributes.add(RESOLVE);
        return attributes;
    }

    public static ArrayList<String> getDerivedAttributes() {
        ArrayList<String> attributes = new ArrayList<String>();
        attributes.add(ACTION_POINTS);
        attributes.add(DEFENCE);
        attributes.add(MAX_HEALTH);
        attributes.add(MORALE);
        attributes.add(TOUGHNESS);
        attributes.add(WEIGHT_LIMIT);
        return attributes;
    }

    public static ArrayList<String> getAllAttributes() {
        ArrayList<String> attributes = new ArrayList<String>();
        attributes.addAll(getPrimaryAttributes());
        attributes.addAll(getDerivedAttributes());
        return attributes;
    }
}
