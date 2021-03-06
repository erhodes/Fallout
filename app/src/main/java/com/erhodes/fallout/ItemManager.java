package com.erhodes.fallout;

import android.content.Context;

import com.erhodes.fallout.model.Action;
import com.erhodes.fallout.model.AmmoWeapon;
import com.erhodes.fallout.model.Attributes;
import com.erhodes.fallout.model.Effect;
import com.erhodes.fallout.model.TargetGroup;
import com.erhodes.fallout.model.skillcheck.EffectResult;
import com.erhodes.fallout.model.skillcheck.InflictedCheckResult;
import com.erhodes.fallout.model.Item;
import com.erhodes.fallout.model.skillcheck.SkillCheck;
import com.erhodes.fallout.model.skillcheck.StaticSkillCheck;
import com.erhodes.fallout.model.Weapon;

import java.util.ArrayList;

/**
 * Created by Eric on 24/10/2015.
 */
public class ItemManager {
    public static String ITEM_LEATHER_ARMOR = "leather_armor";
    public static String ITEM_POISON = "poison";
    public static String ITEM_POISON_GUN = "poison_gun";
    public static String ITEM_POISON_DART = "poison_dart";
    public static String ITEM_REVOLVER = "revolver";
    public static String ITEM_38_ROUND = ".38_round";
    public static String ITEM_DEFAULT_UNARMED = "default_unarmed";

    private static ArrayList<Item> mItems;
    /**
     * Loads all item descriptions into memory
     */
    public static void loadItems(Context context) {
        mItems = new ArrayList<>();
        /*
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
        */
        // Poison
        Item poison = new Item("Poison","Deals damage to you", ITEM_POISON, Item.TYPE_CONSUMABLE, 1);
        Action poisonAction = new ItemAction("Drink Poison","Drink the poison and take some damage", 2, poison);
        poisonAction.performerEffects.add(new Effect(Attributes.HEALTH, -10, 0));
        poison.actions.add(poisonAction);
        mItems.add(poison);

        // Revolver
        AmmoWeapon revolver = new AmmoWeapon("Revolver", "Classic 6 shooter", ITEM_REVOLVER, 4, 6, ITEM_38_ROUND, 6);
        revolver.actions.add(revolver.buildStandardAttackAction(2));
        mItems.add(revolver);

        Item revolverAmmo = new Item(".38 Round", ".38 special ammunition", ITEM_38_ROUND, Item.TYPE_CONSUMABLE, 1);
        mItems.add(revolverAmmo);

        // Poison Gun
        AmmoWeapon poisonGun = new AmmoWeapon("Poison Gun", "Deals poison damage to a target if you hit them", ITEM_POISON_GUN, 1, 2, ITEM_POISON_DART, 6);
        Action poisonGunAction = poisonGun.buildStandardAttackAction(2); //new ItemAction("Poison Gun Attack","Shoot a poison dart a target", 2, poisonGun);
        SkillCheck poisonGunAttackCheck = poisonGunAction.skillCheck;

        SkillCheck poisonCheck = new StaticSkillCheck(Attributes.ENDURANCE, 30);
        poisonCheck.addFailResult(new EffectResult(Attributes.HEALTH, -6, true));

        InflictedCheckResult poisonGunPoison = new InflictedCheckResult(poisonCheck);
        poisonGunPoison.addAffectedTargetGroup(TargetGroup.TARGET_PRIMARY);
        poisonGunAttackCheck.addPassResult(poisonGunPoison);

        poisonGun.actions.add(poisonGunAction);
        mItems.add(poisonGun);

        Item poisonDart = new Item("Poison Dart", "Ammo for the poison dart gun", ITEM_POISON_DART, Item.TYPE_MISC, 1);
        mItems.add(poisonDart);
    }

    public static ArrayList<Item> getAllItems() {
        return mItems;
    }

    public static Item getItem(String c) {
        for (Item i : mItems) {
            if (i.mId.equals(c)) {
                return i;
            }
        }
        return null;
    }

    /**
     * Fists are the default equipped weapon.
     * @return
     */
    public static Weapon getFists() {
        Weapon weapon = new Weapon("Fists","You aren't holding anything",ITEM_DEFAULT_UNARMED, 0, 1);
        return weapon;
    }
    public static Item getNoArmor() {
        Item i = new Item("Nothing", "You aren't wearing anything");
        i.type = Item.TYPE_DEFAULT;
        return i;
    }
}
