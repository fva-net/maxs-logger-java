package io.github.fvanet.maxsloggerjava;

import info.rexs.model.RexsComponent;
import info.rexs.model.RexsModelObjectFactory;
import info.rexs.schema.constants.standard.RexsStandardComponentTypes;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
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

    @Setter
    @Data
    @Builder
	static class TestRexsPart implements RexsPart {
        private int rexsId;
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
		TestRexsPart part = TestRexsPart.builder().rexsId(42).build();
        MaxsLogger.logMessage(IsoRoutine.ISO21771_2007, part, "Test message", MessageType.INFO);
        assertEquals(1, MaxsLogger.kernelNotifications.size());
        assertEquals(42, MaxsLogger.kernelNotifications.get(0).getCompId());
        assertEquals("Test message", MaxsLogger.kernelNotifications.get(0).getMessage());
        assertEquals(MessageType.INFO, MaxsLogger.kernelNotifications.get(0).getType());
        assertEquals("iso21771_2007", MaxsLogger.kernelNotifications.get(0).getRoutine());
    }

    @Test
    void logMessage_withString_logsMessage() {
        MaxsLogger.logMessage(IsoRoutine.ISO6336_2019, "String message", MessageType.ERROR);
        assertEquals(1, MaxsLogger.kernelNotifications.size());
        assertEquals("String message", MaxsLogger.kernelNotifications.get(0).getMessage());
        assertEquals(MessageType.ERROR, MaxsLogger.kernelNotifications.get(0).getType());
        assertEquals("iso6336_2019", MaxsLogger.kernelNotifications.get(0).getRoutine());
    }

    @Test
    void requireNonNull_doubleValueIsNaN_logsMissingAttribute() {
		TestRexsPart part = TestRexsPart.builder().rexsId(1).build();
        MaxsLogger.requireNonNull(IsoRoutine.ISO21771_2007, part, Double.NaN, "attr");
        assertEquals(1, MaxsLogger.kernelNotifications.size());
        assertEquals(MessageType.DEBUG_ERROR, MaxsLogger.kernelNotifications.get(0).getType());
    }

    @Test
    void requireNonNull_doubleValueIsNotNaN_doesNotLog() {
		TestRexsPart part = TestRexsPart.builder().rexsId(1).build();
        MaxsLogger.requireNonNull(IsoRoutine.ISO21771_2007, part, 1.23, "attr");
        assertEquals(0, MaxsLogger.kernelNotifications.size());
    }

    @Test
    void requireNonNull_enumValueIsNull_logsMissingAttribute() {
		TestRexsPart part = TestRexsPart.builder().rexsId(2).build();
        MaxsLogger.requireNonNull(IsoRoutine.ISO6336_2019, part, (TestEnum) null, "enumAttr");
        assertEquals(1, MaxsLogger.kernelNotifications.size());
        assertEquals(MessageType.DEBUG_ERROR, MaxsLogger.kernelNotifications.get(0).getType());
    }

    @Test
    void requireNonNull_enumValueIsUnknown_logsMissingAttribute() {
		TestRexsPart part = TestRexsPart.builder().rexsId(2).build();
        MaxsLogger.requireNonNull(IsoRoutine.ISO6336_2019, part, TestEnum.UNKNOWN, "enumAttr");
        assertEquals(1, MaxsLogger.kernelNotifications.size());
        assertEquals(MessageType.DEBUG_ERROR, MaxsLogger.kernelNotifications.get(0).getType());
    }

    @Test
    void requireNonNull_enumValueIsValid_doesNotLog() {
		TestRexsPart part = TestRexsPart.builder().rexsId(2).build();
        MaxsLogger.requireNonNull(IsoRoutine.ISO6336_2019, part, TestEnum.VALID, "enumAttr");
        assertEquals(0, MaxsLogger.kernelNotifications.size());
    }

    @Test
    void requireNonZero_doubleValueIsNaN_logsMissingAttribute() {
		TestRexsPart part = TestRexsPart.builder().rexsId(3).build();
        MaxsLogger.requireNonZero(IsoRoutine.ISO21771_2007, part, Double.NaN, "attr");
        assertEquals(1, MaxsLogger.kernelNotifications.size());
        assertEquals(MessageType.DEBUG_ERROR, MaxsLogger.kernelNotifications.get(0).getType());
    }

    @Test
    void requireNonZero_doubleValueIsZero_logsMissingAttribute() {
		TestRexsPart part = TestRexsPart.builder().rexsId(3).build();
        MaxsLogger.requireNonZero(IsoRoutine.ISO21771_2007, part, 0.0, "attr");
        assertEquals(1, MaxsLogger.kernelNotifications.size());
        assertEquals(MessageType.DEBUG_ERROR, MaxsLogger.kernelNotifications.get(0).getType());
    }

    @Test
    void requireNonZero_doubleValueIsNonZero_doesNotLog() {
		TestRexsPart part = TestRexsPart.builder().rexsId(3).build();
        MaxsLogger.requireNonZero(IsoRoutine.ISO21771_2007, part, 2.0, "attr");
        assertEquals(0, MaxsLogger.kernelNotifications.size());
    }

    @Test
    void requireNonZero_intValueIsZero_logsMissingAttribute() {
		TestRexsPart part = TestRexsPart.builder().rexsId(4).build();
        MaxsLogger.requireNonZero(IsoRoutine.ISO6336_2019, part, 0, "intAttr");
        assertEquals(1, MaxsLogger.kernelNotifications.size());
        assertEquals(MessageType.DEBUG_ERROR, MaxsLogger.kernelNotifications.get(0).getType());
    }

    @Test
    void requireNonZero_intValueIsNonZero_doesNotLog() {
		TestRexsPart part = TestRexsPart.builder().rexsId(4).build();
        MaxsLogger.requireNonZero(IsoRoutine.ISO6336_2019, part, 5, "intAttr");
        assertEquals(0, MaxsLogger.kernelNotifications.size());
    }

    @Test
    void reset_clearsNotificationsAndDeactivatesLogging(@TempDir Path tempDir) {
        File validFile = new File(tempDir.toFile(), "logfile.maxs");
        MaxsLogger.activateFileLogging(validFile);
		TestRexsPart part = TestRexsPart.builder().rexsId(5).build();
        MaxsLogger.logMessage(IsoRoutine.ISO21771_2007, part, "msg", MessageType.INFO);
        assertTrue(MaxsLogger.isLoggingToFileActivated());
        assertEquals(1, MaxsLogger.kernelNotifications.size());
        MaxsLogger.reset();
        assertFalse(MaxsLogger.isLoggingToFileActivated());
        assertEquals(0, MaxsLogger.kernelNotifications.size());
    }

    @Test
    void setAppInformation_setsAppIdAndVersion() {
        MaxsLogger.setAppInformation("appId", "1.2.3");
        assertEquals("appId", MaxsLogger.kernelNotifications.getAppId());
        assertEquals("1.2.3", MaxsLogger.kernelNotifications.getAppVersion());
    }

    private static final RexsComponent rexsGear = RexsModelObjectFactory.getInstance()
            .createRexsComponent(12, RexsStandardComponentTypes.cylindrical_gear, "mygear");

    @Test
    void logMessage_withRexsComponent() {
        MaxsLogger.logMessage(IsoRoutine.ISO21771_2007, rexsGear, "Info message", MessageType.INFO);
        assertEquals(1, MaxsLogger.kernelNotifications.size());
        assertEquals(12, MaxsLogger.kernelNotifications.get(0).getCompId());
        assertEquals("Info message", MaxsLogger.kernelNotifications.get(0).getMessage());
        assertEquals(MessageType.INFO, MaxsLogger.kernelNotifications.get(0).getType());
        assertEquals("iso21771_2007", MaxsLogger.kernelNotifications.get(0).getRoutine());
        assertTrue(MaxsLogger.kernelNotifications.get(0).getMessage().contains("Info"));
    }
}
