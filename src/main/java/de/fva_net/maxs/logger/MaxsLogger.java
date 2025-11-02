package de.fva_net.maxs.logger;


import de.fva_net.maxs.logger.xml.Item;
import de.fva_net.maxs.logger.xml.KernelNotifications;
import de.fva_net.maxs.logger.xml.Notification;
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
import java.util.List;
import java.util.function.Predicate;

/**
 * MaxsLogger is a utility class for the generation of MAXS log files.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MaxsLogger {

	/**
	 * The XML object holding all .
	 */
	private static final KernelNotifications kernelNotifications = new KernelNotifications();

	/**
	 * The path to the log file.
	 */
	private static File logPath;

	/**
	 * Activates logging to file.
	 *
	 * @param logFile the path to the log file
	 */
	public static void activateFileLogging(final File logFile) {
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
	public static void deactivateFileLogging() {
		log.info("Logging to file has been deactivated.");
		logPath = null;
	}

	/**
	 * Returns if logging to a file is activated.
	 *
	 * @return true if logging to a file is activated, false otherwise
	 */
	public static boolean isLoggingToFileActivated() {
		return logPath != null;
	}

	/**
	 * Logs a new message.
	 *
	 * @param routine       the routine for which the messasge shall be logged
	 * @param rexsComponent the rexs component
	 * @param message       the log message
	 * @param messageType   the severity of the message
	 */
	public static void logMessage(final MaxsLoggableRoutine routine, final RexsComponent rexsComponent, final String message, final MaxsMessageType messageType) {
		if (rexsComponent == null) {
			logMessage(routine, message, messageType);
		} else {
			logMessage(routine, rexsComponent.getId(), message, messageType);
		}
	}

	/**
	 * Logs a new message.
	 *
	 * @param routine     the routine for which the messasge shall be logged
	 * @param componentId the numeric ID of the component
	 * @param message     the log message
	 * @param messageType the severity of the message
	 */
	public static void logMessage(final MaxsLoggableRoutine routine, final int componentId, final String message, final MaxsMessageType messageType) {
		// Create the log message
		final Notification notification = new Notification();
		notification.setRoutine(routine);
		notification.setMessage(message);
		notification.setType(messageType);
		if (componentId > 0) {
			notification.setCompId(componentId);
		}

		// store it in the list and write it to file
		kernelNotifications.add(notification);
		writeToFileIfActivated();
	}

	/**
	 * Logs a new message.
	 *
	 * @param routine     the routine for which the messasge shall be logged
	 * @param message     the log message
	 * @param messageType the severity of the message
	 */
	public static void logMessage(final MaxsLoggableRoutine routine, final String message, final MaxsMessageType messageType) {
		// Create the log message
		final Notification notification = new Notification();
		notification.setRoutine(routine);
		notification.setMessage(message);
		notification.setType(messageType);

		// store it in the list and write it to file
		kernelNotifications.add(notification);
		writeToFileIfActivated();
	}

	/**
	 * Logs a missing attribute error.
	 *
	 * @param routine     the routine
	 * @param componentId the numeric ID of the component
	 * @param value       the value
	 * @param attribute   the attribute name
	 */
	private static void logMissingAttribute(final MaxsLoggableRoutine routine, final int componentId, final Object value, final String attribute) {
		final Notification notification = new Notification();
		final Item item = new Item();

		notification.setRoutine(routine);
		notification.setMessage(attribute + " is required to perform the calculation but is missing.");
		notification.setType(MaxsMessageType.DEBUG_ERROR);
		notification.setCompId(componentId);

		if (value == null) {
			item.setValue(Double.NaN);
		} else if (value instanceof final Double d) {
			item.setValue(d);
		} else if (value instanceof final Integer integer) {
			item.setValue(integer);
		} else if (value instanceof final NumberQuantity<?> quantity) {
			item.setValue(quantity.getValue().doubleValue());
		} else if (value instanceof Enum<?>) {
			final String enumName = ((Enum<?>) value).name();
			if ("UNKNOWN".equalsIgnoreCase(enumName)) {
				item.setValue(Double.NaN);
			}
		} else {
			log.error("Unsupported Data Type");
		}
		item.setAttrId(attribute);
		item.setCompId(componentId);
		notification.getData().add(item);

		// store it in the list and write it to file
		kernelNotifications.add(notification);
		writeToFileIfActivated();
	}

	/**
	 * Requires non NaN logs to notification logger if the attribute value is NaN
	 *
	 * @param routine     the routine
	 * @param componentId the numeric ID of the component
	 * @param value       the double field
	 * @param attribute   the name of the attribute
	 */
	public static void requireNonNull(final MaxsLoggableRoutine routine, final int componentId, final double value, final String attribute) {
		if (Double.isNaN(value)) {
			logMissingAttribute(routine, componentId, value, attribute);
		}
	}

	/**
	 * Ensures that the provided value is not null. If the value is null or an enum with the name "UNKNOWN",
	 * logs the missing attribute.
	 *
	 * @param routine     the routine for which the attribute is being checked
	 * @param componentId the numeric ID of the component associated with the attribute
	 * @param value       the value to check for null or "UNKNOWN"
	 * @param attribute   the name of the attribute being checked
	 */
	public static void requireNonNull(final MaxsLoggableRoutine routine, final int componentId, final Object value, final String attribute) {
		// Check if the value is null and log the missing attribute if true
		if (value == null) {
			logMissingAttribute(routine, componentId, null, attribute);
		}

		// Check if the value is an enum with the name "UNKNOWN" and log the missing attribute if true
		else if (value instanceof Enum<?> enumValue && enumValue.name().equalsIgnoreCase("unknown")) {
			logMissingAttribute(routine, componentId, enumValue, attribute);
		}

		// Check if the value is a complex Double or Float and contains NaN
		else if (value instanceof Double d && Double.isNaN(d)) {
			logMissingAttribute(routine, componentId, d, attribute);
		}
	}

	/**
	 * Ensures that the provided value is not zero. If the value is zero, logs the missing attribute.
	 *
	 * @param routine     the routine
	 * @param componentId the numeric ID of the component associated with the attribute
	 * @param value       the double value to check
	 * @param attribute   the name of the attribute
	 */
	public static void requireNonZero(final MaxsLoggableRoutine routine, final int componentId, final double value, final String attribute) {
		if (Double.isNaN(value) || Precision.equalsWithRelativeTolerance(value, 0, 1e-7)) {
			logMissingAttribute(routine, componentId, value, attribute);
		}
	}

	/**
	 * Ensures that the provided quantity value is not zero. If the value is zero, logs the missing attribute.
	 *
	 * @param routine     the routine
	 * @param componentId the numeric ID of the component associated with the attribute
	 * @param quantity    the quantity value to check
	 * @param attribute   the name of the attribute
	 * @param <Q>         the type of the quantity
	 */
	public static <Q extends Quantity<Q>> void requireNonZero(final MaxsLoggableRoutine routine, final int componentId, final Quantity<Q> quantity, final String attribute) {
		if (quantity == null) {
			logMissingAttribute(routine, componentId, null, attribute);
			return;
		}
		if (Precision.equalsWithRelativeTolerance(quantity.getValue().doubleValue(), 0, 1e-7)) {
			logMissingAttribute(routine, componentId, quantity, attribute);
		}
	}

	/**
	 * Resets the notification logger by deactivating file logging and clearing .
	 */
	public static void reset() {
		deactivateFileLogging();
		kernelNotifications.clear();
		log.info("The notification logger has been reset.");
	}

	/**
	 * Sets the application information for the kernel .
	 *
	 * @param appId      the application ID
	 * @param appVersion the application version
	 */
	public static void setAppInformation(final String appId, final String appVersion) {
		kernelNotifications.setAppId(appId);
		kernelNotifications.setAppVersion(appVersion);
	}

	/**
	 * Writes the  to the log file.
	 *
	 * @param logFile the log file path
	 */
	private static void writeToFile(final File logFile) throws JAXBException {
		final JAXBContext context = JAXBContext.newInstance(KernelNotifications.class);
		final Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal(kernelNotifications, logFile);
	}

	/**
	 * Writes the  to the defined log file, if logging to file is activated.
	 */
	private static void writeToFileIfActivated() {
		if (isLoggingToFileActivated()) {
			try {
				writeToFile(logPath);
			} catch (final JAXBException e) {
				log.error("Unable to write  to file: {}", e.getMessage());
			}
		}
	}

	/**
	 * Retrieves an unmodifiable view of the kernel notifications.
	 *
	 * @return a unmodifiable view of the kernel notifications
	 */
	public static List<Notification> getAllNotifications() {
		return List.copyOf(kernelNotifications.getNotifications());
	}

	/**
	 * Retrieves a filtered list of notifications based on the provided predicate.
	 *
	 * @param filter the predicate to filter notifications
	 * @return a list of filtered notifications
	 */
	public static List<Notification> getFilteredNotifications(Predicate<Notification> filter) {
		return getAllNotifications().stream()
			.filter(filter)
			.toList();
	}

	/**
	 * Retrieves the application ID.
	 *
	 * @return the application ID
	 */
	public static String getAppId() {
		return kernelNotifications.getAppId();
	}

	/**
	 * Retrieves the application version.
	 *
	 * @return the application version
	 */
	public static String getAppVersion() {
		return kernelNotifications.getAppVersion();
	}
}
