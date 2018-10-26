package ru.erked.potionsmaster.systems;

import java.util.Arrays;

public class Potion {

    private int bottle;
    private int[] ingredients;

    public Potion (int bottle, int[] ingredients) {
        this.bottle = bottle;
        this.ingredients = Arrays.copyOf(ingredients, ingredients.length);
    }

    public int getBottle() {
        return bottle;
    }
    public int[] getIngredients() {
        return ingredients;
    }
}
