package com.fva.rexs.maxslogger.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.util.Precision;

import javax.measure.Quantity;

/**
 * Utility class for {@link Quantity}
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MathUtility {


    /**
     * Relative precision used for comparing values.
     */
    public static final double RELATIVE_PRECISION = 1e-7;

    /**
     * Compares two double values for similarity, which is given if the relative difference is less or equal than the {@link #RELATIVE_PRECISION}.
     *
     * @param d1
     *         The first double value to compare.
     * @param d2
     *         The second double value to compare.
     *
     * @return {@code true} if the values are similar; {@code false} if they are not similar.
     */
    public static boolean isSimilar(final double d1, final double d2)
    {
        if (Double.isNaN(d1) && Double.isNaN(d2)) {
            // Both values are NaN, so they are considered equal.
            return true;
        }
        return Precision.equalsWithRelativeTolerance(d1, d2, RELATIVE_PRECISION);
    }
}

