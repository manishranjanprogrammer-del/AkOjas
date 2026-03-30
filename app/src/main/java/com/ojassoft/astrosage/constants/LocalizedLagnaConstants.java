package com.ojassoft.astrosage.constants;

/**
 * Reuses the default panchang constants while supplying locale-specific lagna labels for languages
 * that do not have a dedicated constants implementation.
 */
public class LocalizedLagnaConstants extends Constants {

    private final String[] rashiNames;
    private final String[] lagnaNatureNames;

    /**
     * Creates a localized constants wrapper with zodiac sign names and lagna nature labels.
     */
    public LocalizedLagnaConstants(String[] rashiNames, String[] lagnaNatureNames) {
        this.rashiNames = rashiNames;
        this.lagnaNatureNames = lagnaNatureNames;
    }

    /**
     * Returns the localized lagna na\me for the given zodiac index.
     */
    @Override
    public String getLagna(int index) {
        return rashiNames[index];
    }

    /**
     * Returns the localized lagna nature for the given zodiac index.
     */
    @Override
    public String getLagnaNature(int index) {
        return lagnaNatureNames[index];
    }

    /**
     * Returns the localized zodiac sign label for the given zodiac index.
     */
    @Override
    public String getRashi(int index) {
        return rashiNames[index];
    }
}
