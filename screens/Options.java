package ru.erked.potionsmaster.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

import ru.erked.potionsmaster.GameStarter;
import ru.erked.potionsmaster.systems.AdvScreen;
import ru.erked.potionsmaster.systems.AdvSprite;
import ru.erked.potionsmaster.systems.Button;
import ru.erked.potionsmaster.systems.TextLine;
import ru.erked.potionsmaster.systems.TextSystem;

public class Options extends AdvScreen {

    /* Sprites */
    private Animation<TextureRegion> background_anim;
    private ArrayList<AdvSprite> background;
    private AdvSprite lang_ru;
    private AdvSprite lang_en;
    private AdvSprite sound_on;
    private AdvSprite music_on;
    private AdvSprite scroll_m;
    private AdvSprite scroll_s;
    private AdvSprite r_music;
    private AdvSprite r_sound;

    /* Buttons */
    private Button back;

    /* Text */
    private TextLine text_options;

    /* Random */
    private float state_time = 0f; // For all animations
    private float cursor_x = Gdx.input.getX(); // For rhombus movement
    private float cursor_y = Gdx.input.getY(); // For rhombus movement
    private final int RHOMBUSES = 10; // Background

    Options (GameStarter game) {
        //
        super(game);
        //
    }

    @Override
    public void show () {
        //
        super.show();

        if (g.is_music && !change_screen) {
            g.sounds.music_1.setLooping(true);
            g.sounds.music_1.setVolume(g.music_volume);
            g.sounds.music_1.play();
        }

        text_initialization();
        texture_initialization();
        button_initialization();
        stage_addition();

        /* For smooth transition btw screens */
        for (Actor act : stage.getActors()) act.getColor().set(act.getColor().r, act.getColor().g, act.getColor().b, 0f);
        //
    }

