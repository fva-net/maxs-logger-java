package de.fva_net.maxs.logger;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enumeration representing message types.
 */
@Getter
@AllArgsConstructor
public enum MaxsMessageType {
	/**
     * Represents an error message type.
	 * <p>
	 * Severity: High. Indicates a failure or critical issue.
     */
	ERROR(false),

    /**
     * Represents a warning message type.
	 * <p>
	 * Severity: Medium. Indicates a potential problem or non-critical issue.
     */
	WARNING(false),

    /**
     * Represents an info message type.
	 * <p>
	 * Severity: Low. Provides informational messages about normal operations.
     */
	INFO(false),

	/**
	 * Represents a debug error message type.
	 * <p>
	 * Severity: High (Debug). Used for debugging critical errors.
     */
	DEBUG_ERROR(true),

    /**
     * Represents a debug warning message type.
	 * <p>
	 * Severity: Medium (Debug). Used for debugging potential issues.
     */
	DEBUG_WARNING(true),

    /**
     * Represents a debug info message type.
	 * <p>
	 * Severity: Low (Debug). Used for debugging informational messages.
     */
	DEBUG_INFO(true);

	/**
	 * Indicates whether this message type is for debugging purposes.
	 */
	private final boolean debug;
}
