package com.erhodes.fallout;

import com.erhodes.fallout.model.*;
import com.erhodes.fallout.model.Character;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Eric on 06/03/2016.
 */
public class ItemTests {
    public static final String TEST_WEAPON = "test_weapon";
    public static final String TEST_AMMO = "test_ammo";

    @Test
    public void testReload() {
        Weapon testWeapon = new Weapon(TEST_WEAPON, TEST_WEAPON, TEST_WEAPON, 2, 5, TEST_AMMO, 1);
        Action testAttackAction = testWeapon.buildStandardAttackAction(2);
        Action reloadAction = testWeapon.actions.get(0);
        testWeapon.actions.add(testAttackAction);

        Item testAmmo = new Item(TEST_AMMO, TEST_AMMO, TEST_AMMO, Item.TYPE_DEFAULT, 1);
        com.erhodes.fallout.model.Character character = new Character("test_char");
        Character testTarget = new Character("target_char");

        int startingAmmo = testWeapon.getAttribute(Attributes.AMMUNITION_CURRENT);
        assertEquals(startingAmmo, 1);
        character.acquireItem(testWeapon);
        character.acquireItem(testAmmo);

        testAttackAction.getEmptyTargetGroups().get(0).addTarget(testTarget);
        testAttackAction.performAction(character);

        assertEquals(testWeapon.getAttribute(Attributes.AMMUNITION_CURRENT), startingAmmo - 1);
        reloadAction.performAction(character);

        assertEquals(testWeapon.getAttribute(Attributes.AMMUNITION_CURRENT), startingAmmo);
        assertEquals(0, character.hasItem(TEST_AMMO));
    }
}
