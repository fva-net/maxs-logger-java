package com.fva.rexs.maxslogger.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.units.indriya.quantity.Quantities;

import javax.measure.Unit;
import javax.measure.quantity.Length;

import static javax.measure.MetricPrefix.MICRO;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tech.units.indriya.unit.Units.METRE;

class MathUtilityTest {

    public static final Unit<Length> MICRONS = MICRO(METRE);
    @Test
    void testIsSimilar_double()
    {
        // real values
        assertTrue(MathUtility.isSimilar(10.0, 10.00000001), "d1 and d2 to be equal");
        assertFalse(MathUtility.isSimilar(5.0, 5.0001), "d1 and d2 to not be equal");
        assertTrue(MathUtility.isSimilar(0.0, 0.0), "d1 and d2 to be equal");

        // NaN values
        assertTrue(MathUtility.isSimilar(Double.NaN, Double.NaN), "Equality check should pass for NaN values");
        assertFalse(MathUtility.isSimilar(Double.NaN, 0.0), "Equality check should fail if one value is NaN");
        assertFalse(MathUtility.isSimilar(-10.0, Double.NaN), "Equality check should fail if one value is NaN");

        // Infinite values
        assertTrue(MathUtility.isSimilar(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), "Equality check should pass for infinite values");
        assertTrue(MathUtility.isSimilar(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY), "Equality check should pass for infinite values");
        assertFalse(MathUtility.isSimilar(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY), "Equality check should fail for infinite values");

        // Mix of infinite and NaN values
        assertFalse(MathUtility.isSimilar(Double.POSITIVE_INFINITY, Double.NaN), "Equality check should fail for infinite and NaN values");
        assertFalse(MathUtility.isSimilar(Double.NEGATIVE_INFINITY, Double.NaN), "Equality check should fail for infinite and NaN values");
        assertFalse(MathUtility.isSimilar(Double.NaN, Double.POSITIVE_INFINITY), "Equality check should fail for infinite and NaN values");
        assertFalse(MathUtility.isSimilar(Double.NaN, Double.NEGATIVE_INFINITY), "Equality check should fail for infinite and NaN values");

        // Mix of infinite and real values
        assertFalse(MathUtility.isSimilar(Double.POSITIVE_INFINITY, 10.0), "Equality check should fail for infinite and real values");
        assertFalse(MathUtility.isSimilar(Double.NEGATIVE_INFINITY, 10.0), "Equality check should fail for infinite and real values");
        assertFalse(MathUtility.isSimilar(10.0, Double.POSITIVE_INFINITY), "Equality check should fail for infinite and real values");
        assertFalse(MathUtility.isSimilar(10.0, Double.NEGATIVE_INFINITY), "Equality check should fail for infinite and real values");
    }

    @Test
    void testGetQuantity()
    {
        // null/NaN checks
        Assertions.assertNull(MathUtility.getQuantity(Double.NaN, MICRONS));
        Assertions.assertNull(MathUtility.getQuantity(1.2, null));

        // checks with values
        Assertions.assertEquals(Quantities.getQuantity(1.2, MICRONS), MathUtility.getQuantity(1.2, MICRONS));
        Assertions.assertEquals(Quantities.getQuantity(MathUtility.THRESHOLD_LIMIT, MICRONS),
                MathUtility.getQuantity(Double.POSITIVE_INFINITY, MICRONS));
        Assertions.assertEquals(Quantities.getQuantity(-MathUtility.THRESHOLD_LIMIT, MICRONS),
                MathUtility.getQuantity(Double.NEGATIVE_INFINITY, MICRONS));
    }
}
