package com.fva.rexs.maxslogger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotificationTest {

    @Test
    void testSetAndGetRoutine() {
        Notification notification = new Notification();
        notification.setRoutine(IsoRoutine.ISO21771_2007);
        assertEquals("iso21771_2007", notification.getRoutine());
        notification.setRoutine(null);
        assertNull(notification.getRoutine());
    }

    @Test
    void testGetDataInitializesList() {
        Notification notification = new Notification();
        assertNotNull(notification.getData());
        assertTrue(notification.getData().isEmpty());
    }
}