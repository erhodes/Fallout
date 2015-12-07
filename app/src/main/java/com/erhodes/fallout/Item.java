package com.erhodes.fallout;

import java.util.ArrayList;

/**
 * Created by Eric on 24/10/2015.
 */
public class Item {
    public static final String TYPE_ARMOR = "armor";
    public static final String TYPE_WEAPON = "weapon";
    public static final String TYPE_CONSUMABLE = "consumable";
    public static final String TYPE_DEFAULT = "default";
    public static final String TYPE_MISC = "misc";

    String displayName, description, id, type = TYPE_MISC;
    int weight;
    ArrayList<Effect> effects;
    ArrayList<Action> actions;

    public Item() {}

    public Item(String name, String desc) {
        displayName = name;
        description = desc;
        weight = 0;
        effects = new ArrayList<>();
        actions = new ArrayList<>();
    }

    public Item (String n, String desc, String i, String t, int w) {
        displayName = n;
        description = desc;
        id = i;
        type = t;
        weight = w;
        effects = new ArrayList<>();
        actions = new ArrayList<>();
    }

    //half assed copy constructor. Not sure I need it
    public Item(Item i) {
        displayName = i.displayName;
        description = i.description;
        weight = i.weight;
        effects = new ArrayList<>(i.effects);
    }
}
