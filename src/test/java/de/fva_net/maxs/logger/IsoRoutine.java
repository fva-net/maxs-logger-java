package de.fva_net.maxs.logger;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum representing ISO routines for the MAXS logging system.
 * <p>
 * Each routine has a unique identifier used for logging.
 */
@Getter
@AllArgsConstructor
public enum IsoRoutine implements MaxsLoggableRoutine {
	/**
	 * ISO 21771:2007 routine.
	 */
    ISO21771_2007("iso21771_2007"),
	/**
	 * ISO 6336:2019 routine.
	 */
    ISO6336_2019("iso6336_2019");

	/**
	 * The unique identifier for the routine.
	 */
	private final String maxsId;
}
