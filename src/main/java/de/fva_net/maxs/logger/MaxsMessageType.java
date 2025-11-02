package de.fva_net.maxs.logger;

/**
 * Enumeration representing message types.
 */
public enum MaxsMessageType {
	/**
     * Represents an error message type.
	 * <p>
	 * Severity: High. Indicates a failure or critical issue.
     */
    ERROR,

    /**
     * Represents a warning message type.
	 * <p>
	 * Severity: Medium. Indicates a potential problem or non-critical issue.
     */
    WARNING,

    /**
     * Represents an info message type.
	 * <p>
	 * Severity: Low. Provides informational messages about normal operations.
     */
    INFO,

	/**
	 * Represents a debug error message type.
	 * <p>
	 * Severity: High (Debug). Used for debugging critical errors.
     */
    DEBUG_ERROR,

    /**
     * Represents a debug warning message type.
	 * <p>
	 * Severity: Medium (Debug). Used for debugging potential issues.
     */
    DEBUG_WARNING,

    /**
     * Represents a debug info message type.
	 * <p>
	 * Severity: Low (Debug). Used for debugging informational messages.
     */
    DEBUG_INFO
}
