package com.erhodes.fallout;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by Eric on 19/12/2015.
 */
public class CharacterService {
    private ArrayList<Character> mCharacters;
    private Character mActiveCharacter;

    public CharacterService() {
        mCharacters = new ArrayList<>();
        mActiveCharacter = null;

        addTestChars();
    }

    private void addTestChars() {
        Character mainChar = new Character("Johanson");
        Item poison = ItemManager.getItem(ItemManager.ITEM_POISON);
        mainChar.acquireItem(poison);
        mainChar.equipItem(poison);
        Item poisonGun = ItemManager.getItem(ItemManager.ITEM_POISON_GUN);
        mainChar.acquireItem(poisonGun);
        mainChar.equipItem(poisonGun);
        mainChar.acquireItem(ItemManager.getItem(ItemManager.ITEM_LEATHER_ARMOR));
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
