package com.fva.rexs.maxslogger;


import com.fva.rexs.maxslogger.xml.Item;
import com.fva.rexs.maxslogger.xml.KernelNotifications;
import com.fva.rexs.maxslogger.xml.Notification;
import info.rexs.model.RexsComponent;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.Precision;
import tech.units.indriya.quantity.NumberQuantity;

import javax.measure.Quantity;
import java.io.File;

/**
 * MaxsLogger is a utility class for the generation of MAXS log files.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MaxsLogger {

    /**
     * The XML object holding all .
     */
    static final KernelNotifications kernelNotifications = new KernelNotifications();

    /**
     * The path to the log file.
     */
    private static File logPath;

    /**
     * Activates logging to file.
     *
     * @param logFile
     *         the path to the log file
     */
    public static void activateFileLogging(final File logFile)
    {
        // null check
        if (logFile == null) {
            log.error("The given path is null");
            return;
        }

        // check if file ending is *.maxs (case-insensitive)
        if (!logFile.getName().toLowerCase().endsWith(".maxs")) {
            log.error("The given log path does not end with '.maxs': {}", logFile);
            return;
        }

        // create file, if it does not exist
        // if path is not writable, log an error and return
        try {
            writeToFile(logFile);
        } catch (final JAXBException e) {
            log.error("Cannot write to the logfile: {}", e.getMessage());
            return;
        }

        // set the log path and write all messages that are already stored in the list to the file
        MaxsLogger.logPath = logFile;
        log.info("The log path is set to: {}", logFile);
    }

    /**
     * Deactivates logging to file.
     */
    public static void deactivateFileLogging()
    {
        log.info("Logging to file has been deactivated.");
        logPath = null;
    }

    /**
     * Returns if logging to a file is activated.
     *
     * @return true if logging to a file is activated, false otherwise
     */
    public static boolean isLoggingToFileActivated()
    {
        return logPath != null;
    }

    /**
     * Logs a new message.
     *
     * @param routineType
     *         the standard
     * @param rexsComponent
     *         the rexs component
     * @param message
     *         a message string which shall be logged
     * @param messageType
     *         severity of the message
     */
    public static void logMessage(final Routine routineType, final RexsComponent rexsComponent, final String message, final MessageType messageType)
    {
        // Create the log message
        final Notification notification = new Notification();
        notification.setRoutine(routineType);
        notification.setMessage(message);
        notification.setType(messageType);
        notification.setCompId(rexsComponent.getId());

        // store it in the list and write it to file
        kernelNotifications.add(notification);
        writeToFileIfActivated();
    }

    /**
     * Logs a new message.
     *
     * @param routineType
     *         the standard
     * @param part
     *         the part
     * @param message
     *         a message string which shall be logged
     * @param messageType
     *         severity of the message
     */
    public static void logMessage(final Routine routineType, final Part part, final String message, final MessageType messageType)
    {
        // Create the log message
        final Notification notification = new Notification();
        notification.setRoutine(routineType);
        notification.setMessage(message);
        notification.setType(messageType);
        notification.setCompId(part.getRexsId());

        // store it in the list and write it to file
        kernelNotifications.add(notification);
        writeToFileIfActivated();
    }

    /**
     * Logs a new message.
     *
     * @param routineType
     *         the standard
     * @param message
     *         a message string which shall be logged
     * @param messageType
     *         severity of the message
     */
    public static void logMessage(final Routine routineType, final String message, final MessageType messageType)
    {
        // Create the log message
        final Notification notification = new Notification();
        notification.setRoutine(routineType);
        notification.setMessage(message);
        notification.setType(messageType);

        // store it in the list and write it to file
        kernelNotifications.add(notification);
        writeToFileIfActivated();
    }

    /**
     * Logs a missing attribute error.
     *
     * @param routine
     *         the routine
     * @param part
     *         the part
     * @param value
     *         the value
     * @param attribute
     *         the attribute name
     */
    private static void logMissingAttribute(final Routine routine, final Part part, final Object value, final String attribute)
    {
        final Notification notification = new Notification();
        final Item item = new Item();

        notification.setRoutine(routine);
        notification.setMessage(attribute + " is required to perform the calculation but is missing.");
        notification.setType(MessageType.DEBUG_ERROR);
        notification.setCompId(part.getRexsId());

        if (value == null) {
            item.setValue(Double.NaN);
        }
        else if (value instanceof final Double d) {
            item.setValue(d);
        }
        else if (value instanceof final Integer integer) {
            item.setValue(integer);
        }
        else if (value instanceof final NumberQuantity<?> quantity) {
            item.setValue(quantity.getValue().doubleValue());
        }
        else if (value instanceof Enum<?>) {
            final String enumName = ((Enum<?>) value).name();
            if ("UNKNOWN".equalsIgnoreCase(enumName)) {
                item.setValue(Double.NaN);
            }
        }
        else {
            log.error("Unsupported Data Type");
        }
        item.setAttrId(attribute);
        item.setCompId(part.getRexsId());
        notification.getData().add(item);

        // store it in the list and write it to file
        kernelNotifications.add(notification);
        writeToFileIfActivated();
    }

    /**
     * Requires non NaN logs to notification logger if the attribute value is NaN
     *
     * @param routine
     *         the routine
     * @param part
     *         the part
     * @param value
     *         the double field
     * @param attribute
     *         the name of the attribute
     */
    public static void requireNonNull(final Routine routine, final Part part, final double value, final String attribute)
    {
        if (Double.isNaN(value)) {
            logMissingAttribute(routine, part, value, attribute);
        }
    }

    /**
     * Requires non null logs to notification logger if the attribute value is null for quantity type
     *
     * @param routine
     *         the routine
     * @param part
     *         the part
     * @param quantity
     *         the quantity field
     * @param attribute
     *         the name of the attribute
     * @param <Q>
     *         the quantity of type Q
     */
    public static <Q extends Quantity<Q>> void requireNonNull(final Routine routine, final Part part, final Quantity<Q> quantity, final String attribute)
    {
        if (quantity == null) {
            logMissingAttribute(routine, part, null, attribute);
        }
    }

    /**
     * Ensures that the provided enum value is not "unknown" or {@code null}. If the enum value is "unknown" or {@code null}, logs the missing attribute.
     *
     * @param routine
     *         the routine
     * @param part
     *         the part associated with the attribute
     * @param enumValue
     *         the enum value to check
     * @param attribute
     *         the name of the attribute
     * @param <E>
     *         the type of the enum
     */
    public static <E extends Enum<E>> void requireNonNull(final Routine routine, final Part part, final E enumValue, final String attribute)
    {

        if (enumValue == null) {
            logMissingAttribute(routine, part, null, attribute);
            return;
        }
        if (enumValue.toString().equalsIgnoreCase("unknown")) {
            logMissingAttribute(routine, part, enumValue, attribute);
        }
    }

    /**
     * Ensures that the provided value is not zero. If the value is zero, logs the missing attribute.
     *
     * @param routine
     *         the routine
     * @param part
     *         the part associated with the attribute
     * @param value
     *         the double value to check
     * @param attribute
     *         the name of the attribute
     */
    public static void requireNonZero(final Routine routine, final Part part, final double value, final String attribute)
    {
        if (Double.isNaN(value) || Precision.equalsWithRelativeTolerance(value, 0, 1e-7)) {
            logMissingAttribute(routine, part, value, attribute);
        }
    }

    /**
     * Ensures that the provided quantity value is not zero. If the value is zero, logs the missing attribute.
     *
     * @param routine
     *         the routine
     * @param part
     *         the part associated with the attribute
     * @param quantity
     *         the quantity value to check
     * @param attribute
     *         the name of the attribute
     * @param <Q>
     *         the type of the quantity
     */
    public static <Q extends Quantity<Q>> void requireNonZero(final Routine routine, final Part part, final Quantity<Q> quantity, final String attribute)
    {
        if (quantity == null) {
            logMissingAttribute(routine, part, null, attribute);
            return;
        }
        if (Precision.equalsWithRelativeTolerance(quantity.getValue().doubleValue(), 0, 1e-7)) {
            logMissingAttribute(routine, part, quantity, attribute);
        }
    }

    /**
     * Requires non-zero logs to notification logger if the attribute value is zero
     *
     * @param routine
     *         the routine
     * @param part
     *         the part
     * @param value
     *         the integer field
     * @param attribute
     *         the name of the attribute
     */
    public static void requireNonZero(final Routine routine, final Part part, final int value, final String attribute)
    {
        if (value == 0) {
            logMissingAttribute(routine, part, value, attribute);
        }
    }

    /**
     * Resets the notification logger by deactivating file logging and clearing .
     */
    public static void reset()
    {
        deactivateFileLogging();
        kernelNotifications.clear();
        log.info("The notification logger has been reset.");
    }

    /**
     * Sets the application information for the kernel .
     *
     * @param appId
     *         the application ID
     * @param appVersion
     *         the application version
     */
    public static void setAppInformation(final String appId, final String appVersion)
    {
        kernelNotifications.setAppId(appId);
        kernelNotifications.setAppVersion(appVersion);
    }

    /**
     * Writes the  to the log file.
     *
     * @param logFile
     *         the log file path
     */
    private static void writeToFile(final File logFile) throws JAXBException
    {
        final JAXBContext context = JAXBContext.newInstance(KernelNotifications.class);
        final Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(kernelNotifications, logFile);
    }

    /**
     * Writes the  to the defined log file, if logging to file is activated.
     */
    private static void writeToFileIfActivated()
    {
        if (isLoggingToFileActivated()) {
            try {
                writeToFile(logPath);
            } catch (final JAXBException e) {
                log.error("Unable to write  to file: {}", e.getMessage());
            }
        }
    }
}