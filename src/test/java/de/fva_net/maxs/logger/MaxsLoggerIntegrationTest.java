package de.fva_net.maxs.logger;

import info.rexs.model.RexsComponent;
import info.rexs.model.RexsModelObjectFactory;
import info.rexs.schema.constants.standard.RexsStandardComponentTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class MaxsLoggerIntegrationTest {

    // Mock enum for attribute testing
    enum TestEnum {
        UNKNOWN,
        VALID
    }

    @BeforeEach
    void beforeEach() {
        MaxsLogger.reset();
        MaxsLogger.setAppInformation(null, null);
    }

    @Test
    void activateFileLogging_nullPath_logsError() {
        MaxsLogger.activateFileLogging(null);
        assertFalse(MaxsLogger.isLoggingToFileActivated());
    }

    @Test
    void activateFileLogging_invalidExtension_logsError(@TempDir Path tempDir) {
        File invalidFile = new File(tempDir.toFile(), "logfile.txt");
        MaxsLogger.activateFileLogging(invalidFile);
        assertFalse(MaxsLogger.isLoggingToFileActivated());
    }

    @Test
    void activateFileLogging_validPath_setsLogPath(@TempDir Path tempDir) {
        File validFile = new File(tempDir.toFile(), "logfile.maxs");
        MaxsLogger.activateFileLogging(validFile);
        assertTrue(MaxsLogger.isLoggingToFileActivated());
    }

    @Test
    void deactivateFileLogging_deactivatesLogging(@TempDir Path tempDir) {
        File validFile = new File(tempDir.toFile(), "logfile.maxs");
        MaxsLogger.activateFileLogging(validFile);
        assertTrue(MaxsLogger.isLoggingToFileActivated());
        MaxsLogger.deactivateFileLogging();
        assertFalse(MaxsLogger.isLoggingToFileActivated());
    }

    @Test
    void logMessage_withPart_logsMessage() {
		MaxsLogger.logMessage(IsoRoutine.ISO21771_2007, 42, "Test message", MaxsMessageType.INFO);
		assertEquals(1, MaxsLogger.getAllNotifications().size());
		assertEquals(42, MaxsLogger.getAllNotifications().get(0).getCompId());
		assertEquals("Test message", MaxsLogger.getAllNotifications().get(0).getMessage());
		assertEquals(MaxsMessageType.INFO, MaxsLogger.getAllNotifications().get(0).getType());
		assertEquals("iso21771_2007", MaxsLogger.getAllNotifications().get(0).getRoutine());
    }

    @Test
    void logMessage_withString_logsMessage() {
		MaxsLogger.logMessage(IsoRoutine.ISO6336_2019, "String message", MaxsMessageType.ERROR);
		assertEquals(1, MaxsLogger.getAllNotifications().size());
		assertEquals("String message", MaxsLogger.getAllNotifications().get(0).getMessage());
		assertEquals(MaxsMessageType.ERROR, MaxsLogger.getAllNotifications().get(0).getType());
		assertEquals("iso6336_2019", MaxsLogger.getAllNotifications().get(0).getRoutine());
    }

    @Test
    void requireNonNull_doubleValueIsNaN_logsMissingAttribute() {
		MaxsLogger.requireNonNull(IsoRoutine.ISO21771_2007, 1, Double.NaN, "attr");
		assertEquals(1, MaxsLogger.getAllNotifications().size());
		assertEquals(MaxsMessageType.DEBUG_ERROR, MaxsLogger.getAllNotifications().get(0).getType());
    }

	@Test
	void requireNonNull_objectDoubleValueIsNaN_logsMissingAttribute() {
		MaxsLogger.requireNonNull(IsoRoutine.ISO21771_2007, 1, Double.valueOf(Double.NaN), "attr");
		assertEquals(1, MaxsLogger.getAllNotifications().size());
		assertEquals(MaxsMessageType.DEBUG_ERROR, MaxsLogger.getAllNotifications().get(0).getType());
	}

    @Test
    void requireNonNull_doubleValueIsNotNaN_doesNotLog() {
		MaxsLogger.requireNonNull(IsoRoutine.ISO21771_2007, 1, 1.23, "attr");
		assertEquals(0, MaxsLogger.getAllNotifications().size());
    }

	@Test
	void requireNonNull_objectDoubleValueIsNotNaN_doesNotLog() {
		MaxsLogger.requireNonNull(IsoRoutine.ISO21771_2007, 1, Double.valueOf(1.0), "attr");
		assertEquals(0, MaxsLogger.getAllNotifications().size());
	}

    @Test
    void requireNonNull_enumValueIsNull_logsMissingAttribute() {
		MaxsLogger.requireNonNull(IsoRoutine.ISO6336_2019, 2, (TestEnum) null, "enumAttr");
		assertEquals(1, MaxsLogger.getAllNotifications().size());
		assertEquals(MaxsMessageType.DEBUG_ERROR, MaxsLogger.getAllNotifications().get(0).getType());
    }

    @Test
    void requireNonNull_enumValueIsUnknown_logsMissingAttribute() {
		MaxsLogger.requireNonNull(IsoRoutine.ISO6336_2019, 2, TestEnum.UNKNOWN, "enumAttr");
		assertEquals(1, MaxsLogger.getAllNotifications().size());
		assertEquals(MaxsMessageType.DEBUG_ERROR, MaxsLogger.getAllNotifications().get(0).getType());
    }

    @Test
    void requireNonNull_enumValueIsValid_doesNotLog() {
		MaxsLogger.requireNonNull(IsoRoutine.ISO6336_2019, 2, TestEnum.VALID, "enumAttr");
		assertEquals(0, MaxsLogger.getAllNotifications().size());
    }

    @Test
    void requireNonZero_doubleValueIsNaN_logsMissingAttribute() {
		MaxsLogger.requireNonZero(IsoRoutine.ISO21771_2007, 3, Double.NaN, "attr");
		assertEquals(1, MaxsLogger.getAllNotifications().size());
		assertEquals(MaxsMessageType.DEBUG_ERROR, MaxsLogger.getAllNotifications().get(0).getType());
    }

    @Test
    void requireNonZero_doubleValueIsZero_logsMissingAttribute() {
		MaxsLogger.requireNonZero(IsoRoutine.ISO21771_2007, 3, 0.0, "attr");
		assertEquals(1, MaxsLogger.getAllNotifications().size());
		assertEquals(MaxsMessageType.DEBUG_ERROR, MaxsLogger.getAllNotifications().get(0).getType());
    }

    @Test
    void requireNonZero_doubleValueIsNonZero_doesNotLog() {
		MaxsLogger.requireNonZero(IsoRoutine.ISO21771_2007, 3, 2.0, "attr");
		assertEquals(0, MaxsLogger.getAllNotifications().size());
    }

    @Test
    void requireNonZero_intValueIsZero_logsMissingAttribute() {
		MaxsLogger.requireNonZero(IsoRoutine.ISO6336_2019, 4, 0, "intAttr");
		assertEquals(1, MaxsLogger.getAllNotifications().size());
		assertEquals(MaxsMessageType.DEBUG_ERROR, MaxsLogger.getAllNotifications().get(0).getType());
    }

    @Test
    void requireNonZero_intValueIsNonZero_doesNotLog() {
		MaxsLogger.requireNonZero(IsoRoutine.ISO6336_2019, 4, 5, "intAttr");
		assertEquals(0, MaxsLogger.getAllNotifications().size());
    }

    @Test
    void reset_clearsNotificationsAndDeactivatesLogging(@TempDir Path tempDir) {
        File validFile = new File(tempDir.toFile(), "logfile.maxs");
        MaxsLogger.activateFileLogging(validFile);
		MaxsLogger.logMessage(IsoRoutine.ISO21771_2007, 5, "msg", MaxsMessageType.INFO);
        assertTrue(MaxsLogger.isLoggingToFileActivated());
		assertEquals(1, MaxsLogger.getAllNotifications().size());
        MaxsLogger.reset();
        assertFalse(MaxsLogger.isLoggingToFileActivated());
		assertEquals(0, MaxsLogger.getAllNotifications().size());
    }

    @Test
    void setAppInformation_setsAppIdAndVersion() {
        MaxsLogger.setAppInformation("appId", "1.2.3");
		assertEquals("appId", MaxsLogger.getAppId());
		assertEquals("1.2.3", MaxsLogger.getAppVersion());
    }

    private static final RexsComponent rexsGear = RexsModelObjectFactory.getInstance()
		.createRexsComponent(12, RexsStandardComponentTypes.cylindrical_gear, "mygear");

	@Test
    void logMessage_withRexsComponent() {
		MaxsLogger.logMessage(IsoRoutine.ISO21771_2007, rexsGear, "Info message", MaxsMessageType.INFO);
		assertEquals(1, MaxsLogger.getAllNotifications().size());
		assertEquals(12, MaxsLogger.getAllNotifications().get(0).getCompId());
		assertEquals("Info message", MaxsLogger.getAllNotifications().get(0).getMessage());
		assertEquals(MaxsMessageType.INFO, MaxsLogger.getAllNotifications().get(0).getType());
		assertEquals("iso21771_2007", MaxsLogger.getAllNotifications().get(0).getRoutine());
		assertTrue(MaxsLogger.getAllNotifications().get(0).getMessage().contains("Info"));
    }
}
