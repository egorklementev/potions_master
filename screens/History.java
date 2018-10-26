package ru.erked.potionsmaster.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import ru.erked.potionsmaster.systems.InGameText;
import ru.erked.potionsmaster.systems.TextLine;

public class History extends AdvScreen {

    /* Buttons */
    private ArrayList<Button> buttons;
    private Button close;

    /* Particles */

    /* Random */
    private float state_time = 0f;
    private final int RHOMBUSES = 10;
    private int s_p = -1;
    private int s_ing = -1;

    /* Sprites */
    private Animation<TextureRegion> background_anim;
    private ArrayList<AdvSprite> background;
    private ArrayList<AdvSprite> bottles;
    private ArrayList<AdvSprite> b_potions;
    private AdvSprite scroll;
    private AdvSprite s_bottle;
    private AdvSprite s_potion;
    private ArrayList<AdvSprite> ings;

    /* Text Lines */
    private TextLine title;
    private TextLine date;
    private TextLine day;
    private TextLine week;
    private TextLine t_ings;
    private TextLine ings_name;
    private InGameText potion_name;

    History (GameStarter game) {
        //
        super(game);
        //
    }

    @Override
    public void show () {
        //
        super.show();

        if (g.is_music) {
            g.sounds.music_2.setLooping(true);
            g.sounds.music_2.setVolume(g.music_volume);
            g.sounds.music_2.play();
        }

        button_initialization();
        texture_initialization();
        text_initialization();
        particles_initialization();
        stage_addition(); // Addition of actors to the stage

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

        state_time += delta;

        /* Transition btw screens */
        if (background.get(0).getColor().a == 0f) {
            if (change_screen) {
                this.dispose();
                g.setScreen(next_screen);
            } else {
                for (Actor act : stage.getActors()) act.addAction(Actions.alpha(1f, .5f));
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

        stage.act(delta);
        stage.draw();

        /* ESC listener */
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) { this.dispose(); Gdx.app.exit(); }
        //
    }

    private void button_initialization () {
        //
        buttons = new ArrayList<>();

        Button back = new Button(
                g,
                .725f * g.w,
                .025f * g.w,
                .25f * g.w,
                g.fonts.f_5.getFont(),
                g.textSystem.get("back_btn"),
                1,
                "back_btn"
        );
        back.get().addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if (!change_screen && !scroll.isVisible()) {
                    if (g.is_sound) g.sounds.click.play(g.sound_volume);
                    change_screen = true;
                    next_screen = new Lab(g);
                    back.get().setChecked(false);
                    for (Actor act : stage.getActors()) act.addAction(Actions.alpha(0f, .5f));
                } else {
                    back.get().setChecked(false);
                }
            }
        });

        float aspect_ratio = g.h / g.w;
        close = new Button(
                g,
                .8175f * g.w,
                .5f * g.h + .475f * g.w * aspect_ratio - .125f * g.w,
                .1f * g.w,
                g.fonts.f_0.getFont(),
                "x",
                2,
                "close_btn"
        );
        close.get().setVisible(false);
        close.get().addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if (g.is_sound) g.sounds.click.play(g.sound_volume);
                close.get().addAction(Actions.sequence(
                        Actions.alpha(0f, .5f),
                        Actions.visible(false)
                ));
                scroll.addAction(Actions.sequence(
                        Actions.alpha(0f, .5f),
                        Actions.visible(false)
                ));
                s_potion.addAction(Actions.sequence(
                        Actions.alpha(0f, .5f),
                        Actions.visible(false)
                ));
                s_bottle.addAction(Actions.sequence(
                        Actions.alpha(0f, .5f),
                        Actions.visible(false)
                ));
                potion_name.addAction(Actions.sequence(
                        Actions.alpha(0f, .5f),
                        Actions.visible(false)
                ));
                date.addAction(Actions.sequence(
                        Actions.alpha(0f, .5f),
                        Actions.visible(false)
                ));
                day.addAction(Actions.sequence(
                        Actions.alpha(0f, .5f),
                        Actions.visible(false)
                ));
                week.addAction(Actions.sequence(
                        Actions.alpha(0f, .5f),
                        Actions.visible(false)
                ));
                t_ings.addAction(Actions.sequence(
                        Actions.alpha(0f, .5f),
                        Actions.visible(false)
                ));
                for (int i = 0; i < 5; ++i) {
                    ings.get(i).addAction(Actions.sequence(
                            Actions.alpha(0f, .5f),
                            Actions.visible(false)
                    ));
                }
                ings_name.addAction(Actions.sequence(
                        Actions.alpha(0f, .5f),
                        Actions.visible(false)
                ));
                float time = .1f;
                float size = .04f * g.w;
                if (s_ing != -1) {
                    ings.get(s_ing).addAction(
                            Actions.parallel(
                                    Actions.sizeBy(-size, -size, time),
                                    Actions.moveBy(.5f * size, .5f * size, time)
                            )
                    );
                    s_ing = -1;
                }
                close.get().setChecked(false);
            }
        });

        buttons.add(back);
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
                g.textSystem.get("history_title"),
                .5f * (g.w - g.fonts.f_0S.getWidth(g.textSystem.get("history_title"))),
                .925f * g.h
        );

        potion_name = new InGameText(
                g,
                g.fonts.f_5Black,
                "temp",
                .5f * g.w,
                .375f * g.w,
                .5f * g.h + .475f * g.w * (g.h / g.w) - .4f * g.w,
                false
        );
        potion_name.setVisible(false);

        t_ings = new TextLine(
                g.fonts.f_5Black,
                g.textSystem.get("ingredients") + ":",
                .5f * (g.w - g.fonts.f_5Black.getWidth(g.textSystem.get("ingredients") + ":")),
                .5f * g.h + .475f * g.w * (g.h / g.w) - .625f * g.w
        );
        t_ings.setVisible(false);

        date = new TextLine(
                g.fonts.f_5Black,
                g.textSystem.get("date") + ":",
                .5f * (g.w - g.fonts.f_5Black.getWidth(g.textSystem.get("date") + ":")),
                .5f * g.h + .475f * g.w * (g.h / g.w) - .4f * g.w
        );
        date.setVisible(false);

        day = new TextLine(
                g.fonts.f_10Black,
                g.textSystem.get("day") + ":",
                .11f * g.w,
                .5f * g.h + .475f * g.w * (g.h / g.w) - .4f * g.w - 3f * g.fonts.f_10Black.getHeight("A")
        );
        day.setVisible(false);

        week = new TextLine(
                g.fonts.f_10Black,
                g.textSystem.get("week") + ":",
                .11f * g.w,
                .5f * g.h + .475f * g.w * (g.h / g.w) - .4f * g.w - 5f * g.fonts.f_10Black.getHeight("A")
        );
        week.setVisible(false);

        ings_name = new TextLine(
                g.fonts.f_10Black,
                "temp",
                .275f * g.w,
                .5f * g.h + .475f * g.w * (g.h / g.w) - .9f * g.w
        );
        ings_name.setVisible(false);
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

        float aspect_ratio = g.h / g.w;
        scroll = new AdvSprite(
                g.atlas.createSprite("scroll"),
                .025f * g.w,
                .5f * (g.h - .95f * g.w * aspect_ratio),
                .95f * g.w,
                .95f * g.w * aspect_ratio
        );
        scroll.setVisible(false);

        float margin = (g.w - .07f * 5f * g.h) / 12f;
        b_potions = new ArrayList<>();
        for (int i = 0; i < (g.week - 1) * 5 + (g.day - 1); ++i) {
            b_potions.add(new AdvSprite(
                    g.atlas.createSprite("bottle_p", g.potions.get(i).getBottle()),
                    margin * 4f + (i % 5) * (.07f * g.h + margin),
                    .8f * g.h - (i / 5) * (.07f * g.h + margin),
                    .07f * g.h,
                    .07f * g.h
            ));
            b_potions.get(i).setColor(
                    g.ings.colors[g.ings.list.get(g.potions.get(i).getIngredients()[0] - 1 ).getColor()]
            );
        }

        bottles = new ArrayList<>();
        for (int i = 0; i < (g.week - 1) * 5 + (g.day - 1); ++i) {
            bottles.add(new AdvSprite(
                    g.atlas.createSprite("bottle", g.potions.get(i).getBottle()),
                    margin * 4f + (i % 5) * (.07f * g.h + margin),
                    .8f * g.h - (i / 5) * (.07f * g.h + margin),
                    .07f * g.h,
                    .07f * g.h
            ));
            int finalI = i;
            bottles.get(i).addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    bottles.get(finalI).addAction(Actions.sequence(
                            Actions.parallel(
                                    Actions.sizeBy(-.05f*g.w, -.05f*g.w, .075f),
                                    Actions.moveBy(.025f*g.w, .025f*g.w, .075f)
                            ),
                            Actions.parallel(
                                    Actions.sizeBy(.05f*g.w, .05f*g.w, .075f),
                                    Actions.moveBy(-.025f*g.w, -.025f*g.w, .075f)
                            )
                    ));
                    b_potions.get(finalI).addAction(Actions.sequence(
                            Actions.parallel(
                                    Actions.sizeBy(-.05f*g.w, -.05f*g.w, .075f),
                                    Actions.moveBy(.025f*g.w, .025f*g.w, .075f)
                            ),
                            Actions.parallel(
                                    Actions.sizeBy(.05f*g.w, .05f*g.w, .075f),
                                    Actions.moveBy(-.025f*g.w, -.025f*g.w, .075f)
                            )
                    ));

                    scroll.addAction(Actions.sequence(
                            Actions.alpha(0f),
                            Actions.visible(true),
                            Actions.alpha(1f, .5f)
                    ));
                    close.get().addAction(Actions.sequence(
                            Actions.alpha(0f),
                            Actions.visible(true),
                            Actions.alpha(1f, .5f)
                    ));

                    s_potion.getSprite().setRegion(g.atlas.findRegion("bottle_p", g.potions.get(finalI).getBottle()));
                    s_potion.setColor(b_potions.get(finalI).getColor());
                    s_bottle.getSprite().setRegion(g.atlas.findRegion("bottle", g.potions.get(finalI).getBottle()));

                    s_potion.addAction(Actions.sequence(
                            Actions.alpha(0f),
                            Actions.visible(true),
                            Actions.alpha(1f, .5f)
                    ));
                    s_bottle.addAction(Actions.sequence(
                            Actions.alpha(0f),
                            Actions.visible(true),
                            Actions.alpha(1f, .5f)
                    ));

                    String temp = g.textSystem.get("taste_" + g.ings.list.get(g.potions.get(finalI).getIngredients()[1] - 1).getTaste()) + " " +
                            g.textSystem.get("race_" + g.ings.list.get(g.potions.get(finalI).getIngredients()[2] - 1).getRace()) + " " +
                            g.textSystem.get("potion_of") + " " +
                            g.textSystem.get("ing_" + g.ings.list.get(g.potions.get(finalI).getIngredients()[3] - 1).getId() +
                                    "_e_t" + (g.ings.list.get(g.potions.get(finalI).getIngredients()[4] - 1).isMale() ? "m" : "f")) + " " +
                            g.textSystem.get("ing_" + g.ings.list.get(g.potions.get(finalI).getIngredients()[4] - 1).getId() + "_e");
                    potion_name.setPlainText(g, temp, false);
                    potion_name.setY(s_bottle.getY() + .5f * s_bottle.getHeight() - .5f * potion_name.getHeight());
                    potion_name.updatePos(0f);
                    potion_name.addAction(Actions.sequence(
                            Actions.alpha(0f),
                            Actions.visible(true),
                            Actions.alpha(1f, .5f)
                    ));

                    t_ings.addAction(Actions.sequence(
                            Actions.alpha(0f),
                            Actions.visible(true),
                            Actions.alpha(1f, .5f)
                    ));
                    date.addAction(Actions.sequence(
                            Actions.alpha(0f),
                            Actions.visible(true),
                            Actions.alpha(1f, .5f)
                    ));
                    day.setText(g.textSystem.get("day") + ": " + ((finalI % 5) + 1));
                    day.addAction(Actions.sequence(
                            Actions.alpha(0f),
                            Actions.visible(true),
                            Actions.alpha(1f, .5f)
                    ));
                    week.setText(g.textSystem.get("week") + ": " + ((finalI / 5) + 1) +
                            " (" + g.textSystem.get("week_" + g.weeks[(finalI / 5)]) + ")");
                    week.addAction(Actions.sequence(
                            Actions.alpha(0f),
                            Actions.visible(true),
                            Actions.alpha(1f, .5f)
                    ));

                    for (int i = 0; i < 5; ++i) {
                        ings.get(i).getSprite().setRegion(
                                g.atlas.findRegion("ing", g.ings.list.get(g.potions.get(finalI).getIngredients()[i] - 1).getId())
                        );
                        ings.get(i).addAction(Actions.sequence(
                                Actions.alpha(0f),
                                Actions.visible(true),
                                Actions.alpha(1f, .5f)
                        ));
                    }

                    s_p = finalI;

                    if (g.is_sound) g.sounds.click.play(g.sound_volume);
                }
            });
        }

        s_potion = new AdvSprite(
                g.atlas.createSprite("bottle_p", 1),
                .1f * g.w,
                .5f * g.h + .475f * g.w * aspect_ratio - .35f * g.w,
                .25f * g.w,
                .25f * g.w
        );
        s_potion.setVisible(false);
        s_bottle = new AdvSprite(
                g.atlas.createSprite("bottle", 1),
                .1f * g.w,
                .5f * g.h + .475f * g.w * aspect_ratio - .35f * g.w,
                .25f * g.w,
                .25f * g.w
        );
        s_bottle.setVisible(false);

        ings = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            ings.add(new AdvSprite(
                    g.atlas.createSprite("ing", (i + 1)),
                    .0875f * g.w + i * .175f * g.w,
                    .5f * g.h + .475f * g.w * aspect_ratio - .825f * g.w,
                    .125f * g.w,
                    .125f * g.w
            ));
            int finalI = i;
            float time = .1f;
            float size = .04f * g.w;
            ings.get(i).addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (s_ing == -1 || s_ing != finalI) {

                        ings.get(finalI).addAction(
                                Actions.parallel(
                                        Actions.sizeBy(size, size, time),
                                        Actions.moveBy(-.5f * size, -.5f * size, time)
                                )
                        );

                        ings_name.setText(
                                g.textSystem.get(
                                        "ing_" + g.ings.list.get(
                                                g.potions.get(s_p).getIngredients()[finalI] - 1
                                        ).getId()
                                )
                        );
                        ings_name.setX(.5f * (g.w - ings_name.getFont().getWidth(ings_name.getText())));

                        if (s_ing != -1) {
                            ings.get(s_ing).addAction(
                                    Actions.parallel(
                                            Actions.sizeBy(-size, -size, time),
                                            Actions.moveBy(.5f * size, .5f * size, time)
                                    )
                            );
                            ings_name.addAction(Actions.sequence(
                                    Actions.visible(true),
                                    Actions.alpha(1f, .25f)
                            ));
                        } else {
                            ings_name.addAction(Actions.sequence(
                                    Actions.alpha(0f),
                                    Actions.visible(true),
                                    Actions.alpha(1f, .25f)
                            ));
                        }

                        s_ing = finalI;
                    } else {
                        ings_name.addAction(Actions.sequence(
                                Actions.alpha(0f, .25f),
                                Actions.visible(false)
                                ));

                        ings.get(s_ing).addAction(
                                Actions.parallel(
                                        Actions.sizeBy(-size, -size, time),
                                        Actions.moveBy(.5f * size, .5f * size, time)
                                )
                        );

                        s_ing = -1;
                    }
                }
            });
            ings.get(i).setVisible(false);
        }

        //
    }

    private void stage_addition () {
        //
        for (AdvSprite s : background) stage.addActor(s);
        stage.addActor(title);
        for (AdvSprite s : b_potions) stage.addActor(s);
        for (AdvSprite s : bottles) stage.addActor(s);
        for (Button b : buttons) stage.addActor(b.get());
        stage.addActor(scroll);
        stage.addActor(close.get());
        stage.addActor(s_potion);
        stage.addActor(s_bottle);
        for (AdvSprite s : ings) stage.addActor(s);
        stage.addActor(ings_name);
        stage.addActor(potion_name);
        stage.addActor(date);
        stage.addActor(day);
        stage.addActor(week);
        stage.addActor(t_ings);
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
