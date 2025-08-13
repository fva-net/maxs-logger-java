package com.rexs.maxslogger;

import info.rexs.model.RexsComponent;
import info.rexs.model.RexsModelObjectFactory;
import info.rexs.schema.constants.standard.RexsStandardComponentTypes;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tech.units.indriya.quantity.NumberQuantity;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Length;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;


import static com.rexs.maxslogger.utils.MathUtility.getQuantity;
import static javax.measure.MetricPrefix.MILLI;
import static org.junit.jupiter.api.Assertions.*;
import static tech.units.indriya.unit.Units.METRE;


class MaxsLogger_UnitTest {

    public static final Unit<Length> MILLIMETER = MILLI(METRE);

    // Mock enum for LubricationType
    enum TestEnum {
        UNKNOWN,
        GREASE
    }

    // Mock class for CylGearPairComp
    @Data
    @Builder
    static class RexsComp implements Part {
        @Setter
        private int rexsId;
        private NumberQuantity<Length> centerDistance;
    }

    private static final RexsComponent rexsGear = RexsModelObjectFactory.getInstance()
            .createRexsComponent(12, RexsStandardComponentTypes.cylindrical_gear, "mygear");

    @Test
    void activateFileLogging_fileCreationFails_logsError(@TempDir final Path tempDir)
    {
        final File invalidFile = new File(tempDir.toFile(), "invalid\0file.maxs");
        MaxsLogger.activateFileLogging(invalidFile);
        assertFalse(MaxsLogger.isLoggingToFileActivated());
    }

