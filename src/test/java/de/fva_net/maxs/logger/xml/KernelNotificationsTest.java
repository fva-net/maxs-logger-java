package de.fva_net.maxs.logger.xml;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Unit tests for the KernelNotifications class.
 */
class KernelNotificationsTest {

	/**
	 * Verifies adding, clearing, getting, and size of notifications.
	 */
    @Test
    void testAddAndClearAndGetAndSize() {
        KernelNotifications kn = new KernelNotifications();
        Notification n = new Notification();
        kn.add(n);
        assertEquals(1, kn.size());
        assertSame(n, kn.get(0));
        kn.clear();
        assertEquals(0, kn.size());
    }

	/**
	 * Verifies setting and getting application ID and version.
	 */
    @Test
    void testSettersAndGetters() {
        KernelNotifications kn = new KernelNotifications();
        kn.setAppId("app");
        kn.setAppVersion("1.0");
        assertEquals("app", kn.getAppId());
        assertEquals("1.0", kn.getAppVersion());
    }
}
