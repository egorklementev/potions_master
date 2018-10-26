package ru.erked.potionsmaster.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

import ru.erked.potionsmaster.GameStarter;
import ru.erked.potionsmaster.systems.AdvParticle;
import ru.erked.potionsmaster.systems.AdvScreen;
import ru.erked.potionsmaster.systems.AdvSprite;
import ru.erked.potionsmaster.systems.Button;
import ru.erked.potionsmaster.systems.Ingredient;
import ru.erked.potionsmaster.systems.TextLine;

public class WeekResults extends AdvScreen {

    /* Buttons */
    private ArrayList<Button> buttons;

    /* Groups */
    private ArrayList<Group> bar_groups;

    /* Particles */

    /* Random */
    private float state_time = 0f; // For all animations
    private final int RHOMBUSES = 10; // Background
    private float timer = 0f;
    private float[] percents;

    /* Sprites */
    private Animation<TextureRegion> background_anim;
    private ArrayList<AdvSprite> background;
    private ArrayList<AdvSprite> potions_b;
    private ArrayList<AdvSprite> potions_p;

    /* Text Lines */
    private TextLine title;
    private ArrayList<TextLine> t_percents;

    WeekResults (GameStarter game) {
        //
        super(game);
        //
    }

    @Override
    public void show () {
        //
        super.show();

        if (g.is_music && !change_screen) {
            g.sounds.music_2.setLooping(true);
            g.sounds.music_2.setVolume(g.music_volume);
            g.sounds.music_2.play();
        }

        percents_calculation();

        button_initialization();
        text_initialization();
        texture_initialization();
        particles_initialization();
        groups_initialization();
        stage_addition();

        /* For smooth transition btw screens */
        for (Actor act : stage.getActors())
            if (!(act instanceof TextLine && t_percents.contains(act)))
                act.getColor().set(act.getColor().r, act.getColor().g, act.getColor().b, 0f);
        //
    }

