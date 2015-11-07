package com.erhodes.fallout;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Eric on 17/10/2015.
 */
public class Character {
    private HashMap<String, Attribute> mAttributes;
    private HashSet<String> mAcquiredPerks;
    private ArrayList<Item> mInventory;
    private ArrayList<Action> mActions;
    private Item mArmor;
    public int mCarriedWeight, mHealth, mActionPoints;
    public String name;


    Character(String n) {
        name = n;
        mAttributes = new HashMap<>();
        mAcquiredPerks = new HashSet<>();
        mInventory = new ArrayList<>();
        mActions = new ArrayList<>();

        mAttributes.put(Attributes.STRENGTH, new Attribute("Strength",Attributes.STRENGTH,4));
        mAttributes.put(Attributes.ENDURANCE, new Attribute("Endurance",Attributes.ENDURANCE,4));
        mAttributes.put(Attributes.AGILITY, new Attribute("Agility",Attributes.AGILITY, 4));
        mAttributes.put(Attributes.RESOLVE, new Attribute("Resolve",Attributes.RESOLVE, 4));

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

        mArmor = ItemManager.getNoArmor();
        calculateAttributes();
        addDefaultActions();
        mActionPoints = getAttribute(Attributes.ACTION_POINTS);
    }

    public void calculateAttributes() {
        for (String s: Attributes.getPrimaryAttributes()) {
            mAttributes.get(s).calculateFinalValue();
        }
        for (String s: Attributes.getDerivedAttributes()) {
            mAttributes.get(s).calculateFinalValue();
        }
    }
    public void modifyAttribute(String attrKey, int mag) {
        mAttributes.get(attrKey).addModifier(mag);
        calculateAttributes();
    }

    public int getAttribute(String attrKey) {
        return mAttributes.get(attrKey).getFinalValue();
    }
    public void applyEffect(Effect e) {
        modifyAttribute(e.key, e.magnitude);
    }

    public void removeEffect(Effect e) {
        modifyAttribute(e.key, -e.magnitude);
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
        dodge.effects.add(new Effect(Attributes.DEFENCE, 2));
        mActions.add(dodge);
    }

    public ArrayList<Action> getActions() {
        return mActions;
    }
    public boolean takeAction(Action a) {
        if (mActionPoints < a.cost) {
            return false;
        }
        mActionPoints -= a.cost;
        for (Effect e : a.effects) {
            applyEffect(e);
        }
        return true;
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
