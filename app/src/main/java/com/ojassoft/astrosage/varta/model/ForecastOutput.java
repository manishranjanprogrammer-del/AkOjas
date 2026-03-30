package com.ojassoft.astrosage.varta.model;

public class ForecastOutput {

    private Item BEST_ACTIVITY_OF_THE_DAY;
    private Item STRESS_TRIGGER;
    private Item VIBE_OF_THE_DAY;
    private Item ENERGY_PEAK;
    private Item LUCKY_COLOR;
    private Item ENERGY_DIP;
    private Item LUCKY_HOUR;

    public Item getBEST_ACTIVITY_OF_THE_DAY() { return BEST_ACTIVITY_OF_THE_DAY; }
    public Item getSTRESS_TRIGGER() { return STRESS_TRIGGER; }
    public Item getVIBE_OF_THE_DAY() { return VIBE_OF_THE_DAY; }
    public Item getENERGY_PEAK() { return ENERGY_PEAK; }
    public Item getLUCKY_COLOR() { return LUCKY_COLOR; }
    public Item getENERGY_DIP() { return ENERGY_DIP; }
    public Item getLUCKY_HOUR() { return LUCKY_HOUR; }

    public static class Item {
        public String label;
        public String value;
    }
}
