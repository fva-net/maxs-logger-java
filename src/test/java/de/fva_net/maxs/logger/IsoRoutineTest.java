package de.fva_net.maxs.logger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the IsoRoutine enum.
 */
class IsoRoutineTest {

	/**
	 * Verifies that the correct routine identifiers are returned.
	 */
    @Test
    void testGetId() {
		assertEquals("iso21771_2007", IsoRoutine.ISO21771_2007.getMaxsId());
		assertEquals("iso6336_2019", IsoRoutine.ISO6336_2019.getMaxsId());
    }
}
