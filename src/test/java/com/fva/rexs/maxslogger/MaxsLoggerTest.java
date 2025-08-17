package com.fva.rexs.maxslogger;


import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.xmlunit.assertj3.XmlAssert;
import tech.units.indriya.quantity.NumberQuantity;

import javax.measure.quantity.Pressure;
import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MaxsLoggerTest {

    @Data
    @Builder
    static class MaterialComp implements Part {
        private int rexsId;
        private NumberQuantity<Pressure> elasticModulus;
    }

    @BeforeEach
    void beforeEach() {
        MaxsLogger.reset();
        MaxsLogger.setAppInformation(null, null);
    }

    @Test
    void test_notificationList(@TempDir final Path tempDir) {
        final File expectedMaxFile = new File("src/test/resources/notificationList.maxs");
        final File actualMaxsFile = tempDir.resolve("actual.maxs").toFile();

        MaxsLogger.activateFileLogging(actualMaxsFile);
        final MaterialComp materialComp = MaterialComp.builder().rexsId(5).build();
        MaxsLogger.logMessage(IsoRoutine.ISO21771_2007, materialComp, "ISO21771 plugin", MessageType.INFO);
        MaxsLogger.requireNonNull(IsoRoutine.ISO21771_2007, materialComp, materialComp.getElasticModulus(), "elastic_modulus");

        XmlAssert.assertThat(actualMaxsFile).and(expectedMaxFile).ignoreWhitespace().areIdentical();
    }

    @Test
    void test_setAppInformation() {
        MaxsLogger.setAppInformation("app", "1.0");
        assertEquals("app", MaxsLogger.kernelNotifications.getAppId());
        assertEquals("1.0", MaxsLogger.kernelNotifications.getAppVersion());
    }

    @Test
    void test_reset() {
        MaxsLogger.logMessage(IsoRoutine.ISO21771_2007, "msg", MessageType.INFO);
        assertTrue(MaxsLogger.kernelNotifications.size() > 0);
        MaxsLogger.reset();
        assertEquals(0, MaxsLogger.kernelNotifications.size());
    }
}