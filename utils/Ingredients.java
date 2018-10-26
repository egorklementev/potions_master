package ru.erked.potionsmaster.utils;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;

import ru.erked.potionsmaster.systems.Ingredient;

public class Ingredients {

    public Color[] colors;
    public ArrayList<Ingredient> list;

    public Ingredients () {
        list = new ArrayList<>();
        colors = new Color[]{
                Color.BLUE, // 0
                Color.NAVY, // 1
                Color.ROYAL, // 2
                Color.SLATE, // 3
                Color.SKY, // 4
                Color.CYAN, // 5
                Color.TEAL, // 6
                Color.GREEN, // 7
                Color.CHARTREUSE, // 8
                Color.LIME, // 9
                Color.FOREST, // 10
                Color.OLIVE, // 11
                Color.YELLOW, // 12
                Color.GOLD, // 13
                Color.GOLDENROD, // 14
                Color.ORANGE, // 15
                Color.BROWN, // 16
                Color.TAN, // 17
                Color.FIREBRICK, // 18
                Color.RED, // 19
                Color.SCARLET, // 20
                Color.CORAL, // 21
                Color.SALMON, // 22
                Color.PINK, // 23
                Color.MAGENTA, // 24
                Color.PURPLE, // 25
                Color.VIOLET, // 26
                Color.MAROON //27
        };

        // Races:
        // 1) Human
        // 2) Elves
        // 3) Dwarfs
        // 4) Orcs

        // Taste:
        // 1) Tasty
        // 2) Tasteless
        // 3) Smelly

        // Water from Wetterd's swamps
        list.add(new Ingredient(
                1, new int[]{2,8},1,
                new int[]{4, 7, 9, 12, 14, 18, 19, 24, 27, 31, 32, 34, 37, 44, 45},
                new int[]{2, 5, 8, 11, 15, 16, 21, 22, 23, 29, 33, 35, 39, 40, 41},
                11,4,2,false));
        // Water from fountain of youth
        list.add(new Ingredient(
                2, new int[]{2,8}, 1,
                new int[]{5, 6, 8, 11, 16, 17, 22, 23, 25, 29, 35, 36, 39, 41, 43},
                new int[]{1, 7, 9, 12, 14, 15, 21, 24, 26, 28, 30, 32, 37, 42, 44},
                13, 2, 1, false));
        // Water from north seas
        list.add(new Ingredient(
                3, new int[]{2,4}, 1,
                new int[]{7, 8, 9, 12, 13, 14, 20, 21, 27, 28, 31, 32, 38, 42, 44},
                new int[]{4, 5, 6, 11, 15, 17, 19, 22, 23, 33, 35, 36, 37, 39, 41},
                4, 1, 2, true));
        // Juice of a firepine
        list.add(new Ingredient(
                4, new int[]{3,10}, 2,
                new int[]{1, 7, 12, 15, 17, 19, 22, 23, 31, 33, 36, 38, 40, 43},
                new int[]{3, 5, 8, 11, 13, 16, 20, 25, 27, 28, 30, 35, 37, 42, 45},
                20, 4, 2, true));
        // Juice of royal apple tree
        list.add(new Ingredient(
                5, new int[]{1,7}, 2,
                new int[]{2, 6, 9, 10, 11, 16, 19, 23, 25, 29, 30, 36, 37, 39, 41},
                new int[]{1, 3, 7, 12, 13, 18, 20, 22, 24, 31, 32, 34, 38, 40, 45},
                23, 1, 1, false));
        // Juice of south birch
        list.add(new Ingredient(
                6, new int[]{5,7}, 2,
                new int[]{2, 5, 8, 11, 16, 18, 21, 24, 26, 28, 30, 35, 39, 41, 43},
                new int[]{3, 7, 9, 12, 13, 17, 20, 25, 27, 29, 31, 34, 38, 42, 44},
                22, 2, 2, false));
        // Decoction of oblique grass
        list.add(new Ingredient(
                7, new int[]{5,6}, 3,
                new int[]{1, 3, 4, 10, 17, 18, 21, 24, 26, 30, 33, 34, 37, 40, 45},
                new int[]{2, 8, 14, 19, 25, 29, 36, 38, 44},
                6, 4, 3, false));
        // Decoction of seeds of treeflower
        list.add(new Ingredient(
                8, new int[]{5,9}, 3,
                new int[]{2, 3, 6, 10, 13, 15, 20, 25, 27, 28, 29, 35, 38, 42, 44},
                new int[]{1, 7, 9, 14, 17, 19, 21, 26, 32, 34, 36, 41, 43, 45},
                26, 2, 1, false));
        // Decoction of mad caterpillar
        list.add(new Ingredient(
                9, new int[]{1,4}, 3,
                new int[]{1, 3, 5, 13, 14, 15, 20, 22, 26, 32, 33, 34, 40, 42, 45},
                new int[]{2, 7, 8, 10, 16, 18, 23, 24, 27, 28, 30, 31, 39, 41, 43},
                17, 3, 3, false));
        // Bowstring of elven king
        list.add(new Ingredient(
                10, new int[]{1,6}, 4,
                new int[]{5, 7, 8, 11, 12, 16, 19, 23, 26, 29, 35, 36, 39, 43, 44},
                new int[]{9, 13, 14, 15, 21, 22, 27, 28, 31, 32, 38, 40, 45},
                2, 2, 2, true));
        // Birch bark
        list.add(new Ingredient(
                11, new int[]{1,6}, 4,
                new int[]{5, 7, 8, 11, 12, 16, 19, 23, 26, 29, 35, 36, 39, 43, 44},
                new int[]{9, 13, 14, 15, 21, 22, 27, 28, 31, 32, 38, 40, 45},
                2, 2, 2, true));

    }

}
