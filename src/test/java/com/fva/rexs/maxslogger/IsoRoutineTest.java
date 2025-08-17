package com.fva.rexs.maxslogger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IsoRoutineTest {

    @Test
    void testGetId() {
        assertEquals("iso21771_2007", IsoRoutine.ISO21771_2007.getId());
        assertEquals("iso6336_2019", IsoRoutine.ISO6336_2019.getId());
    }
}