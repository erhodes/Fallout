package com.erhodes.fallout;

import java.util.ArrayList;

/**
 * Created by Eric on 24/10/2015.
 */
public class Item extends GameObject {
    public static final String TYPE_ARMOR = "armor";
    public static final String TYPE_WEAPON = "weapon";
    public static final String TYPE_CONSUMABLE = "consumable";
    public static final String TYPE_DEFAULT = "default";
    public static final String TYPE_MISC = "misc";

    String mDisplayName, mDescription, mId, type = TYPE_MISC;
    int mWeight;
    ArrayList<Effect> effects;
    ArrayList<Action> actions;

    public Item() {}

    public Item(String name, String desc) {
        this(name, desc, "temp", TYPE_DEFAULT, 0);
    }

    public Item (String name, String description, String id, String type, int weight) {
        super();
        mDisplayName = name;
        mDescription = description;
        mId = id;
        this.type = type;
        mWeight = weight;
        effects = new ArrayList<>();
        actions = new ArrayList<>();
    }

    //half assed copy constructor. Not sure I need it
    public Item(Item i) {
        mDisplayName = i.mDisplayName;
        mDescription = i.mDescription;
        mWeight = i.mWeight;
        effects = new ArrayList<>(i.effects);
    }

    public String getName() {
        return mDisplayName;
    }
    public boolean isValidAttribute(String attrKey) {
        return Attributes.getAllItemAttributes().contains(attrKey);
    }
    public void applyEffect(Effect e) {
        modifyAttribute(e.key, e.magnitude);
    }
}
