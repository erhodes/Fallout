package com.erhodes.fallout;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.erhodes.fallout.model.*;
import com.erhodes.fallout.model.Character;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Eric on 19/12/2015.
 */
@Singleton
public class CharacterRepository {
    private static final String TAG = "CharacterRepository";
    private ArrayList<Character> mCharacters;
    private MutableLiveData<Character> mActiveCharacterData;
    private Character mActiveCharacter;

    @Inject
    public CharacterRepository() {
        mCharacters = new ArrayList<>();
        mActiveCharacterData = new MutableLiveData<>();
        //TODO remove this line once a database is functional
        addTestChars();
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
        mainChar.mAvailablePerks++;
        for (int i = 0; i < 5; i++) {
            mainChar.addRank(Skills.GUNS);
        }
        setActiveCharacter(mainChar);

        mCharacters.add(new Character("Raider 1"));
        mCharacters.add(new Character("Raider 2"));

        Character ally = new Character("Ally 1");
        ally.applyEffect(new Effect(Attributes.HEALTH, -15));
        mCharacters.add(ally);
    }

    public void setActiveCharacter(Character c) {
        mActiveCharacter = c;
        mActiveCharacterData.setValue(c);
    }

    public LiveData<Character> getActiveCharacter() {
        return mActiveCharacterData;
    }

    public void addPerkToActiveCharacter(Perk perk) {
        // at some point, the active character will be LiveData from a database
        if (mActiveCharacter == null) {
            return;
        }
        mActiveCharacter.acquirePerk(perk);
        mActiveCharacterData.setValue(mActiveCharacter);
    }

    public void addSkillRankToActiveChar(String skillKey) {
        if (mActiveCharacter == null) {
            return;
        }
        mActiveCharacter.addRank(skillKey);
        mActiveCharacterData.setValue(mActiveCharacter);
    }

    public ArrayList<Character> getNonActiveCharacters() {
        return mCharacters;
    }

    public ArrayList<Character> getAllCharacters() {
        ArrayList<Character> result = new ArrayList<>(mCharacters);
        result.add(mActiveCharacterData.getValue());
        return result;
    }
}