    @Override
    public void render (float delta) {
        //
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        g.sounds.music_1.setVolume(g.music_volume);

        state_time += delta;

        /* Transition btw screens */
        if (background.get(0).getColor().a == 0f) {
            if (change_screen) {
                this.dispose();
                g.setScreen(next_screen);
            } else {
                for (Actor act : stage.getActors()) act.addAction(Actions.alpha(1f, 0.5f));
            }
        }

        /* Teleportation of rhombuses */
        for (AdvSprite s : background) {
            if (s.getX() < -g.w / 4f) {
                s.setX(s.getX() + (g.w / 4f) * RHOMBUSES);
                s.setY(s.getY() - (g.w / 4f));
            }
            if (s.getY() > g.h + (g.w / 4f)) {
                s.setY(s.getY() - (g.w / 4f) * RHOMBUSES);
            }
        }

        rhombuses_movement();

        /* Animation */
        for (AdvSprite s : background) {s.getSprite().setRegion(background_anim.getKeyFrame(state_time, true));}

        stage.act(delta);
        stage.draw();

        /* Updating volume */
        g.music_volume = (r_music.getX() + 0.1f * r_music.getWidth() - scroll_m.getX()) / (scroll_m.getX() + scroll_m.getWidth());
        g.sound_volume = (r_sound.getX() + 0.1f * r_sound.getWidth() - scroll_s.getX()) / (scroll_s.getX() + scroll_s.getWidth());

        /* ESC listener. */
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) { this.dispose();Gdx.app.exit(); }
        if (Gdx.input.isKeyPressed(Input.Buttons.BACK)) {
            change_screen = true;
            next_screen = new Menu(g);
            for (Actor act : stage.getActors()) act.addAction(Actions.alpha(0f, 0.5f));
        }
        //
    }

    private void rhombuses_movement() {
        //
        /* Move rhombuses using cursor */
        if (Gdx.input.isTouched()) {

            boolean satisfy_x = cursor_x >= r_music.getX() &&
                    cursor_x <= r_music.getX() + r_music.getWidth();
            boolean satisfy_y = cursor_y >= r_music.getY() &&
                    cursor_y <= r_music.getY() + r_music.getHeight();

            if (satisfy_x && satisfy_y)
                r_music.addAction(Actions.moveBy(Gdx.input.getDeltaX(), 0f));

            satisfy_x = cursor_x >= r_sound.getX() &&
                    cursor_x <= r_sound.getX() + r_sound.getWidth();
            satisfy_y = cursor_y >= r_sound.getY() &&
                    cursor_y <= r_sound.getY() + r_sound.getHeight();

            if (satisfy_x && satisfy_y)
                r_sound.addAction(Actions.moveBy(Gdx.input.getDeltaX(), 0f));
        }
        if (r_music.getX() < scroll_m.getX()) r_music.setX(scroll_m.getX());
        if (r_music.getX() + r_music.getWidth() > scroll_m.getX() + scroll_m.getWidth())
            r_music.setX(scroll_m.getX() + scroll_m.getWidth() - r_music.getWidth());
        if (r_sound.getX() < scroll_s.getX()) r_sound.setX(scroll_s.getX());
        if (r_sound.getX() + r_sound.getWidth() > scroll_s.getX() + scroll_s.getWidth())
            r_sound.setX(scroll_s.getX() + scroll_s.getWidth() - r_sound.getWidth());
        cursor_x = Gdx.input.getX();
        cursor_y = g.h - Gdx.input.getY();
        //
    }

    private void text_initialization() {
        //
        text_options = new TextLine(
                g.fonts.f_0S,
                g.textSystem.get("options_btn"),
                0f,
                0.9f * g.h
        );
        text_options.setX(0.5f * (g.w - text_options.getWidth()));
        //
    }
    private void texture_initialization() {
        //
        background = new ArrayList<>();
        for (int i = 0; i < RHOMBUSES * RHOMBUSES; i++) {
            background.add(new AdvSprite(
                    g.atlas.createSprite("background", 0),
                    (i % RHOMBUSES) * g.w / 4f,
                    (i / RHOMBUSES) * g.w / 4f - (g.w / 4f),
                    g.w / 4f,
                    g.w / 4f
            ));
            background.get(i).addAction(Actions.forever(Actions.moveBy(-(g.w / 4f), (g.w / 4f), 5f)));
        }

        background_anim = new Animation<>(
                0.09375f,
                g.atlas.findRegions("background"),
                Animation.PlayMode.LOOP_PINGPONG);

        scroll_m = new AdvSprite(
                g.atlas.createSprite("scroll_line"),
                0.225f * g.w,
                0.625f * g.w,
                0.4f * g.w,
                0.05f * g.w
        );
        scroll_s = new AdvSprite(
                g.atlas.createSprite("scroll_line"),
                0.225f * g.w,
                0.375f * g.w,
                0.4f * g.w,
                0.05f * g.w
        );

        //
    }
    private void button_initialization() {
        //
        /* --BACK-- */
        back = new Button(
                g,
                0.675f * g.w,
                0.025f * g.w,
                0.3f * g.w,
                g.fonts.f_5.getFont(),
                g.textSystem.get("back_btn"),
                1,
                "back_btn"
        );
        back.get().addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if (g.is_sound) g.sounds.click.play(g.sound_volume); // Click sound
                change_screen = true;
                next_screen = new Menu(g);
                back.get().setChecked(false);
                for (Actor act : stage.getActors()) act.addAction(Actions.alpha(0f, 0.5f));
            }
        });

        /* --LANG_RU-- */
        lang_ru = new AdvSprite(
                g.atlas.createSprite("opt_lang_ru"),
                0.01f * g.w,
                0.01f * g.w,
                0.2f * g.w,
                0.2f * g.w
        );
        if (g.lang != 1) lang_ru.setColor(Color.GRAY);
        lang_ru.addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if (g.is_sound) g.sounds.click.play(g.sound_volume); // Click sound
                lang_ru.setColor(Color.WHITE);
                lang_en.setColor(Color.GRAY);
                g.lang = 1;
                g.textSystem = new TextSystem(g.lang);
                text_options.setText(g.textSystem.get("options_btn"));
                text_options.setPosition(.5f*(g.w - g.fonts.f_0S.getWidth(g.textSystem.get("options_btn"))), text_options.getY());
                back.get().setText(g.textSystem.get("back_btn"));
            }
        });

        /* --LANG_EN-- */
        lang_en = new AdvSprite(
                g.atlas.createSprite("opt_lang_en"),
                0.225f * g.w,
                0.01f * g.w,
                0.2f * g.w,
                0.2f * g.w
        );
        if (g.lang != 0) lang_en.setColor(Color.GRAY);
        lang_en.addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if (g.is_sound) g.sounds.click.play(g.sound_volume); // Click sound
                lang_ru.setColor(Color.GRAY);
                lang_en.setColor(Color.WHITE);
                g.lang = 0;
                g.textSystem = new TextSystem(g.lang);
                text_options.setText(g.textSystem.get("options_btn"));
                text_options.setPosition(.5f*(g.w - g.fonts.f_0S.getWidth(g.textSystem.get("options_btn"))), text_options.getY());
                back.get().setText(g.textSystem.get("back_btn"));
            }
        });

        /* --SOUND-- */
        sound_on = new AdvSprite(
                g.atlas.createSprite("opt_sound"),
                0.01f * g.w,
                0.3f * g.w,
                0.2f * g.w,
                0.2f * g.w
        );
        if (!g.is_sound) sound_on.setColor(Color.GRAY);
        sound_on.addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if (g.is_sound) g.sounds.click.play(g.sound_volume); // Click sound
                if (g.is_sound) {
                    g.is_sound = false;
                    sound_on.setColor(Color.GRAY);
                } else {
                    g.is_sound = true;
                    sound_on.setColor(Color.WHITE);
                }
            }
        });

        /* --MUSIC-- */
        music_on = new AdvSprite(
                g.atlas.createSprite("opt_music"),
                0.01f * g.w,
                0.55f * g.w,
                0.2f * g.w,
                0.2f * g.w
        );
        if (!g.is_music) music_on.setColor(Color.GRAY);
        music_on.addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if (g.is_sound) g.sounds.click.play(g.sound_volume); // Click sound
                if (g.is_music) {
                    if (g.sounds.music_1.isPlaying()) g.sounds.music_1.stop();
                    g.is_music = false;
                    music_on.setColor(Color.GRAY);
                } else {
                    g.sounds.music_1.setLooping(true);
                    g.sounds.music_1.setVolume(0.5f);
                    g.sounds.music_1.play();
                    g.is_music = true;
                    music_on.setColor(Color.WHITE);
                }
            }
        });

        /* --RHOMBUSES-- */
        r_music = new AdvSprite(
                g.atlas.createSprite("rhombus"),
                scroll_m.getX() + g.music_volume * (scroll_m.getX() + scroll_m.getWidth()) - 0.0035f * g.w,
                0.6175f * g.w,
                0.075f * g.w,
                0.075f * g.w
        );
        r_sound = new AdvSprite(
                g.atlas.createSprite("rhombus"),
                scroll_s.getX() + g.sound_volume * (scroll_s.getX() + scroll_s.getWidth()) - 0.0035f * g.w,
                0.3675f * g.w,
                0.075f * g.w,
                0.075f * g.w
        );

        scroll_m.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (g.is_sound) g.sounds.click.play(g.sound_volume);
                r_music.setPosition(
                        x - .5f*r_music.getWidth() + scroll_m.getX(),
                        r_music.getY()
                );
            }
        });

        scroll_s.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (g.is_sound) g.sounds.click.play(g.sound_volume);
                r_sound.setPosition(
                        x - .5f*r_sound.getWidth() + scroll_s.getX(),
                        r_sound.getY()
                );
            }
        });

        //
    }

    private void stage_addition () {
        //
        for (AdvSprite s : background) stage.addActor(s);
        stage.addActor(back.get());
        stage.addActor(lang_en);
        stage.addActor(lang_ru);
        stage.addActor(music_on);
        stage.addActor(sound_on);
        stage.addActor(scroll_s);
        stage.addActor(scroll_m);
        stage.addActor(r_music);
        stage.addActor(r_sound);
        stage.addActor(text_options);
        //
    }

    @Override
    public void resume() {
        if (g.is_music && !change_screen) {
            g.sounds.music_1.setLooping(true);
            g.sounds.music_1.setVolume(g.music_volume);
            g.sounds.music_1.play();
        }
    }

}
