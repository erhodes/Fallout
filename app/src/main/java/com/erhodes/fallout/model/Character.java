package com.erhodes.fallout.model;

import android.util.Log;

import com.erhodes.fallout.ItemManager;
import com.erhodes.fallout.model.skillcheck.AutopassSkillCheck;
import com.erhodes.fallout.model.skillcheck.EffectResult;
import com.erhodes.fallout.model.skillcheck.OpposedStaticSkillCheck;
import com.erhodes.fallout.model.skillcheck.SkillCheck;
import com.erhodes.fallout.model.skillcheck.StaticSkillCheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Eric on 17/10/2015.
 */
public class Character extends GameObject {
    private HashSet<String> mAcquiredPerks;
    private ArrayList<Item> mQuickItems;
    private HashMap<String, Item> mInventory;
    private ArrayList<Action> mActions;
    private ArrayList<Effect> mActiveEffects;
    private Item mArmor;
    private Weapon mWeapon;
    public int mCarriedWeight, mActionPoints, mCurrentExperience, mSpentExperience, mAvailablePerks;
    public String name;


    public Character(String n) {
        super();
        name = n;
        mAcquiredPerks = new HashSet<>();
        mInventory = new HashMap<>();
        mActions = new ArrayList<>();
        mActiveEffects = new ArrayList<>();
        mQuickItems = new ArrayList<>();

        mAttributes.put(Attributes.STRENGTH, new Attribute("Strength",Attributes.STRENGTH,4));
        mAttributes.put(Attributes.ENDURANCE, new Attribute("Endurance",Attributes.ENDURANCE,4));
        mAttributes.put(Attributes.AGILITY, new Attribute("Agility",Attributes.AGILITY, 4));
        mAttributes.put(Attributes.RESOLVE, new Attribute("Resolve",Attributes.RESOLVE, 4));
        mAttributes.put(Attributes.INTELLIGENCE, new Attribute("Intelligence",Attributes.INTELLIGENCE, 4));

        //    *** DERIVED ATTRIBUTES ***
        mAttributes.put(Attributes.MAX_MORALE, new DerivedAttribute("Max Morale", Attributes.MAX_MORALE, mAttributes.get(Attributes.RESOLVE), 3, 0));
        mAttributes.put(Attributes.MORALE, new CapacityAttribute("Morale", mAttributes.get(Attributes.MAX_MORALE)));
        mAttributes.put(Attributes.WEIGHT_LIMIT, new DerivedAttribute("Weight Limit", Attributes.WEIGHT_LIMIT, mAttributes.get(Attributes.STRENGTH), 25, 0));
        mAttributes.put(Attributes.TOUGHNESS, new DerivedAttribute("Toughness", Attributes.TOUGHNESS, mAttributes.get(Attributes.ENDURANCE), 1, 0));
        mAttributes.put(Attributes.MAX_HEALTH, new DerivedAttribute("Maximum Health",Attributes.MAX_HEALTH, mAttributes.get(Attributes.ENDURANCE), 5, 0));
        mAttributes.put(Attributes.HEALTH, new CapacityAttribute("Current Health", mAttributes.get(Attributes.MAX_HEALTH)));
        mAttributes.put(Attributes.ACTION_POINTS, new DerivedAttribute("Max Action Points",Attributes.ACTION_POINTS, mAttributes.get(Attributes.AGILITY), 0.5f, 5));
        mAttributes.put(Attributes.DEFENCE, new DerivedAttribute("Defence", Attributes.DEFENCE, mAttributes.get(Attributes.AGILITY), 1, 10));

        //  *** SKILLS! ***
        mAttributes.put(Skills.GUNS, new Skill("Guns",Skills.GUNS, mAttributes.get(Attributes.AGILITY)));
        mAttributes.put(Skills.MEDICINE, new Skill("Medicine",Skills.MEDICINE, mAttributes.get(Attributes.INTELLIGENCE)));
        mAttributes.put(Skills.MELEE, new Skill("Melee",Skills.MELEE, mAttributes.get(Attributes.STRENGTH)));

        mArmor = ItemManager.getNoArmor();
        mWeapon = ItemManager.getFists();
        calculateAttributes();
        addDefaultActions();
        mActionPoints = getAttributeValue(Attributes.ACTION_POINTS);
    }

    public String getName() {
        return name;
    }

    public void calculateAttributes() {
        for (String s: Attributes.getPrimaryAttributes()) {
            mAttributes.get(s).calculateFinalValue();
        }
        for (String s: Attributes.getDerivedAttributes()) {
            mAttributes.get(s).calculateFinalValue();
        }
        for (String s: Skills.getAllSkills()) {
            mAttributes.get(s).calculateFinalValue();
        }
    }

    @Override
    public void modifyAttribute(String attrKey, int mag) {
        super.modifyAttribute(attrKey, mag);
        calculateAttributes();
    }

