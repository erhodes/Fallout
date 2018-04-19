package com.erhodes.fallout;

import com.erhodes.fallout.model.*;
import com.erhodes.fallout.model.Character;
import com.erhodes.fallout.model.skillcheck.EffectResult;
import com.erhodes.fallout.model.skillcheck.SkillCheck;
import com.erhodes.fallout.model.skillcheck.StaticSkillCheck;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Eric on 05/03/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class CharacterTests {
    private static final String TEST_CHAR_NAME = "Test Char";
    private static final String TEST_ITEM = "test_item";
    private static final String TEST_PERK = "test_perk";

    com.erhodes.fallout.model.Character mCharacter;

    @Before
    public void setup() {
        mCharacter = new Character(TEST_CHAR_NAME);
    }

    @Test
    public void nameTest() throws Exception {
        assertEquals(mCharacter.getName(), TEST_CHAR_NAME);
    }

    @Test
    public void testModifyAttributes() {
        int startingValue = 0;
        for (String attribute : Attributes.getAllCharacterAttributes()) {
            startingValue = mCharacter.getAttributeValue(attribute);
            mCharacter.modifyAttribute(attribute, 3);
            assertEquals(mCharacter.getAttributeValue(attribute), startingValue+3);
        }
    }

    @Test
    public void testAcquireItem() {
        Item item = new Item(TEST_ITEM, TEST_ITEM, TEST_ITEM, Item.TYPE_DEFAULT, 2);
        int startWeight = mCharacter.carriedWeight;
        int startingItems = mCharacter.hasItem(TEST_ITEM);
        mCharacter.acquireItem(item);

        assertEquals(startWeight + 2, mCharacter.carriedWeight);
        assertEquals(mCharacter.hasItem(TEST_ITEM), startingItems + 1);

        mCharacter.acquireItem(item);
        assertEquals(mCharacter.hasItem(TEST_ITEM), startingItems + 2);
        assertEquals(startWeight + 4, mCharacter.carriedWeight);

        assertEquals(0, mCharacter.removeItemsFromInventory(TEST_ITEM, 2));

        assertEquals(mCharacter.carriedWeight, startWeight);
        assertEquals(mCharacter.hasItem(TEST_ITEM), startingItems);
    }

    @Test
    public void testAcquireConsumable() {
        Item item = new Item(TEST_ITEM, TEST_ITEM, TEST_ITEM, Item.TYPE_CONSUMABLE, 2);
        Action itemAction = new Action("Item Action", "A test item action", 2);
        SkillCheck skillCheck = new StaticSkillCheck(Skills.MELEE, 1);
        skillCheck.addPassResult(new EffectResult(Attributes.STRENGTH, 2, true));
        itemAction.skillCheck = skillCheck;
        item.actions.add(itemAction);

        int startingStrength = mCharacter.getAttributeValue(Attributes.STRENGTH);

        assertFalse(mCharacter.getActions().contains(itemAction));
        mCharacter.acquireItem(item);
        mCharacter.equipItem(item);
        assertTrue(mCharacter.getActions().contains(itemAction));
        mCharacter.takeAction(itemAction);
        assertEquals(mCharacter.getAttributeValue(Attributes.STRENGTH), startingStrength+2);
    }

    @Test
    public void testAcquirePerk() {
        Perk perk = new Perk(TEST_PERK ,"this is a test perk");

        assertEquals(0, mCharacter.availablePerks);
        assertFalse(mCharacter.acquirePerk(perk));
        mCharacter.availablePerks++;
        assertTrue(mCharacter.acquirePerk(perk));
        assertFalse(mCharacter.acquirePerk(perk));
    }
}
