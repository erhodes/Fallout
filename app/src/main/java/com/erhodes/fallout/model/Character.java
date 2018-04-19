package com.erhodes.fallout.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
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

import javax.annotation.Nonnull;

/**
 * Created by Eric on 17/10/2015.
 */
@Entity
public class Character extends GameObject {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private HashSet<String> acquiredPerks;
    private ArrayList<Item> quickItems;
    private HashMap<String, Item> inventory;
    private ArrayList<Action> actions;
    private ArrayList<Effect> activeEffects;
    private Item armour;
    private Weapon weapon;
    public int carriedWeight, actionPoints, currentExperience, spentExperience, availablePerks;
    public String name;

    public Character() {
        // empty constructor for use by Room
    }

    public Character(String n) {
        super();
        name = n;
        acquiredPerks = new HashSet<>();
        inventory = new HashMap<>();
        actions = new ArrayList<>();
        activeEffects = new ArrayList<>();
        quickItems = new ArrayList<>();

        addAttribute(new Attribute("Strength",Attributes.STRENGTH,4));
        addAttribute(new Attribute("Endurance",Attributes.ENDURANCE,4));
        addAttribute(new Attribute("Agility",Attributes.AGILITY, 4));
        addAttribute(new Attribute("Resolve",Attributes.RESOLVE, 4));
        addAttribute(new Attribute("Intelligence",Attributes.INTELLIGENCE, 4));
        // fully initialize these before moving on
        for (String s: Attributes.getPrimaryAttributes()) {
            attributes.get(s).calculateFinalValue();
        }

        //    *** DERIVED ATTRIBUTES ***
        addDerivedAttribute(new DerivedAttribute("Max Morale", Attributes.MAX_MORALE, attributes.get(Attributes.RESOLVE), 3, 0));
        addCapacityAttribute(new CapacityAttribute("Morale", Attributes.MORALE, attributes.get(Attributes.MAX_MORALE)));
        addDerivedAttribute(new DerivedAttribute("Weight Limit", Attributes.WEIGHT_LIMIT, attributes.get(Attributes.STRENGTH), 25, 0));
        addDerivedAttribute(new DerivedAttribute("Toughness", Attributes.TOUGHNESS, attributes.get(Attributes.ENDURANCE), 1, 0));
        addDerivedAttribute(new DerivedAttribute("Maximum Health",Attributes.MAX_HEALTH, attributes.get(Attributes.ENDURANCE), 5, 0));
        addCapacityAttribute(new CapacityAttribute("Current Health", Attributes.HEALTH, attributes.get(Attributes.MAX_HEALTH)));
        addDerivedAttribute(new DerivedAttribute("Max Action Points",Attributes.ACTION_POINTS, attributes.get(Attributes.AGILITY), 0.5f, 5));
        addDerivedAttribute(new DerivedAttribute("Defence", Attributes.DEFENCE, attributes.get(Attributes.AGILITY), 1, 10));

        //  *** SKILLS! ***
        addDerivedAttribute(new Skill("Guns",Skills.GUNS, attributes.get(Attributes.AGILITY)));
        addDerivedAttribute(new Skill("Medicine",Skills.MEDICINE, attributes.get(Attributes.INTELLIGENCE)));
        addDerivedAttribute(new Skill("Melee",Skills.MELEE, attributes.get(Attributes.STRENGTH)));

        armour = ItemManager.getNoArmor();
        weapon = ItemManager.getFists();
        calculateAttributes();
        addDefaultActions();
        actionPoints = getAttributeValue(Attributes.ACTION_POINTS);
    }

    public String toString() {
        return name + " " + id;
    }
    public String getName() {
        return name;
    }