    public boolean isValidAttribute(String attributeKey) {
        return Attributes.getAllCharacterAttributes().contains(attributeKey);
    }

    public ArrayList<Attribute> getPrimaryAttributes() {
        ArrayList<Attribute> result = new ArrayList<>();
        for (String attributeKey : Attributes.getPrimaryAttributes()) {
            result.add(getAttribute(attributeKey));
        }
        return result;
    }

    public ArrayList<Skill> getSkills() {
        ArrayList<Skill> result = new ArrayList<>();
        for (String attributeKey : Skills.getAllSkills()) {
            Attribute attribute = getAttribute(attributeKey);
            if (attribute instanceof Skill)
                result.add((Skill)getAttribute(attributeKey));
        }
        return result;
    }

    @Override
    public void applyEffect(Effect e) {
        modifyAttribute(e.key, e.magnitude);

        if (e.duration > 0) {
            Effect effect = new Effect(e);
            mActiveEffects.add(effect);
        }
    }

    // called when this character begins a new turn
    public void newTurn() {
        mActionPoints = getAttributeValue(Attributes.ACTION_POINTS);
        Iterator iterator = mActiveEffects.iterator();
        while (iterator.hasNext()) {
            Effect e = (Effect)iterator.next();
            e.duration--;
            if (e.duration <1) {
                iterator.remove();
                removeEffect(e);
            }
        }
    }

    public void addRank(String skillId) {
        if (mCurrentExperience < 1)
            return;
        Skill skill = (Skill)getAttribute(skillId);
        skill.addRank();
        mCurrentExperience--;
        mSpentExperience++;
        if (mSpentExperience >= 5) {
            mSpentExperience = 0;
            mAvailablePerks++;
        }
    }
    // Perk Methods
    public boolean hasPerk(Perk p) {
        return mAcquiredPerks.contains(p.id);
    }

    public boolean acquirePerk(Perk p) {
        if (mAvailablePerks > 0 && applyPerk(p)) {
            mAvailablePerks--;
            return true;
        }
        return false;
    }

    public boolean applyPerk(Perk p){
        if (hasPerk(p))
            return false;
        mAcquiredPerks.add(p.id);
        for (Effect e: p.effects) {
            applyEffect(e);
        }
        calculateAttributes();
        return true;
    }

    public boolean removePerk(Perk p) {
        if (!hasPerk(p))
            return false;
        mAcquiredPerks.remove(p.id);
        for (Effect e: p.effects) {
            removeEffect(e);
        }
        return true;
    }

    public List<Perk> getPerks() {
        return PerkManager.getInstance().getPerks(mAcquiredPerks);
    }

    // Action Methods
    private void addDefaultActions() {
        Action dodge = new Action("Dodge","Move out of the way",1);
        dodge.performerEffects.add(new Effect(Attributes.DEFENCE, 2, 1));
        mActions.add(dodge);

        // these are test actions and will need to be removed later
        Action heal = new Action("Heal","Healing magic for your allies",1);
        SkillCheck healCheck = new AutopassSkillCheck();
        healCheck.addTargetGroup(TargetGroup.TARGET_PRIMARY, new TargetGroup("Primary Target", 1, 1));
        EffectResult healResult = new EffectResult(Attributes.HEALTH, 5);
        healResult.addAffectedTargetGroup(TargetGroup.TARGET_PRIMARY);
        healCheck.addPassResult(healResult);
        heal.skillCheck = healCheck;
        mActions.add(heal);

        Action selfHeal = new Action("Self Heal", "Attempt to heal yourself",1);
        SkillCheck medCheck = new StaticSkillCheck(Skills.MEDICINE, 10);
        EffectResult selfHealResult = new EffectResult(Attributes.HEALTH, 5, true);
        medCheck.addPassResult(selfHealResult);
        selfHeal.skillCheck = medCheck;
        mActions.add(selfHeal);

        Action firebolt = new Action("Fire Bolt", "Deal fire damage to an enemy",2);
        TargetGroup enemyGroup = new TargetGroup("Primary Target", 1, 1);
        SkillCheck fireCheck = new OpposedStaticSkillCheck(Skills.GUNS, Attributes.DEFENCE, enemyGroup);
        EffectResult damageResult = new EffectResult(Attributes.HEALTH, -6);
        damageResult.addAffectedTargetGroup(TargetGroup.TARGET_PRIMARY);
        fireCheck.addPassResult(damageResult);
        firebolt.skillCheck = fireCheck;
        mActions.add(firebolt);
    }

    public ArrayList<Action> getActions() {
        return mActions;
    }

    public int takeAction(Action a) {
        return a.performAction(this);
    }

    // *****************   Inventory Methods         ***************************

