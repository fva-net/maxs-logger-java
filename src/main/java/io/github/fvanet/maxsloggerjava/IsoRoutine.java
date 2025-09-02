package io.github.fvanet.maxsloggerjava;

/**
 * Enum for ISO routines.
 */
public enum IsoRoutine implements Routine {
    ISO21771_2007("iso21771_2007"),
    ISO6336_2019("iso6336_2019");

    private final String id;

    IsoRoutine(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }
}