package de.fva_net.maxs.logger;

/**
 * Interface for routines that can be logged in the MAXS logging system.
 * <p>
 * The identifier returned by {@code getMaxsId()} must be lowercase and use underscores.
 * For DIN/ISO standards and similar, omit the part number of the standard and the month.
 * <p>
 * Example: {@code ISO 6336-2:2019-11} becomes {@code iso6336_2019}.
 * <p>
 * The identifier should be unique within the context of the application.
 */
public interface MaxsLoggableRoutine {
	/**
	 * Returns the unique identifier of the routine.
	 *
	 * @return the routine identifier
	 */
	String getMaxsId();
}
