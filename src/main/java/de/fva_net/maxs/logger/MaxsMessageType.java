package de.fva_net.maxs.logger;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents the type of message in the MAXS logging system.
 * <p>
 * Message types indicate the severity and purpose of a log entry. Debug types are intended for development and troubleshooting.
 */
@Getter
@AllArgsConstructor
public enum MaxsMessageType {
	/**
	 * Error message type.
	 * <p>
	 * Indicates a failure or critical issue that requires immediate attention.
     */
	ERROR(false),

    /**
	 * Warning message type.
	 * <p>
	 * Indicates a potential problem or non-critical issue that should be reviewed.
     */
	WARNING(false),

    /**
	 * Info message type.
	 * <p>
	 * Provides informational messages about normal operations.
     */
	INFO(false),

	/**
	 * Debug error message type.
	 * <p>
	 * Used for debugging critical errors during development.
     */
	DEBUG_ERROR(true),

    /**
	 * Debug warning message type.
	 * <p>
	 * Used for debugging potential issues during development.
     */
	DEBUG_WARNING(true),

    /**
	 * Debug info message type.
	 * <p>
	 * Used for debugging informational messages during development.
     */
	DEBUG_INFO(true);

	/**
	 * Indicates whether this message type is for debugging purposes.
	 */
	private final boolean debug;

}
