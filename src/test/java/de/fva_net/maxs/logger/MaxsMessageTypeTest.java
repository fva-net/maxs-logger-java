package de.fva_net.maxs.logger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the MaxsMessageType enum.
 */
class MaxsMessageTypeTest {

	/**
	 * Verifies the isDebug() method for all message types.
	 */
	@Test
	void isDebug() {
		assertFalse(MaxsMessageType.ERROR.isDebug());
		assertFalse(MaxsMessageType.WARNING.isDebug());
		assertFalse(MaxsMessageType.INFO.isDebug());
		assertTrue(MaxsMessageType.DEBUG_ERROR.isDebug());
		assertTrue(MaxsMessageType.DEBUG_WARNING.isDebug());
		assertTrue(MaxsMessageType.DEBUG_INFO.isDebug());
	}

}
