package de.fva_net.maxs.logger;


import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.xmlunit.assertj3.XmlAssert;
import tech.units.indriya.quantity.NumberQuantity;
import tech.units.indriya.quantity.Quantities;

import javax.measure.quantity.Pressure;
import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tech.units.indriya.unit.Units.PASCAL;

class MaxsLoggerTest {

    @Data
    @Builder
	static class MaterialComp implements RexsPart {
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

	@Test
	void test_requireNonZero_quantity_null_logsMissingAttribute() {
		MaterialComp materialComp = MaterialComp.builder().rexsId(1).build();
		int initialSize = MaxsLogger.kernelNotifications.size();
		MaxsLogger.requireNonZero(IsoRoutine.ISO21771_2007, materialComp, (NumberQuantity<Pressure>) null, "elastic_modulus");
		assertEquals(initialSize + 1, MaxsLogger.kernelNotifications.size());
	}

	@Test
	void test_requireNonZero_quantity_zero_logsMissingAttribute() {
		MaterialComp materialComp = MaterialComp.builder().rexsId(2).build();
		NumberQuantity<Pressure> zeroQuantity = (NumberQuantity<Pressure>) Quantities.getQuantity(0.0, PASCAL);
		int initialSize = MaxsLogger.kernelNotifications.size();
		MaxsLogger.requireNonZero(IsoRoutine.ISO21771_2007, materialComp, zeroQuantity, "elastic_modulus");
		assertEquals(initialSize + 1, MaxsLogger.kernelNotifications.size());
	}

	@Test
	void test_requireNonZero_quantity_nonZero_doesNotLogMissingAttribute() {
		MaterialComp materialComp = MaterialComp.builder().rexsId(3).build();
		NumberQuantity<Pressure> nonZeroQuantity = (NumberQuantity<Pressure>) Quantities.getQuantity(100.0, PASCAL);
		int initialSize = MaxsLogger.kernelNotifications.size();
		MaxsLogger.requireNonZero(IsoRoutine.ISO21771_2007, materialComp, nonZeroQuantity, "elastic_modulus");
		assertEquals(initialSize, MaxsLogger.kernelNotifications.size());
	}
}
