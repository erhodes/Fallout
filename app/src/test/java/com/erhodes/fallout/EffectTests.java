package com.erhodes.fallout;

import com.erhodes.fallout.model.Attributes;
import com.erhodes.fallout.model.Character;
import com.erhodes.fallout.model.Effect;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Eric on 14/05/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class EffectTests {
    private static final String TEST_CHAR_NAME = "Test Char";
    Character mCharacter;

    @Before
    public void setup() {
        mCharacter = new Character(TEST_CHAR_NAME);
    }

    @Test
    public void testEffect() {
        int effectMagnitude = 2;
        Effect effect = new Effect(Attributes.STRENGTH, effectMagnitude);
        int baseStrength = mCharacter.getAttributeValue(Attributes.STRENGTH);
        mCharacter.applyEffect(effect);
        assertEquals(baseStrength+effectMagnitude, mCharacter.getAttributeValue(Attributes.STRENGTH));
    }

    @Test
    public void testDurationEffect() {
        int effectMagnitude = 2;
        int baseStrength = mCharacter.getAttributeValue(Attributes.STRENGTH);
        Effect effect = new Effect(Attributes.STRENGTH, effectMagnitude, 1);
        mCharacter.applyEffect(effect);
        assertEquals(baseStrength + effectMagnitude, mCharacter.getAttributeValue(Attributes.STRENGTH));

        mCharacter.newTurn();
        assertEquals(baseStrength, mCharacter.getAttributeValue(Attributes.STRENGTH));
    }

    @Test
    public void testRecurringEffect() {
        int baseStrength = mCharacter.getAttributeValue(Attributes.STRENGTH);
        int effectMagnitude = 1;
        int expectedStrength = baseStrength;
        Effect recurringEffect = new Effect(Attributes.STRENGTH, effectMagnitude, 2, true);
        mCharacter.applyEffect(recurringEffect);
        expectedStrength+=effectMagnitude;
        assertEquals(mCharacter.getAttributeValue(Attributes.STRENGTH), expectedStrength);

        mCharacter.newTurn();
        expectedStrength += effectMagnitude;
        assertEquals(mCharacter.getAttributeValue(Attributes.STRENGTH), expectedStrength);

        mCharacter.newTurn();
        expectedStrength += effectMagnitude;
        assertEquals(mCharacter.getAttributeValue(Attributes.STRENGTH), expectedStrength);

        mCharacter.newTurn();
        assertEquals(mCharacter.getAttributeValue(Attributes.STRENGTH), expectedStrength);
    }
}
