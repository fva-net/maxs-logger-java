package com.fva.rexs.maxslogger;


import java.io.File;
import java.nio.file.Path;

import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.xmlunit.assertj3.XmlAssert;
import tech.units.indriya.quantity.NumberQuantity;

import javax.measure.quantity.Pressure;

class MaxsLoggerTest {

    @Data
    @Builder
    static class MaterialComp implements Part{
        private int rexsId;
        private NumberQuantity<Pressure> elasticModulus;
    }

    @BeforeEach
    void beforeEach()
    {
        MaxsLogger.reset();
        MaxsLogger.setAppInformation(null, null);
    }

    @Test
    void test_notificationList(@TempDir final Path tempDir)
    {

        // Given
        final File expectedMaxFile = new File("src/test/resources/notificationList.maxs");
        final File actualMaxsFile = tempDir.resolve("actual.maxs").toFile();

        // When
        MaxsLogger.activateFileLogging(actualMaxsFile);
        final MaterialComp part5 = MaterialComp.builder().rexsId(5).build();
        MaxsLogger.logMessage(Routine.TR06, part5, "TR06 plugin", MessageType.INFO);
        MaxsLogger.requireNonNull(Routine.TR06, part5, part5.getElasticModulus(), "elastic_modulus");

        // Then
        XmlAssert.assertThat(actualMaxsFile).and(expectedMaxFile).ignoreWhitespace().areIdentical();
    }
}
