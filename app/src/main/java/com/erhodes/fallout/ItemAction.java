package com.erhodes.fallout;

import android.util.Log;

import java.util.ArrayList;

/**
 * An ItemAction is an action granted by an item. When performed, it can apply effects to the item
 * that granted it.
 */
public class ItemAction extends Action {
    Item mItem;
    ArrayList<Effect> itemEffects;

    public ItemAction(String n, String d, int c, Item i) {
        super(n, d, c);
        mItem = i;
        itemEffects = new ArrayList<>();
    }

    @Override
    public void performAction(Character c) {
        super.performAction(c);

        if (mItem.type.equals(Item.TYPE_CONSUMABLE)) {
            c.unequipConsumable(mItem);
            c.removeItemFromInventory(mItem);
        }
    }
}
