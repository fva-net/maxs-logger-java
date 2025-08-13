package com.fva.rexs.maxslogger.utils;

import java.util.Objects;
import javax.measure.Quantity;
import javax.measure.Unit;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.util.Precision;
import tech.units.indriya.quantity.NumberQuantity;
import tech.units.indriya.quantity.Quantities;

/**
 * Utility class for {@link Quantity}
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MathUtility {

    /**
     * Threshold limit for positive and negative infinity
     */
    public static final double THRESHOLD_LIMIT = 1.3407807929942596E154;

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

    /**
     * @param value
     *         the value of quantity
     * @param unit
     *         the unit of measure
     * @param <Q>
     *         the type of quantity
     *
     * @return the quantity
     */
    public static <Q extends Quantity<Q>> NumberQuantity<Q> getQuantity(final double value, final Unit<Q> unit)
    {
        if (Objects.isNull(unit) || Double.isNaN(value)) {
            return null;
        }
        else if (Double.isInfinite(value) && value > 0) {
            return (NumberQuantity<Q>) Quantities.getQuantity(THRESHOLD_LIMIT, unit);
        }
        else if (Double.isInfinite(value) && value < 0) {
            return (NumberQuantity<Q>) Quantities.getQuantity(-THRESHOLD_LIMIT, unit);
        }
        else {
            return (NumberQuantity<Q>) Quantities.getQuantity(value, unit);
        }
    }
}