    @Test
    void activateFileLogging_invalidExtension_logsError(@TempDir final Path tempDir)
    {
        final File invalidFile = new File(tempDir.toFile(), "logfile.txt");
        MaxsLogger.activateFileLogging(invalidFile);
        assertFalse(MaxsLogger.isLoggingToFileActivated());
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void activateFileLogging_nonWritablePath_logsError(@TempDir final Path tempDir) throws IOException
    {
        final File nonWritableFile = new File(tempDir.toFile(), "temp.maxs");
        nonWritableFile.createNewFile();
        nonWritableFile.setWritable(false);
        MaxsLogger.activateFileLogging(nonWritableFile);
        assertFalse(MaxsLogger.isLoggingToFileActivated());
    }

    @Test
    void activateFileLogging_nullPath_logsError()
    {
        MaxsLogger.activateFileLogging(null);
        assertFalse(MaxsLogger.isLoggingToFileActivated());
    }

    @Test
    void activateFileLogging_validPath_setsLogPath(@TempDir final Path tempDir)
    {
        final File validFile = new File(tempDir.toFile(), "logfile.maxs");
        MaxsLogger.activateFileLogging(validFile);
        assertTrue(MaxsLogger.isLoggingToFileActivated());
    }

    @BeforeEach
    void beforeEach()
    {
        MaxsLogger.reset();
        MaxsLogger.setAppInformation(null, null);
    }

    @Test
    void isLoggingToFileActivated(@TempDir final Path tempDir) throws IOException
    {
        // Logging to file is deactivated by default
        assertFalse(MaxsLogger.isLoggingToFileActivated());

        // Logging to file is activated
        final File tempFile = File.createTempFile("test", ".maxs", tempDir.toFile());
        MaxsLogger.activateFileLogging(tempFile);
        assertTrue(MaxsLogger.isLoggingToFileActivated());
    }

    @Test
    void logMessage()
    {
        // log one message
        MaxsLogger.logMessage(Routine.TR06, rexsGear, "Info message", MessageType.INFO);

        // check message in list
        assertEquals(1, MaxsLogger.kernelNotifications.size());
        assertEquals(12, MaxsLogger.kernelNotifications.get(0).getCompId());
        assertEquals("Info message", MaxsLogger.kernelNotifications.get(0).getMessage());
        assertEquals(MessageType.INFO, MaxsLogger.kernelNotifications.get(0).getType());
        assertEquals(Routine.TR06, MaxsLogger.kernelNotifications.get(0).getRoutine());
    }

    @Test
    void logMessage_withPart_logsMessage()
    {
        final Part part = RexsComp.builder().build();
        part.setRexsId(34);
        MaxsLogger.logMessage(Routine.TR06, part, "Warning message", MessageType.WARNING);
        assertEquals(1, MaxsLogger.kernelNotifications.size());
        assertEquals(34, MaxsLogger.kernelNotifications.get(0).getCompId());
        assertEquals("Warning message", MaxsLogger.kernelNotifications.get(0).getMessage());
        assertEquals(MessageType.WARNING, MaxsLogger.kernelNotifications.get(0).getType());
        assertEquals(Routine.TR06, MaxsLogger.kernelNotifications.get(0).getRoutine());
    }

    @Test
    void logMessage_withRexsComponent_logsMessage()
    {
        MaxsLogger.logMessage(Routine.TR06, rexsGear, "Info message", MessageType.INFO);
        assertEquals(1, MaxsLogger.kernelNotifications.size());
        assertEquals(12, MaxsLogger.kernelNotifications.get(0).getCompId());
        assertEquals("Info message", MaxsLogger.kernelNotifications.get(0).getMessage());
        assertEquals(MessageType.INFO, MaxsLogger.kernelNotifications.get(0).getType());
        assertEquals(Routine.TR06, MaxsLogger.kernelNotifications.get(0).getRoutine());
        assertTrue(MaxsLogger.kernelNotifications.get(0).getMessage().contains("Info"));
    }

    @Test
    void logMessage_withString_logsMessage()
    {
        MaxsLogger.logMessage(Routine.TR06, "Error message", MessageType.ERROR);
        assertEquals(1, MaxsLogger.kernelNotifications.size());
        assertEquals("Error message", MaxsLogger.kernelNotifications.get(0).getMessage());
        assertEquals(MessageType.ERROR, MaxsLogger.kernelNotifications.get(0).getType());
        assertEquals(Routine.TR06, MaxsLogger.kernelNotifications.get(0).getRoutine());
    }

    @Test
    void requireNonNull_doubleValueIsNaN_logsMissingAttribute()
    {
        final RexsComp part = RexsComp.builder().build();
        MaxsLogger.requireNonNull(Routine.TR06, part, Double.NaN, "attribute");
        assertEquals(1, MaxsLogger.kernelNotifications.size());
        assertEquals(MessageType.DEBUG_ERROR, MaxsLogger.kernelNotifications.get(0).getType());
    }

    @Test
    void requireNonNull_doubleValueIsNonNaN_doesNotLog()
    {
        final RexsComp part = RexsComp.builder().build();
        MaxsLogger.requireNonNull(Routine.TR06, part, 0.0, "attribute");
        assertEquals(0, MaxsLogger.kernelNotifications.size());
    }

    @Test
    void requireNonNull_enumValueIsNull_logsMissingAttribute()
    {
        final RexsComp part = RexsComp.builder().build();
        MaxsLogger.requireNonNull(Routine.TR06, part, (TestEnum) null, "attribute");
        assertEquals(1, MaxsLogger.kernelNotifications.size());
        assertEquals(MessageType.DEBUG_ERROR, MaxsLogger.kernelNotifications.get(0).getType());
    }

    @Test
    void requireNonNull_enumValueIsUnknown_logsMissingAttribute()
    {
        final RexsComp part = RexsComp.builder().build();
        MaxsLogger.requireNonNull(Routine.TR06, part, TestEnum.UNKNOWN, "attribute");
        assertEquals(1, MaxsLogger.kernelNotifications.size());
        assertEquals(MessageType.DEBUG_ERROR, MaxsLogger.kernelNotifications.get(0).getType());
    }

    @Test
    void requireNonNull_enumValueIsValid_doesNotLog()
    {
        final RexsComp part = RexsComp.builder().build();
        MaxsLogger.requireNonNull(Routine.TR06, part, TestEnum.GREASE, "attribute");
        assertEquals(0, MaxsLogger.kernelNotifications.size());
    }

    @Test
    void requireNonNull_quantityIsNonNull_doesNotLog()
    {
        final RexsComp part = RexsComp.builder().centerDistance(getQuantity(0, MILLIMETER)).build();
        MaxsLogger.requireNonNull(Routine.TR06, part, part.getCenterDistance(), "attribute");
        assertEquals(0, MaxsLogger.kernelNotifications.size());
    }

    @Test
    void requireNonNull_quantityIsNull_logsMissingAttribute()
    {
        final RexsComp part = RexsComp.builder().build();
        MaxsLogger.requireNonNull(Routine.TR06, part, (Quantity<?>) null, "attribute");
        assertEquals(1, MaxsLogger.kernelNotifications.size());
        assertEquals(MessageType.DEBUG_ERROR, MaxsLogger.kernelNotifications.get(0).getType());
    }

    @Test
    void requireNonZero_doubleValueIsNaN_logsMissingAttribute()
    {
        final RexsComp part = RexsComp.builder().build();
        MaxsLogger.requireNonZero(Routine.TR06, part, Double.NaN, "attribute");
        assertEquals(1, MaxsLogger.kernelNotifications.size());
        assertEquals(MessageType.DEBUG_ERROR, MaxsLogger.kernelNotifications.get(0).getType());
    }

    @Test
    void requireNonZero_doubleValueIsNonZero_doesNotLog()
    {
        final RexsComp part = RexsComp.builder().build();
        MaxsLogger.requireNonZero(Routine.TR06, part, 1.0, "attribute");
        assertEquals(0, MaxsLogger.kernelNotifications.size());
    }

    @Test
    void requireNonZero_doubleValueIsZero_logsMissingAttribute()
    {
        final RexsComp part = RexsComp.builder().build();
        MaxsLogger.requireNonZero(Routine.TR06, part, 0.0, "attribute");
        assertEquals(1, MaxsLogger.kernelNotifications.size());
        assertEquals(MessageType.DEBUG_ERROR, MaxsLogger.kernelNotifications.get(0).getType());
    }

    @Test
    void requireNonZero_intValueIsNonZero_doesNotLog()
    {
        final RexsComp part = RexsComp.builder().build();
        MaxsLogger.requireNonZero(Routine.TR06, part, 1, "attribute");
        assertEquals(0, MaxsLogger.kernelNotifications.size());
    }

    @Test
    void requireNonZero_intValueIsZero_logsMissingAttribute()
    {
        final RexsComp part = RexsComp.builder().build();
        MaxsLogger.requireNonZero(Routine.TR06, part, 0, "attribute");
        assertEquals(1, MaxsLogger.kernelNotifications.size());
        assertEquals(MessageType.DEBUG_ERROR, MaxsLogger.kernelNotifications.get(0).getType());
    }

    @Test
    void requireNonZero_quantityIsNonZero_doesNotLog()
    {
        final RexsComp part = RexsComp.builder().centerDistance(getQuantity(10, MILLIMETER)).build();
        final Quantity<?> quantity = part.getCenterDistance();
        MaxsLogger.requireNonZero(Routine.TR06, part, quantity, "attribute");
        assertEquals(0, MaxsLogger.kernelNotifications.size());
    }

    @Test
    void requireNonZero_quantityIsNull_logsMissingAttribute()
    {
        final RexsComp part = RexsComp.builder().build();
        MaxsLogger.requireNonZero(Routine.TR06, part, null, "attribute");
        assertEquals(1, MaxsLogger.kernelNotifications.size());
        assertEquals(MessageType.DEBUG_ERROR, MaxsLogger.kernelNotifications.get(0).getType());
    }

    @Test
    void requireNonZero_quantityIsZero_logsMissingAttribute()
    {
        final RexsComp part = RexsComp.builder().centerDistance(getQuantity(0, MILLIMETER)).build();
        final Quantity<Length> quantity = part.getCenterDistance();
        MaxsLogger.requireNonZero(Routine.TR06, part, quantity, "attribute");
        assertEquals(1, MaxsLogger.kernelNotifications.size());
        assertEquals(MessageType.DEBUG_ERROR, MaxsLogger.kernelNotifications.get(0).getType());
    }
}