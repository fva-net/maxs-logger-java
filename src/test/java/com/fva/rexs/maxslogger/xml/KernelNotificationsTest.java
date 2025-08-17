package com.fva.rexs.maxslogger.xml;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class KernelNotificationsTest {

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

    @Test
    void testSettersAndGetters() {
        KernelNotifications kn = new KernelNotifications();
        kn.setAppId("app");
        kn.setAppVersion("1.0");
        assertEquals("app", kn.getAppId());
        assertEquals("1.0", kn.getAppVersion());
    }
}