    public void calculateAttributes() {
        for (String s: Attributes.getDerivedAttributes()) {
            attributes.get(s).calculateFinalValue();
        }
        for (String s: Attributes.getCapacityAttributes()) {
            attributes.get(s).calculateFinalValue();
        }
        for (String s: Skills.getAllSkills()) {
            attributes.get(s).calculateFinalValue();
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

    // *** Effects
    @Override
    public void applyEffect(Effect e) {
        modifyAttribute(e.mKey, e.mMagnitude);

        if (e.mDuration > 0) {
            Effect effect = new Effect(e);
            activeEffects.add(effect);
        }
    }

    public ArrayList<Effect> getActiveEffects() {
        return activeEffects;
    }

    // called when this character begins a new turn
    public void newTurn() {
        actionPoints = getAttributeValue(Attributes.ACTION_POINTS);
        Iterator iterator = activeEffects.iterator();
        while (iterator.hasNext()) {
            Effect e = (Effect)iterator.next();
            e.mDuration--;
            if (e.isRecurring()) {
                GameLog.getInstance().addRecurringEffectEvent(getName(), e.getKey(), e.getMagnitude());
                modifyAttribute(e.getKey(), e.getMagnitude());
            }
            if (e.mDuration <1) {
                iterator.remove();
                removeEffect(e);
            }
        }
    }

    public void addRank(String skillId) {
        if (currentExperience < 1)
            return;
        Skill skill = (Skill)getAttribute(skillId);
        skill.addRank();
        currentExperience--;
        spentExperience++;
        if (spentExperience >= 5) {
            spentExperience = 0;
            availablePerks++;
        }
    }
    // Perk Methods
    public boolean hasPerk(Perk p) {
        return acquiredPerks.contains(p.id);
    }

    public boolean acquirePerk(Perk p) {
        if (availablePerks > 0 && applyPerk(p)) {
            availablePerks--;
            return true;
        }
        return false;
    }

    public boolean applyPerk(Perk p){
        if (hasPerk(p))
            return false;
        acquiredPerks.add(p.id);
        for (Effect e: p.effects) {
            applyEffect(e);
        }
        calculateAttributes();
        return true;
    }

    public boolean removePerk(Perk p) {
        if (!hasPerk(p))
            return false;
        acquiredPerks.remove(p.id);
        for (Effect e: p.effects) {
            removeEffect(e);
        }
        return true;
    }

    public List<Perk> getPerks() {
        return PerkManager.getInstance().getPerks(acquiredPerks);
    }

    // Action Methods
    private void addDefaultActions() {
        Action dodge = new Action("Dodge","Move out of the way",1);
        dodge.performerEffects.add(new Effect(Attributes.DEFENCE, 2, 1));
        actions.add(dodge);

        // these are test actions and will need to be removed later
        Action heal = new Action("Heal","Healing magic for your allies",1);
        SkillCheck healCheck = new AutopassSkillCheck();
        healCheck.addTargetGroup(TargetGroup.TARGET_PRIMARY, new TargetGroup("Primary Target", 1, 1));
        EffectResult healResult = new EffectResult(Attributes.HEALTH, 5);
        healResult.addAffectedTargetGroup(TargetGroup.TARGET_PRIMARY);
        healCheck.addPassResult(healResult);
        heal.skillCheck = healCheck;
        actions.add(heal);

        Action selfHeal = new Action("Self Heal", "Attempt to heal yourself",1);
        SkillCheck medCheck = new StaticSkillCheck(Skills.MEDICINE, 10);
        EffectResult selfHealResult = new EffectResult(Attributes.HEALTH, 5, true);
        medCheck.addPassResult(selfHealResult);
        selfHeal.skillCheck = medCheck;
        actions.add(selfHeal);

        Action adrenalineSurge = new Action("Adrenaline Surge","Take damage to boost your strength", 1);
        SkillCheck surgeCheck = new AutopassSkillCheck();
        EffectResult damageSelfResult = new EffectResult(new Effect(Attributes.HEALTH, -2, 5, true), true);
        EffectResult strengthBoostResult = new EffectResult(new Effect(Attributes.STRENGTH, 2, 5), true);
        surgeCheck.addPassResult(damageSelfResult);
        surgeCheck.addPassResult(strengthBoostResult);
        adrenalineSurge.skillCheck = surgeCheck;
        actions.add(adrenalineSurge);

        Action firebolt = new Action("Fire Bolt", "Deal fire damage to an enemy",2);
        TargetGroup enemyGroup = new TargetGroup("Primary Target", 1, 1);
        SkillCheck fireCheck = new OpposedStaticSkillCheck(Skills.GUNS, Attributes.DEFENCE, enemyGroup);
        EffectResult damageResult = new EffectResult(Attributes.HEALTH, -6);
        damageResult.addAffectedTargetGroup(TargetGroup.TARGET_PRIMARY);
        fireCheck.addPassResult(damageResult);
        firebolt.skillCheck = fireCheck;
        actions.add(firebolt);
    }

    public ArrayList<Action> getActions() {
        return actions;
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
        if (item.mWeight + carriedWeight > getAttributeValue(Attributes.WEIGHT_LIMIT)) {
            return false;
        }
        carriedWeight += item.mWeight;
        if (inventory.containsKey(item.mId)) {
            inventory.get(item.mId).mQuantity++;
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
            inventory.put(item.mId, newItem);
        }
        return true;
    }

    public ArrayList<Item> getInventoryList() {
        ArrayList<Item> items = new ArrayList<>();
        for (String string : inventory.keySet()) {
            items.add(inventory.get(string));
        }
        return new ArrayList<>(inventory.values());
    }

    public void removeItemFromInventory(Item item) {
        if (!inventory.containsKey(item.mId)) {
            return;
        }
        carriedWeight -= item.mWeight;
        inventory.get(item.mId).mQuantity--;
        if (inventory.get(item.mId).mQuantity < 1) {
            inventory.remove(item.mId);
        }
    }

    /**
     * Returns how many of a given item this character has in his inventory. Doesn't look at equipped weapon/armor.
     * @param itemId
     * @return
     */
    public int hasItem(String itemId) {
        Item item = inventory.get(itemId);
        return item == null ? 0 : item.mQuantity;
    }

    /**
     * Removes a number of items with the given ID. Returns the number that could not be removed.
     * @param itemId
     * @param quantity
     * @return
     */
    public int removeItemsFromInventory(String itemId, int quantity) {
        if (!inventory.containsKey(itemId))
            return quantity;
        Item item = inventory.get(itemId);

        if (quantity >= item.mQuantity) {
            inventory.remove(item.mId);
            carriedWeight -= item.mWeight * item.mQuantity;
            return quantity - item.mQuantity;
        } else {
            item.mQuantity -= quantity;
            carriedWeight -= item.mWeight * quantity;
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
        actions.addAll(i.actions);
        quickItems.add(i);
        return true;
    }

    public void unequipConsumable(Item i) {
        actions.removeAll(i.actions);
        quickItems.remove(i);
    }

    public ArrayList<Item> getQuickItems() {
        return quickItems;
    }
    public boolean equipWeapon(Weapon weapon) {
        unequipWeapon();
        removeItemFromInventory(weapon);
        this.weapon = weapon;
        carriedWeight += weapon.mWeight;
        actions.addAll(weapon.actions);
        return true;
    }

    public void unequipWeapon() {
        if (weapon.mId.equals(ItemManager.ITEM_DEFAULT_UNARMED)) {
            return;
        }
        carriedWeight -= weapon.mWeight;
        actions.removeAll(weapon.actions);
        acquireItem(weapon);
        weapon = ItemManager.getFists();
    }

    public Weapon getWeapon() {
        return weapon;
    }
    public boolean equipArmor(Item i) {
        if (!i.type.equals(Item.TYPE_ARMOR)) {
            return false;
        }
        unequipArmor();
        removeItemFromInventory(i);
        armour = i;
        carriedWeight += i.mWeight;
        for (Effect e : armour.effects) {
            applyEffect(e);
        }
        return true;
    }

    public void unequipArmor() {
        carriedWeight -= armour.mWeight;
        // if the current armor isn't the default, then put it back into inventory
        if (!armour.type.equals(Item.TYPE_DEFAULT))
            acquireItem(armour);
        for (Effect e : armour.effects) {
            removeEffect(e);
        }
        armour = ItemManager.getNoArmor();
    }

    public Item getArmor() {
        return armour;
    }

    // GETTERS AND SETTERS
    public HashSet<String> getAcquiredPerks() {
        return acquiredPerks;
    }
    public void setAcquiredPerks(HashSet<String> acquiredPerks) {
        this.acquiredPerks = acquiredPerks;
    }
    public void setQuickItems(ArrayList<Item> quickItems) {
        this.quickItems = quickItems;
    }
    public HashMap<String, Item> getInventory() {
        return inventory;
    }
    public void setInventory(HashMap<String, Item> inventory) {
        this.inventory = inventory;
    }
    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }
    public void setActiveEffects(ArrayList<Effect> activeEffects) {
        this.activeEffects = activeEffects;
    }
    public Item getArmour() {
        return armour;
    }
    public void setArmour(Item armour) {
        this.armour = armour;
    }
    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }
    public int getCarriedWeight() {
        return carriedWeight;
    }
    public void setCarriedWeight(int carriedWeight) {
        this.carriedWeight = carriedWeight;
    }
    public int getActionPoints() {
        return actionPoints;
    }
    public void setActionPoints(int actionPoints) {
        this.actionPoints = actionPoints;
    }
    public int getCurrentExperience() {
        return currentExperience;
    }
    public void setCurrentExperience(int currentExperience) {
        this.currentExperience = currentExperience;
    }
    public int getSpentExperience() {
        return spentExperience;
    }
    public void setSpentExperience(int spentExperience) {
        this.spentExperience = spentExperience;
    }
    public int getAvailablePerks() {
        return availablePerks;
    }
    public void setAvailablePerks(int availablePerks) {
        this.availablePerks = availablePerks;
    }
    public void setName(@Nonnull String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
