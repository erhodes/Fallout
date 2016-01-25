package com.erhodes.fallout;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 24/10/2015.
 */
public class ItemManager {
    public static String ITEM_LEATHER_ARMOR = "leather_armor";
    public static String ITEM_POISON = "poison";
    public static String ITEM_POISON_GUN = "poison_gun";
    public static String ITEM_DEFAULT_UNARMED = "default_unarmed";

    private static ArrayList<Item> mItems;
    /**
     * Loads all item descriptions into memory
     */
    public static void loadItems(Context context) {
        mItems = new ArrayList<>();
        try {
            Reader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("items.json")));
            Gson gson = new GsonBuilder().create();
            Item[] itemArray = gson.fromJson(reader, Item[].class);
            for (Item i : itemArray) {
                mItems.add(i);
            }
        } catch (IOException ex) {
            // well shit
        }
        // Poison
        Item poison = new Item("Poison","Deals damage to you", ITEM_POISON, Item.TYPE_CONSUMABLE, 1);
        Action poisonAction = new ItemAction("Drink Poison","Drink the poison and take some damage", 2, poison);
        poisonAction.performerEffects.add(new Effect(Attributes.HEALTH, -10, 0));
        poison.actions.add(poisonAction);
        mItems.add(poison);

        // Poison Gun
        Item poisonGun = new Item("Poison Gun", "Deals poison damage to a target if you hit them", ITEM_POISON_GUN, Item.TYPE_WEAPON, 1);
        Action poisonGunAction = new ItemAction("Poison Gun Attack","Shoot a poison dart a target", 2, poisonGun);
        SkillCheck skillCheck = new OpposedStaticSkillCheck(Skills.GUNS, Attributes.DEFENCE);

        SkillCheck poisonCheck = new StaticSkillCheck(Attributes.ENDURANCE, 30);
        poisonCheck.mFailResults.add(new EffectResult(new Effect(Attributes.HEALTH,-6), null));

        skillCheck.mPassResults.add(new EffectResult(null, new Effect(Attributes.HEALTH,-5)));
        skillCheck.mPassResults.add(new InflictedCheckResult(poisonCheck));

        poisonGunAction.skillCheck = skillCheck;
        poisonGun.actions.add(poisonGunAction);
        mItems.add(poisonGun);
    }

    public static ArrayList<Item> getAllItems() {
        return mItems;
    }

    public static Item getItem(String c) {
        for (Item i : mItems) {
            if (i.id.equals(c)) {
                return i;
            }
        }
        return null;
    }

    /**
     * Fists are the default equipped weapon.
     * @return
     */
    public static Item getFists() {
        Item i = new Item("Fists","You aren't holding anything",ITEM_DEFAULT_UNARMED, Item.TYPE_WEAPON, 0);
        return i;
    }
    public static Item getNoArmor() {
        Item i = new Item("Nothing", "You aren't wearing anything");
        i.type = Item.TYPE_DEFAULT;
        return i;
    }
}
