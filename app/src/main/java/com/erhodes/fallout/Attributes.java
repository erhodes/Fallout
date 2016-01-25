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
    public static final String INTELLIGENCE = "intelligence";

    // derived attributes
    public static final String ACTION_POINTS = "action_points";
    public static final String DEFENCE = "defence";
    public static final String HEALTH = "health";
    public static final String MAX_HEALTH = "max_health";
    public static final String MORALE = "morale";
    public static final String TOUGHNESS = "toughness";
    public static final String WEIGHT_LIMIT = "weight_limit";

    // item attributes
    public static final String AMMUNITION_MAX = "ammunition_max";
    public static final String AMMUNITION_CURRENT = "ammunition_current";
    public static final String DAMAGE = "damage";

    public static ArrayList<String> getPrimaryAttributes() {
        ArrayList<String> attributes = new ArrayList<String>();
        attributes.add(STRENGTH);
        attributes.add(ENDURANCE);
        attributes.add(AGILITY);
        attributes.add(RESOLVE);
        attributes.add(INTELLIGENCE);
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

    public static ArrayList<String> getCapacityAttributes() {
        ArrayList<String> attributes = new ArrayList<String>();
        attributes.add(HEALTH);
        return attributes;
    }

    // should skills be in here?
    public static ArrayList<String> getAllCharacterAttributes() {
        ArrayList<String> attributes = new ArrayList<String>();
        attributes.addAll(getPrimaryAttributes());
        attributes.addAll(getDerivedAttributes());
        attributes.addAll(Skills.getAllSkills());
        attributes.addAll(getCapacityAttributes());
        return attributes;
    }

    public static ArrayList<String> getAllItemAttributes() {
        ArrayList<String> attributes = new ArrayList<>();
        return attributes;
    }

    public static ArrayList<String> getWeaponAttributes() {
        ArrayList<String> attributes = new ArrayList<>();
        attributes.addAll(getAllItemAttributes());
        attributes.add(AMMUNITION_CURRENT);
        attributes.add(AMMUNITION_MAX);
        return attributes;
    }
}
