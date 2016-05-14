package com.erhodes.fallout.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Eric on 24/10/2015.
 */
public class Item extends GameObject {
    public static final String TYPE_ARMOR = "armor";
    public static final String TYPE_WEAPON = "weapon";
    public static final String TYPE_CONSUMABLE = "consumable";
    public static final String TYPE_DEFAULT = "default";
    public static final String TYPE_MISC = "misc";

    public String mDisplayName, mDescription, mId, type = TYPE_MISC;
    int mWeight, mQuantity = 1;
    public ArrayList<Effect> effects;
    public ArrayList<Action> actions;

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

    /**
     * Copy constructor
     * @param item
     */

    public Item(Item item) {
        mId = item.mId;
        type = item.type;
        mDisplayName = item.mDisplayName;
        mDescription = item.mDescription;
        mWeight = item.mWeight;
        mQuantity = item.mQuantity;
        effects = new ArrayList<>(item.effects);
        mAttributes = new HashMap<>(item.mAttributes);
        actions = item.actions;
    }

    public String getName() {
        return mDisplayName;
    }
    public String getDescription() { return mDescription; }
    public int getQuantity() { return mQuantity; }

    public boolean isValidAttribute(String attrKey) {
        return Attributes.getAllItemAttributes().contains(attrKey);
    }
    public void applyEffect(Effect e) {
        modifyAttribute(e.mKey, e.mMagnitude);
    }
}
