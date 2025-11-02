package de.fva_net.maxs.logger.xml;

import de.fva_net.maxs.logger.IsoRoutine;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Notification class.
 */
class NotificationTest {

	/**
	 * Verifies that setting and getting the routine works as expected.
	 */
    @Test
    void testSetAndGetRoutine() {
        Notification notification = new Notification();
        notification.setRoutine(IsoRoutine.ISO21771_2007);
        assertEquals("iso21771_2007", notification.getRoutine());
        notification.setRoutine(null);
        assertNull(notification.getRoutine());
    }

	/**
	 * Verifies that getData initializes the list if it is null.
	 */
    @Test
    void testGetDataInitializesList() {
        Notification notification = new Notification();
        assertNotNull(notification.getData());
        assertTrue(notification.getData().isEmpty());
    }
}
