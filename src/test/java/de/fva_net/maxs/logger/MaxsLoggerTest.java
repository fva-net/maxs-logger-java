package de.fva_net.maxs.logger;


import de.fva_net.maxs.logger.xml.Notification;
import lombok.Builder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.xmlunit.assertj3.XmlAssert;
import tech.units.indriya.quantity.NumberQuantity;
import tech.units.indriya.quantity.Quantities;

import javax.measure.quantity.Pressure;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static tech.units.indriya.unit.Units.PASCAL;

class MaxsLoggerTest {

	@Builder
	record MaterialComp(int rexsId, NumberQuantity<Pressure> elasticModulus) {
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
		MaxsLogger.logMessage(IsoRoutine.ISO21771_2007, materialComp.rexsId, "ISO21771 plugin", MaxsMessageType.INFO);
		MaxsLogger.requireNonNull(IsoRoutine.ISO21771_2007, materialComp.rexsId, materialComp.elasticModulus(), "elastic_modulus");

        XmlAssert.assertThat(actualMaxsFile).and(expectedMaxFile).ignoreWhitespace().areIdentical();
    }

    @Test
    void test_setAppInformation() {
        MaxsLogger.setAppInformation("app", "1.0");
		assertEquals("app", MaxsLogger.getAppId());
		assertEquals("1.0", MaxsLogger.getAppVersion());
    }

    @Test
    void test_reset() {
		MaxsLogger.logMessage(IsoRoutine.ISO21771_2007, "msg", MaxsMessageType.INFO);
		assertFalse(MaxsLogger.getAllNotifications().isEmpty());
        MaxsLogger.reset();
		assertEquals(0, MaxsLogger.getAllNotifications().size());
    }

	@Test
	void test_requireNonZero_quantity_null_logsMissingAttribute() {
		MaterialComp materialComp = MaterialComp.builder().rexsId(1).build();
		int initialSize = MaxsLogger.getAllNotifications().size();
		MaxsLogger.requireNonZero(IsoRoutine.ISO21771_2007, materialComp.rexsId, (NumberQuantity<Pressure>) null, "elastic_modulus");
		assertEquals(initialSize + 1, MaxsLogger.getAllNotifications().size());
	}

	@Test
	void test_requireNonZero_quantity_zero_logsMissingAttribute() {
		MaterialComp materialComp = MaterialComp.builder().rexsId(2).build();
		NumberQuantity<Pressure> zeroQuantity = (NumberQuantity<Pressure>) Quantities.getQuantity(0.0, PASCAL);
		int initialSize = MaxsLogger.getAllNotifications().size();
		MaxsLogger.requireNonZero(IsoRoutine.ISO21771_2007, materialComp.rexsId, zeroQuantity, "elastic_modulus");
		assertEquals(initialSize + 1, MaxsLogger.getAllNotifications().size());
	}

	@Test
	void test_requireNonZero_quantity_nonZero_doesNotLogMissingAttribute() {
		MaterialComp materialComp = MaterialComp.builder().rexsId(3).build();
		NumberQuantity<Pressure> nonZeroQuantity = (NumberQuantity<Pressure>) Quantities.getQuantity(100.0, PASCAL);
		int initialSize = MaxsLogger.getAllNotifications().size();
		MaxsLogger.requireNonZero(IsoRoutine.ISO21771_2007, materialComp.rexsId, nonZeroQuantity, "elastic_modulus");
		assertEquals(initialSize, MaxsLogger.getAllNotifications().size());
	}

	@Test
	void test_getFilteredNotifications_byRoutineAndId() {
		MaxsLogger.logMessage(IsoRoutine.ISO21771_2007, 1, "msg1", MaxsMessageType.INFO);
		MaxsLogger.logMessage(IsoRoutine.ISO6336_2019, 1, "msg2", MaxsMessageType.INFO);
		MaxsLogger.logMessage(IsoRoutine.ISO21771_2007, 2, "msg3", MaxsMessageType.INFO);

		Predicate<Notification> filter = n -> n.getRoutine().equals(IsoRoutine.ISO21771_2007.getMaxsId()) && n.getCompId() == 1;
		List<Notification> filteredNotifications = MaxsLogger.getFilteredNotifications(filter);
		assertEquals(1, filteredNotifications.size());
	}
}
