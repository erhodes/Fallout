package com.erhodes.fallout;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Eric on 17/10/2015.
 */
public class Character extends GameObject {
    private HashSet<String> mAcquiredPerks;
    private ArrayList<Item> mInventory;
    private ArrayList<Action> mActions;
    private ArrayList<Effect> mActiveEffects;
    private Item mArmor, mWeapon;
    public int mCarriedWeight, mHealth, mActionPoints;
    public String name;


    Character(String n) {
        super();
        name = n;
        mAcquiredPerks = new HashSet<>();
        mInventory = new ArrayList<>();
        mActions = new ArrayList<>();
        mActiveEffects = new ArrayList<>();

        mAttributes.put(Attributes.STRENGTH, new Attribute("Strength",Attributes.STRENGTH,4));
        mAttributes.put(Attributes.ENDURANCE, new Attribute("Endurance",Attributes.ENDURANCE,4));
        mAttributes.put(Attributes.AGILITY, new Attribute("Agility",Attributes.AGILITY, 4));
        mAttributes.put(Attributes.RESOLVE, new Attribute("Resolve",Attributes.RESOLVE, 4));
        mAttributes.put(Attributes.INTELLIGENCE, new Attribute("Intelligence",Attributes.INTELLIGENCE, 4));

        //    *** DERIVED ATTRIBUTES ***
        //maybe derived attributes should each be their own class. Might be a better way to organize the logic
        mAttributes.put(Attributes.MORALE, new DerivedAttribute("Morale",Attributes.MORALE,mAttributes.get(Attributes.RESOLVE)) {
            @Override
            public void calculateFinalValue() {
                finalValue = mBase.getFinalValue() * 3 + modifier;
            }
        });

        mAttributes.put(Attributes.WEIGHT_LIMIT, new DerivedAttribute("Weight Limit", Attributes.WEIGHT_LIMIT, mAttributes.get(Attributes.STRENGTH)) {
            @Override
            public void calculateFinalValue() {
                finalValue = mBase.getFinalValue() * 25 + modifier;
            }
        });

        mAttributes.put(Attributes.TOUGHNESS, new DerivedAttribute("Toughness", Attributes.TOUGHNESS, mAttributes.get(Attributes.ENDURANCE)) {
            @Override
            public void calculateFinalValue() {
                finalValue = mBase.getFinalValue() + modifier;
            }
        });

        mAttributes.put(Attributes.MAX_HEALTH, new DerivedAttribute("Current Health",Attributes.MAX_HEALTH, mAttributes.get(Attributes.ENDURANCE)) {
            @Override
            public void calculateFinalValue() { finalValue = mBase.getFinalValue()*5 + modifier; }
        });

        mAttributes.put(Attributes.ACTION_POINTS, new DerivedAttribute("Action Points",Attributes.ACTION_POINTS, mAttributes.get(Attributes.AGILITY)) {
            @Override
            public void calculateFinalValue() {
                finalValue = 5 + mBase.getFinalValue()/2 + modifier;
            }
        });

        mAttributes.put(Attributes.DEFENCE, new DerivedAttribute("Defence", Attributes.DEFENCE, mAttributes.get(Attributes.AGILITY)) {
            @Override
            public void calculateFinalValue() {
                finalValue = 10 + mBase.getFinalValue() + modifier;
            }
        });

        //  *** SKILLS! ***
        mAttributes.put(Skills.GUNS, new Skill("Guns",Skills.GUNS, mAttributes.get(Attributes.AGILITY)));
        mAttributes.put(Skills.MEDICINE, new Skill("Medicine",Skills.MEDICINE, mAttributes.get(Attributes.INTELLIGENCE)));
        mAttributes.put(Skills.MELEE, new Skill("Melee",Skills.MELEE, mAttributes.get(Attributes.STRENGTH)));

        mArmor = ItemManager.getNoArmor();
        mWeapon = ItemManager.getFists();
        calculateAttributes();
        addDefaultActions();
        mActionPoints = getAttribute(Attributes.ACTION_POINTS);
        mHealth = getAttribute(Attributes.MAX_HEALTH);
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

    @Override
    public void applyEffect(Effect e) {
        if (e.key.equals(Attributes.HEALTH)) {
            mHealth += e.magnitude;
            if (mHealth > getAttribute(Attributes.MAX_HEALTH)) {
                mHealth = getAttribute(Attributes.MAX_HEALTH);
            } else if (mHealth <= 0) {
                // this character is dead. Eventually that will mean something.
            }
        } else {
            modifyAttribute(e.key, e.magnitude);
        }
        if (e.duration > 0) {
            Effect effect = new Effect(e);
            mActiveEffects.add(effect);
        }
    }

    // called when this character begins a new turn
    public void newTurn() {
        mActionPoints = getAttribute(Attributes.ACTION_POINTS);
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

    // Perk Methods
    public boolean hasPerk(Perk p) {
        return mAcquiredPerks.contains(p.id);
    }
    public boolean applyPerk(Perk p){
        if (hasPerk(p))
            return false;
        mAcquiredPerks.add(p.id);
        for (Effect e: p.effects) {
            applyEffect(e);
        }
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
    // Action Methods
    private void addDefaultActions() {
        Action dodge = new Action("Dodge","Move out of the way",1);
        dodge.performerEffects.add(new Effect(Attributes.DEFENCE, 2, 1));
        mActions.add(dodge);

        // these are test actions and will need to be removed later
        Action heal = new Action("Heal","Healing magic for your allies",1);
        heal.mTargetEffects.add(new Effect(Attributes.HEALTH, 5));
        mActions.add(heal);

        Action selfHeal = new Action("Self Heal", "Attempt to heal yourself",1);
        SkillCheck medCheck = new SkillCheck(Skills.MEDICINE, 10);
        medCheck.mPassResults.add(new EffectResult(new Effect(Attributes.HEALTH, 5), null));
        selfHeal.skillCheck = medCheck;
        mActions.add(selfHeal);

        Action firebolt = new Action("Fire Bolt", "Deal fire damage to an enemy",2);
        SkillCheck fireCheck = new SkillCheck(Skills.GUNS, 10);
        fireCheck.mPassResults.add(new EffectResult(null, new Effect(Attributes.HEALTH, -6)));
        firebolt.skillCheck = fireCheck;
        mActions.add(firebolt);
    }

    public ArrayList<Action> getActions() {
        return mActions;
    }

    public int takeAction(Action a) {
        return a.performAction(this);
    }

    public int takeAction(Action a, Character target) {
        return a.performAction(this, target);
    }

    // Inventory Methods

    /**
     * Add an item to the character's inventory. Returns true if successful, false if the character
     * cannot carry the additional weight.
     * @param i
     * @return
     */
    public boolean acquireItem(Item i) {
        if (i.weight + mCarriedWeight > getAttribute(Attributes.WEIGHT_LIMIT)) {
            return false;
        }
        mCarriedWeight += i.weight;
        mInventory.add(i);
        return true;
    }

    public ArrayList<Item> getInventory() {
        return mInventory;
    }

    public void removeItemFromInventory(Item i) {
        mCarriedWeight -= i.weight;
        mInventory.remove(i);
    }

    public boolean equipItem(Item i) {
        if (i.type.equals(Item.TYPE_ARMOR)) {
            return equipArmor(i);
        } else if (i.type.equals(Item.TYPE_CONSUMABLE)) {
            return equipConsumable(i);
        } else if (i.type.equals(Item.TYPE_WEAPON)) {
            return equipWeapon(i);
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
        return true;
    }

    public void unequipConsumable(Item i) {
        mActions.removeAll(i.actions);
    }

    public boolean equipWeapon(Item i) {
        if (!i.type.equals(Item.TYPE_WEAPON)) {
            return false;
        }
        unequipWeapon();
        removeItemFromInventory(i);
        mWeapon = i;
        mCarriedWeight += i.weight;
        mActions.addAll(i.actions);
        return true;
    }

    public void unequipWeapon() {
        if (mWeapon.id.equals(ItemManager.ITEM_DEFAULT_UNARMED)) {
            return;
        }
        mCarriedWeight -= mWeapon.weight;
        mActions.removeAll(mWeapon.actions);
        acquireItem(mWeapon);
        mWeapon = ItemManager.getFists();
    }

    public Item getWeapon() {
        return mWeapon;
    }
    public boolean equipArmor(Item i) {
        if (!i.type.equals(Item.TYPE_ARMOR)) {
            return false;
        }
        unequipArmor();
        removeItemFromInventory(i);
        mArmor = i;
        mCarriedWeight += i.weight;
        for (Effect e : mArmor.effects) {
            applyEffect(e);
        }
        return true;
    }

    public void unequipArmor() {
        mCarriedWeight -= mArmor.weight;
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
