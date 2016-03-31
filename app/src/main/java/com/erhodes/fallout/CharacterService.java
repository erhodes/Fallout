package com.erhodes.fallout;

import com.erhodes.fallout.model.*;
import com.erhodes.fallout.model.Character;

import java.util.ArrayList;

/**
 * Created by Eric on 19/12/2015.
 */
public class CharacterService {
    private ArrayList<com.erhodes.fallout.model.Character> mCharacters;
    private Character mActiveCharacter;

    public CharacterService() {
        mCharacters = new ArrayList<>();
        mActiveCharacter = null;


    }

    public void addTestChars() {
        Character mainChar = new Character("Johanson");
        Item poison = ItemManager.getItem(ItemManager.ITEM_POISON);
        mainChar.acquireItem(poison);
        mainChar.equipItem(poison);
        Item poisonGun = ItemManager.getItem(ItemManager.ITEM_POISON_GUN);
        mainChar.acquireItem(poisonGun);
        Item revolver = ItemManager.getItem(ItemManager.ITEM_REVOLVER);
        mainChar.acquireItem(revolver);
        mainChar.equipItem(revolver);
        Item revolverAmmo = ItemManager.getItem(ItemManager.ITEM_38_ROUND);
        mainChar.acquireItem(revolverAmmo);
        mainChar.acquireItem(revolverAmmo);
        //mainChar.acquireItem(ItemManager.getItem(ItemManager.ITEM_LEATHER_ARMOR));
        mainChar.applyPerk(PerkManager.getInstance().getPerk(PerkManager.PERK_STRENGTH));
        mainChar.mCurrentExperience = 6;
        setActiveCharacter(mainChar);

        mCharacters.add(new Character("Raider 1"));
        mCharacters.add(new Character("Raider 2"));

        Character ally = new Character("Ally 1");
        ally.applyEffect(new Effect(Attributes.HEALTH, -15));
        mCharacters.add(ally);
    }

    public void setActiveCharacter(Character c) {
        mActiveCharacter = c;
    }

    public Character getActiveCharacter() {
        return mActiveCharacter;
    }

    public ArrayList<Character> getNonActiveCharacters() {
        return mCharacters;
    }

    public ArrayList<Character> getAllCharacters() {
        ArrayList<Character> result = new ArrayList<>(mCharacters);
        result.add(mActiveCharacter);
        return result;
    }
}
