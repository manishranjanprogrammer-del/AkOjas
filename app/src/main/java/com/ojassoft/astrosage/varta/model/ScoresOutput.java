package com.ojassoft.astrosage.varta.model;

public class ScoresOutput {
    public Item STRESS_INDEX;
    public Item CAREER_SCORE;
    public Item LUCK_SCORE;
    public Item RELATIONSHIP_SCORE;
    public Item EMOTION_INDEX;
    public Item MONEY_INDEX;

    public static class Item {
        public String label;
        public int value;
    }
}