    /**
     * Add an item to the character's inventory. Returns true if successful, false if the character
     * cannot carry the additional weight.
     * @param item
     * @return
     */
    public boolean acquireItem(Item item) {
        if (item.mWeight + mCarriedWeight > getAttributeValue(Attributes.WEIGHT_LIMIT)) {
            return false;
        }
        mCarriedWeight += item.mWeight;
        if (mInventory.containsKey(item.mId)) {
            mInventory.get(item.mId).mQuantity++;
        } else {
            Item newItem;
            if (item instanceof AmmoWeapon) {
                Log.d("Eric", "acquiring ammoweapon");
                newItem = new AmmoWeapon((AmmoWeapon) item);
            } else if (item instanceof Weapon) {
                Log.d("Eric","acquiring weapon");
                newItem = new Weapon((Weapon)item);
            } else {
                newItem = new Item(item);
            }
            mInventory.put(item.mId, newItem);
        }
        return true;
    }

    public ArrayList<Item> getInventory() {
        ArrayList<Item> items = new ArrayList<>();
        for (String string : mInventory.keySet()) {
            items.add(mInventory.get(string));
        }
        return new ArrayList<>(mInventory.values());
    }

    public void removeItemFromInventory(Item item) {
        if (!mInventory.containsKey(item.mId)) {
            return;
        }
        mCarriedWeight -= item.mWeight;
        mInventory.get(item.mId).mQuantity--;
        if (mInventory.get(item.mId).mQuantity < 1) {
            mInventory.remove(item.mId);
        }
    }

    /**
     * Returns how many of a given item this character has in his inventory. Doesn't look at equipped weapon/armor.
     * @param itemId
     * @return
     */
    public int hasItem(String itemId) {
        Item item = mInventory.get(itemId);
        return item == null ? 0 : item.mQuantity;
    }

    /**
     * Removes a number of items with the given ID. Returns the number that could not be removed.
     * @param itemId
     * @param quantity
     * @return
     */
    public int removeItemsFromInventory(String itemId, int quantity) {
        if (!mInventory.containsKey(itemId))
            return quantity;
        Item item = mInventory.get(itemId);

        if (quantity >= item.mQuantity) {
            mInventory.remove(item.mId);
            mCarriedWeight -= item.mWeight * item.mQuantity;
            return quantity - item.mQuantity;
        } else {
            item.mQuantity -= quantity;
            mCarriedWeight -= item.mWeight * quantity;
            return 0;
        }
    }

    public boolean equipItem(Item i) {
        if (i.type.equals(Item.TYPE_ARMOR)) {
            return equipArmor(i);
        } else if (i.type.equals(Item.TYPE_CONSUMABLE)) {
            return equipConsumable(i);
        } else if (i.type.equals(Item.TYPE_WEAPON)) {
            return equipWeapon((Weapon)i);
        }
        return false;
    }

    public void unequipItem(Item i) {
        if (i.type.equals(Item.TYPE_ARMOR)) {
            unequipArmor();
        } else if (i.type.equals(Item.TYPE_CONSUMABLE)) {
            unequipConsumable(i);
        } else if (i.type.equals(Item.TYPE_WEAPON)) {
            unequipWeapon();
        }
    }

    private boolean equipConsumable(Item i) {
        mActions.addAll(i.actions);
        mQuickItems.add(i);
        return true;
    }

    public void unequipConsumable(Item i) {
        mActions.removeAll(i.actions);
        mQuickItems.remove(i);
    }

    public ArrayList<Item> getQuickItems() {
        return mQuickItems;
    }
    public boolean equipWeapon(Weapon weapon) {
        unequipWeapon();
        removeItemFromInventory(weapon);
        mWeapon = weapon;
        mCarriedWeight += weapon.mWeight;
        mActions.addAll(weapon.actions);
        return true;
    }

    public void unequipWeapon() {
        if (mWeapon.mId.equals(ItemManager.ITEM_DEFAULT_UNARMED)) {
            return;
        }
        mCarriedWeight -= mWeapon.mWeight;
        mActions.removeAll(mWeapon.actions);
        acquireItem(mWeapon);
        mWeapon = ItemManager.getFists();
    }

    public Weapon getWeapon() {
        return mWeapon;
    }
    public boolean equipArmor(Item i) {
        if (!i.type.equals(Item.TYPE_ARMOR)) {
            return false;
        }
        unequipArmor();
        removeItemFromInventory(i);
        mArmor = i;
        mCarriedWeight += i.mWeight;
        for (Effect e : mArmor.effects) {
            applyEffect(e);
        }
        return true;
    }

    public void unequipArmor() {
        mCarriedWeight -= mArmor.mWeight;
        // if the current armor isn't the default, then put it back into inventory
        if (!mArmor.type.equals(Item.TYPE_DEFAULT))
            acquireItem(mArmor);
        for (Effect e : mArmor.effects) {
            removeEffect(e);
        }
        mArmor = ItemManager.getNoArmor();
    }

    public Item getArmor() {
        return mArmor;
    }
}
