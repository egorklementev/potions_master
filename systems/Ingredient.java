package ru.erked.potionsmaster.systems;

public class Ingredient {

    private int id;
    private int[] weeks;
    private int trend;
    private int[] comb;
    private int[] bad_comb;

    // For the potion display
    private int color;
    private int race;
    private int taste;
    private boolean is_male;

    public Ingredient (
            int id,
            int[] weeks,
            int trend,
            int[] comb,
            int[] bad_comb,
            int color,
            int race,
            int taste,
            boolean is_male
    ) {
        this.id = id;
        this.weeks = weeks;
        this.trend = trend;
        this.comb = comb;
        this.bad_comb = bad_comb;
        this.color = color;
        this.race = race;
        this.taste = taste;
        this.is_male = is_male;
    }

    public int getId() {
        return id;
    }
    public int[] getWeeks() {
        return weeks;
    }
    public int getTrend() {
        return trend;
    }
    public int[] getComb() {
        return comb;
    }
    public int[] getBadComb() {
        return bad_comb;
    }

    public int getColor() {
        return color;
    }
    public int getRace() {
        return race;
    }
    public int getTaste() {
        return taste;
    }

    public boolean isMale() {
        return is_male;
    }
}
