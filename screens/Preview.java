package ru.erked.potionsmaster.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.ArrayList;

import ru.erked.potionsmaster.GameStarter;
import ru.erked.potionsmaster.systems.AdvScreen;
import ru.erked.potionsmaster.systems.Potion;
import ru.erked.potionsmaster.systems.TextSystem;
import ru.erked.potionsmaster.utils.Fonts;
import ru.erked.potionsmaster.utils.Ingredients;
import ru.erked.potionsmaster.utils.Sounds;

public class Preview extends AdvScreen {

    private float timer = 0f;

    public Preview (GameStarter game) {
        //
        super(game);
        //
    }

    @Override
    public void show () {
        //
        /*  Initialisation of main game systems */
        g.prefs = Gdx.app.getPreferences("potions_master_prefs");
        g.lang = g.prefs.getInteger("language", 0);
        g.atlas = new TextureAtlas("textures/pm_resources.atlas");
        g.textSystem = new TextSystem(g.lang);
        g.sounds = new Sounds();
        g.fonts = new Fonts(g.textSystem.get("FONT_CHARS"));

        g.w = Gdx.graphics.getWidth();
        g.h = Gdx.graphics.getHeight();

        g.is_music = g.prefs.getBoolean("is_music_on", true);
        g.is_sound = g.prefs.getBoolean("is_sound_on", true);

        g.music_volume = g.prefs.getFloat("music_volume", 1f);
        g.sound_volume = g.prefs.getFloat("sound_volume", 1f);

        g.day = g.prefs.getInteger("day", 1);
        g.week = g.prefs.getInteger("week", 1);
        g.money = g.prefs.getInteger("money", 0);

        g.ing_added = new int[5];
        g.ing_to_add = new int[10];
        String[] temp_1 = g.prefs.getString("ing_added", "0 0 0 0 0").split(" ");
        String[] temp_2 = g.prefs.getString("ing_to_add", "0 0 0 0 0 0 0 0 0 0").split(" ");
        for (int i = 0; i < 5; i++) g.ing_added[i] = Integer.parseInt(temp_1[i]);
        for (int i = 0; i < 10; i++) g.ing_to_add[i] = Integer.parseInt(temp_2[i]);

        g.ings = new Ingredients();
        g.potions = new ArrayList<>();
        for (int i = 0; i < (g.week - 1)*5 + (g.day - 1); ++i) {
            String[] temp = g.prefs.getString("potion_" + i).split(" ");
            g.potions.add(new Potion(
                    Integer.parseInt(temp[0]),
                    new int[]{
                            Integer.parseInt(temp[1]),
                            Integer.parseInt(temp[2]),
                            Integer.parseInt(temp[3]),
                            Integer.parseInt(temp[4]),
                            Integer.parseInt(temp[5]),
                    }
            ));
        }

        g.weeks = new int[10];
        String[] weeks = g.prefs.getString("weeks", "1 2 3 4 5 6 7 8 9 10 ").split(" ");
        for (int i = 0; i < 10; ++i) {
            g.weeks[i] = Integer.parseInt(weeks[i]);
        }

        g.trends = new int[50];
        String[] trends = g.prefs.getString("trends",
                "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 1 2 3 4 5 6 7 8 9 10 " +
                "11 12 13 14 15 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 1 2 3 4 5 "
        ).split(" ");
        for (int i = 0; i < 50; ++i) {
            g.trends[i] = Integer.parseInt(trends[i]);
        }

        if (g.is_music) {
            g.sounds.music_1.setLooping(true);
            g.sounds.music_1.setVolume(g.music_volume);
            g.sounds.music_1.play();
        }

    }

    @Override
    public void render (float delta) {
        //
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        /* To the next screen. */
        timer += delta;
        if (timer >= 5.5f) g.setScreen(new Menu(g));
        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) timer += 5.5f;
        if (Gdx.input.justTouched()) timer += 5.5f;
        //
    }


}
