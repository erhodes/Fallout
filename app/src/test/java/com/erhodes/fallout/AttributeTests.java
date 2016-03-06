package com.erhodes.fallout;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;

/**
 * Created by Eric on 05/03/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class AttributeTests {
    public static final String BASE_ATTRIBUTE = "base_attribute";
    public static final String DERIVED_ATTRIBUTE = "derived_attribute";
    public static final String CAPACITY_ATTRIBUTE = "capacity_attribute";

    @Test
    public void basicAttributeTests() {
        Attribute attribute = new Attribute(BASE_ATTRIBUTE, BASE_ATTRIBUTE, 5);
        assert(attribute.getFinalValue() == 5);
        attribute.addModifier(4);
        attribute.calculateFinalValue();
        assertEquals(attribute.getFinalValue(), 9);
    }

    @Test
    public void testDerivedAttributes() {
        Attribute baseAttribute = new Attribute(BASE_ATTRIBUTE, BASE_ATTRIBUTE, 5);
        DerivedAttribute derivedAttribute = new DerivedAttribute(DERIVED_ATTRIBUTE, BASE_ATTRIBUTE, baseAttribute, 2, 5);
        assertEquals(derivedAttribute.getFinalValue(), baseAttribute.getFinalValue() * 2 + 5);
    }

    @Test
    public void advancedDerivedAttributeTest() {
        Attribute baseAttribute = new Attribute(BASE_ATTRIBUTE, BASE_ATTRIBUTE, 5);
        DerivedAttribute derivedAttribute = new DerivedAttribute(DERIVED_ATTRIBUTE, BASE_ATTRIBUTE, baseAttribute, 2, 5);

        baseAttribute.addModifier(2);

        derivedAttribute.addModifier(3);
        assertEquals(derivedAttribute.getFinalValue(), baseAttribute.getFinalValue() * 2 + 8);
    }

    @Test
    public void testCapacityAttributes() {
        Attribute attribute = new Attribute(BASE_ATTRIBUTE, BASE_ATTRIBUTE, 5);
        CapacityAttribute capacityAttribute = new CapacityAttribute(CAPACITY_ATTRIBUTE, attribute);

        int startingValue = capacityAttribute.getFinalValue();
        assertEquals(capacityAttribute.getMaxValue(), attribute.getFinalValue());
        assertEquals(capacityAttribute.getFinalValue(), capacityAttribute.getMaxValue());

        attribute.addModifier(5);
        assertEquals(capacityAttribute.getMaxValue(), attribute.getFinalValue());
        assertEquals(capacityAttribute.getFinalValue(), startingValue);

        capacityAttribute.resetToMax();
        assertEquals(capacityAttribute.getFinalValue(), capacityAttribute.getMaxValue());

        attribute.addModifier(-8);
        assertTrue(capacityAttribute.getFinalValue() > capacityAttribute.getMaxValue());
        capacityAttribute.calculateFinalValue();
        assertEquals(capacityAttribute.getFinalValue(), capacityAttribute.getMaxValue());
    }
}