    @Override
    public void render (float delta) {
        //
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        state_time += delta;

        /* Transition btw screens */
        if (background.get(0).getColor().a == 0f) {
            if (change_screen) {
                this.dispose();
                g.setScreen(next_screen);
            } else {
                for (Actor act : stage.getActors())
                    if (!(act instanceof TextLine && t_percents.contains(act)))
                        act.addAction(Actions.alpha(1f, .5f));
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

        /* Animation */
        for (AdvSprite s : background) {s.getSprite().setRegion(background_anim.getKeyFrame(state_time, true));}

        /* Bar loading */
        float time = 5f;
        for (int i = 0; i < 5; ++i) {
            if (timer < time) {
                ((AdvParticle) bar_groups.get(i).getChildren().get(2)).start();
                timer += delta;
                ((AdvSprite) bar_groups.get(i).getChildren().get(1))
                        .setDrawPartially(0f, 0f, (timer / time) * percents[i], 1f, false, false);
                (bar_groups.get(i).getChildren().get(2)).setX(
                        (bar_groups.get(i).getChildren().get(1)).getX()
                                + (bar_groups.get(i).getChildren().get(1)).getWidth()
                                * (timer / time) * percents[i]);
            } else {
                ((AdvParticle)(bar_groups.get(i).getChildren().get(2))).stop();
            }
        }

        stage.act(delta);
        stage.draw();

        /* ESC listener */
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) { this.dispose(); Gdx.app.exit(); }

        //
    }

    private void button_initialization () {
        //
        buttons = new ArrayList<>();

        Button to_lab = new Button(
                g,
                .725f * g.w,
                .025f * g.w,
                .25f * g.w,
                g.fonts.f_5.getFont(),
                g.textSystem.get("back_btn"),
                1,
                "back_btn"
        );
        to_lab.get().addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if (!change_screen) {
                    if (g.is_sound) g.sounds.click.play(g.sound_volume);
                    if (g.sounds.music_2.isPlaying()) g.sounds.music_2.stop();
                    change_screen = true;
                    next_screen = new Lab(g);
                    to_lab.get().setChecked(false);
                    for (Actor act : stage.getActors()) act.addAction(Actions.alpha(0f, .5f));
                }
            }
        });

        buttons.add(to_lab);
        //
    }
    private void groups_initialization () {
        //
        bar_groups = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {

            AdvSprite bar = new AdvSprite(
                    g.atlas.createSprite("bar"),
                    0f,
                    0f,
                    .7f * g.w,
                    .04375f * g.w
            );

            AdvSprite bar_fill = new AdvSprite(
                    g.atlas.createSprite("bar_fill"),
                    0f,
                    0f,
                    .7f * g.w,
                    .04375f * g.w
            );
            bar_fill.setDrawPartially(0f, 0f, (timer / 1f) * 1f, 1f, false, false);

            AdvParticle bar_parts = new AdvParticle(
                    g,
                    "bar",
                    0f,
                    .04375f * g.w * .5f,
                    .021875f * g.h,
                    AdvParticle.Type.LINE
            );
            bar_parts.stop();

            bar_groups.add(new Group());
            bar_groups.get(i).addActor(bar);
            bar_groups.get(i).addActor(bar_fill);
            bar_groups.get(i).addActor(bar_parts);

            bar_groups.get(i).setPosition(
                    .25f * g.w,
                    .785f * g.h - i * (.25f * g.w)
            );
        }
        //
    }
    private void particles_initialization () {
        //
        //
    }
    private void text_initialization () {
        //

        title = new TextLine(
                g.fonts.f_0S,
                g.textSystem.get("week_results_title"),
                .5f * (g.w - g.fonts.f_0S.getWidth(g.textSystem.get("week_results_title"))),
                .975f * g.h
        );

        t_percents = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            t_percents.add(new TextLine(
                    g.fonts.f_0S,
                    (int)(percents[i] * 100f) + " %",
                    .55f * g.w,
                    .785f * g.h - i * (.25f * g.w) + .5f * .04375f * g.w - .1f * g.w +
                            g.fonts.f_0S.getHeight("A")
            ));
            t_percents.get(i).addAction(
                    Actions.sequence(
                            Actions.alpha(0f),
                            Actions.delay(2f),
                            Actions.alpha(1f, 1f)
                            )
            );
        }

        //
    }
    private void texture_initialization () {
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
                .09375f,
                g.atlas.findRegions("background"),
                Animation.PlayMode.LOOP_PINGPONG);

        potions_b = new ArrayList<>();
        potions_p = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            potions_b.add(new AdvSprite(
                    g.atlas.createSprite("bottle",
                            g.potions.get(g.potions.size() - (5 - i)).getBottle()),
                    .025f * g.w,
                    .785f * g.h - i * (.25f * g.w) + .5f * .04375f * g.w - .1f * g.w,
                    .2f * g.w,
                    .2f * g.w
            ));
            potions_p.add(new AdvSprite(
                    g.atlas.createSprite("bottle_p",
                            g.potions.get(g.potions.size() - (5 - i)).getBottle()),
                    .025f * g.w,
                    .785f * g.h - i * (.25f * g.w) + .5f * .04375f * g.w - .1f * g.w,
                    .2f * g.w,
                    .2f * g.w
            ));
            potions_p.get(i).setColor(
                    g.ings.colors[(g.ings.list.get(g.potions.get(g.potions.size() - (5 - i)).getIngredients()[0] - 1).getColor())]
            );
        }

        //
    }

    private void percents_calculation () {
        //
        percents = new float[5];
        for (int i = 4; i >= 0; --i) {
            percents[i] = .5f;

            for (int j = 0; j < 5; ++j) {
                Ingredient ing = g.ings.list.get(
                        g.potions.get(
                                g.potions.size() - (i + 1)
                        ).getIngredients()[j] - 1
                );

                // Weeks
                if (ing.getWeeks()[0] == g.weeks[g.week - 1] ||
                        ing.getWeeks()[1] == g.weeks[g.week - 1]) {
                    percents[i] += .1f;
                }

                // Trends
                if (ing.getTrend() == g.trends[g.day - 1]) {
                    percents[i] -= .1f;
                }

                // Combinations (good and bad)
                for (int k = 0; k < 5; ++k) {
                    if (k != j) {
                        Ingredient comb = g.ings.list.get(
                                g.potions.get(
                                        g.potions.size() - (i + 1)
                                ).getIngredients()[k] - 1
                        );

                        for (int l = 0; l < comb.getComb().length; ++l) {
                            if (comb.getComb()[l] == ing.getId()) {
                                percents[i] += .05f;
                                break;
                            }
                        }

                        for (int l = 0; l < comb.getBadComb().length; ++l) {
                            if (comb.getBadComb()[l] == ing.getId()) {
                                percents[i] -= .05f;
                                break;
                            }
                        }

                    }
                }
            }

            if (percents[i] > 1f) percents[i] = 1f;
            if (percents[i] < 0f) percents[i] = 0f;

        }
        //
    }
    private void stage_addition () {
        //
        for (AdvSprite s : background) stage.addActor(s);
        stage.addActor(title);
        for (AdvSprite s : potions_p) stage.addActor(s);
        for (AdvSprite s : potions_b) stage.addActor(s);
        for (TextLine t : t_percents) stage.addActor(t);
        for (Group g : bar_groups) stage.addActor(g);
        for (Button b : buttons) stage.addActor(b.get());
        //
    }

    @Override
    public void resume() {
        if (g.is_music && !change_screen) {
            g.sounds.music_2.setLooping(true);
            g.sounds.music_2.setVolume(g.music_volume);
            g.sounds.music_2.play();
        }
    }

}
