package ru.erked.potionsmaster.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

import ru.erked.potionsmaster.GameStarter;
import ru.erked.potionsmaster.systems.AdvParticle;
import ru.erked.potionsmaster.systems.AdvScreen;
import ru.erked.potionsmaster.systems.AdvSprite;
import ru.erked.potionsmaster.systems.Button;
import ru.erked.potionsmaster.systems.InGameText;
import ru.erked.potionsmaster.systems.Potion;
import ru.erked.potionsmaster.systems.TextLine;
import ru.erked.potionsmaster.systems.TextNotification;

public class Lab extends AdvScreen {

    /* Buttons */
    private ArrayList<Button> buttons;

    /* Particles */
    private AdvParticle potion_parts;

    /* Random */
    private float state_time = 0f; // For all animations
    private final int RHOMBUSES = 10; // Background
    private int selected_ing_to_add = -1;
    private int selected_ing_added = -1;
    private int added_num;
    private boolean is_adding = false;
    private boolean was_added = false;

    /* Sprites */
    private Animation<TextureRegion> background_anim;
    private ArrayList<AdvSprite> background;
    private AdvSprite kettle;
    private AdvSprite potion;
    private AdvSprite library;
    private AdvSprite rating;
    private AdvSprite history;
    private ArrayList<AdvSprite> ing_added;
    private ArrayList<AdvSprite> ing_to_add;
    private AdvSprite lighting;
    private AdvSprite bottle;
    private AdvSprite bottle_potion;

    /* Text Lines */
    private TextLine date;
    private TextLine title;
    private TextLine t_ing_added;
    private TextLine t_ing_add_to;
    private InGameText potion_name;
    private ArrayList<TextNotification> notifs;

    Lab (GameStarter game) {
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

        for (int i = 0; i < 5; ++i)
            if (g.ing_added[i] != 0)
                added_num++;

        button_initialization();
        text_initialization();
        texture_initialization();
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

        /* Forbid to do anything while ingredient is adding */
        is_adding = false;
        for (int i = 0; i < 10; ++i) {
            if (ing_to_add.get(i) != null &&
                    ing_to_add.get(i).hasActions()) {
                is_adding = true;
            }
        }
        for (int i = 0; i < 5; ++i) {
            if (ing_added.get(i) != null &&
                    ing_added.get(i).hasActions()) {
                is_adding = true;
            }
        }

        if (added_num == 5 && !is_adding && !change_screen && !was_added) {

            was_added = true;

            // Remove added ingredients
            for (int i = 0; i < 5; ++i) {
                ing_added.get(i).addAction(
                        Actions.parallel(
                                Actions.sizeTo(0f, 0f, 1f),
                                Actions.moveTo(
                                        potion.getX() + .5f*potion.getWidth(),
                                        potion.getY() + .75f*potion.getHeight(),
                                        1f
                                )
                        )
                );
                ing_added.get(i).clearListeners();
                ing_added.set(i, null);
            }

            // Lighting
            lighting.setVisible(true);
            lighting.addAction(Actions.alpha(.75f));
            lighting.addAction(Actions.delay(1f));
            lighting.addAction(Actions.parallel(
                    Actions.sizeBy(g.w, g.w, .5f),
                    Actions.moveBy(-.5f * g.w, -.5f * g.w, .5f),
                    new Action() {
                        @Override
                        public boolean act(float delta) {
                            lighting.setOrigin(
                                    .5f*lighting.getWidth(),
                                    .5f*lighting.getHeight()
                            );
                            return true;
                        }
                    }
            ));

            int bottle_index = MathUtils.random(1, 8);
            bottle.getSprite().setRegion(g.atlas.findRegion("bottle", bottle_index));
            bottle_potion.getSprite().setRegion(g.atlas.findRegion("bottle_p", bottle_index));

            bottle.addAction(Actions.parallel(
                    Actions.sizeBy(.3f * g.w, .3f * g.w, .5f),
                    Actions.moveBy(-.15f * g.w, -.15f * g.w, .5f)
            ));
            bottle_potion.addAction(Actions.parallel(
                    Actions.sizeBy(.3f * g.w, .3f * g.w, .5f),
                    Actions.moveBy(-.15f * g.w, -.15f * g.w, .5f)
            ));

            // Ingredients
            Color color = g.ings.colors[g.ings.list.get(g.ing_added[0] - 1).getColor()];
            bottle_potion.setColor(color);

            potion_name.setCenteredText(
                    g,
                    new String[]{
                            g.textSystem.get("taste_" + g.ings.list.get(g.ing_added[1] - 1).getTaste()),
                            g.textSystem.get("race_" + g.ings.list.get(g.ing_added[2] - 1).getRace()),
                            g.textSystem.get("potion_of"),
                            g.textSystem.get("ing_" + g.ings.list.get(g.ing_added[3] - 1).getId() +
                                    "_e_t" + (g.ings.list.get(g.ing_added[4] - 1).isMale() ? "m" : "f")),
                            g.textSystem.get("ing_" + g.ings.list.get(g.ing_added[4] - 1).getId() + "_e")
                    },
                    new int[]{0,1,2,3,4},
                    false
            );
            potion_name.setX(.5f * (g.w - potion_name.getWidth()));
            potion_name.addAction(Actions.alpha(0f));
            potion_name.addAction(Actions.visible(true));
            potion_name.addAction(Actions.alpha(1f, 2f));

            for (Actor act : stage.getActors()) {
                if (act != bottle && act != bottle_potion && act != lighting && act != potion_name
                        && act != t_ing_add_to && act != t_ing_added) {
                    act.addAction(Actions.color(new Color(.25f, .25f, .25f, 1f), 1f));
                }
            }

            g.potions.add(new Potion(bottle_index, g.ing_added));

            for (int i = 0; i < 5; ++i)
                g.ing_added[i] = 0;
        }

        if (added_num == 5 &&
                !potion_name.hasActions() &&
                Gdx.input.justTouched() &&
                !is_adding &&
                !change_screen &&
                was_added) {
            if (g.day == 5) {
                g.week++;
                g.day = 1;
                change_screen = true;
                next_screen = new WeekResults(g);
            } else {
                g.day++;
                change_screen = true;
                next_screen = new Lab(g);
            }
            for (Actor act : stage.getActors()) act.addAction(Actions.alpha(0f, .5f));
        }

        /* Animation */
        for (AdvSprite s : background) {s.getSprite().setRegion(background_anim.getKeyFrame(state_time, true));}

        stage.act(delta);
        stage.draw();

        /* Notifications' deletion*/
        for (int i = notifs.size() - 1; i >= 0; --i)
            if (notifs.get(i).isOver()) stage.getActors().removeValue(notifs.remove(i), true);

        /* ESC listener */
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) { this.dispose(); Gdx.app.exit(); }
        //
    }

    private void button_initialization () {
        //
        buttons = new ArrayList<>();

        float y_coord = (.175f * g.h + .175f * g.w)
                + .5f * (((.65f * g.h) - (.175f * g.h + .175f * g.w)) - .125f * g.w);
        Button add_ing = new Button(
                g,
                .025f * g.w,
                y_coord,
                .125f * g.w,
                g.fonts.f_0.getFont(),
                "+",
                2,
                "add_ing_btn"
        );
        add_ing.get().addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if (!is_adding && !change_screen && added_num < 5 && selected_ing_to_add != -1) {
                    if (g.is_sound) g.sounds.click.play(g.sound_volume); // Click sound
                    ing_to_add.get(selected_ing_to_add).addAction(
                            Actions.parallel(
                                    Actions.moveTo(
                                            .0208f * g.w + added_num * .1958f * g.w,
                                            .175f * g.h,
                                            .75f
                                    ),
                                    Actions.sizeBy(
                                            -.04f * g.w,
                                            -.04f * g.w,
                                            .75f
                                    )
                            )
                    );
                    ing_added.set(added_num, ing_to_add.get(selected_ing_to_add));
                    ing_to_add.set(selected_ing_to_add, null);
                    g.ing_added[added_num] = g.ing_to_add[selected_ing_to_add];
                    g.ing_to_add[selected_ing_to_add] = 0;
                    float time = .1f;
                    float size = .04f * g.w;
                    ing_added.get(added_num).clearListeners();
                    int temp = added_num;
                    ing_added.get(added_num).addListener(new ClickListener(){
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            if (!is_adding) {
                                if (selected_ing_added == -1 || selected_ing_added != temp) {
                                    ing_added.get(temp).addAction(
                                            Actions.parallel(
                                                    Actions.sizeBy(size, size, time),
                                                    Actions.moveBy(-.5f * size, -.5f * size, time)
                                            )
                                    );
                                    if (selected_ing_added != -1) {
                                        ing_added.get(selected_ing_added).addAction(
                                                Actions.parallel(
                                                        Actions.sizeBy(-size, -size, time),
                                                        Actions.moveBy(.5f * size, .5f * size, time)
                                                )
                                        );
                                    }
                                    if (selected_ing_to_add != -1) {
                                        ing_to_add.get(selected_ing_to_add).addAction(
                                                Actions.parallel(
                                                        Actions.sizeBy(-size, -size, time),
                                                        Actions.moveBy(.5f * size, .5f * size, time)
                                                )
                                        );
                                    }
                                    selected_ing_to_add = -1;
                                    selected_ing_added = temp;
                                    t_ing_add_to.addAction(Actions.alpha(0f, .25f));
                                    t_ing_added.setText(g.textSystem.get("ing_" + g.ing_added[selected_ing_added]));
                                    t_ing_added.setX(.5f * (g.w - t_ing_added.getFont().getWidth(g.textSystem.get("ing_" + g.ing_added[selected_ing_added]))));
                                    t_ing_added.addAction(Actions.alpha(1f, .25f));
                                } else {
                                    ing_added.get(selected_ing_added).addAction(
                                            Actions.parallel(
                                                    Actions.sizeBy(-size, -size, time),
                                                    Actions.moveBy(.5f * size, .5f * size, time)
                                            )
                                    );
                                    selected_ing_added = -1;
                                    selected_ing_to_add = -1;
                                    t_ing_added.addAction(Actions.alpha(0f, .25f));
                                }
                            }
                        }
                    });
                    selected_ing_to_add = -1;
                    added_num++;

                    t_ing_add_to.addAction(Actions.alpha(0f, .25f));

                    add_ing.get().setChecked(false);
                } else if (selected_ing_to_add == -1) {
                    spawnNotification(g.textSystem.get("select_ingredient"), 2f);
                    add_ing.get().setChecked(false);
                } else {
                    add_ing.get().setChecked(false);
                }
            }
        });

        Button info = new Button(
                g,
                .85f * g.w,
                y_coord,
                .125f * g.w,
                g.fonts.f_0.getFont(),
                "i",
                2,
                "add_ing_btn"
        );
        info.get().addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if (!change_screen) {
                    if (g.is_sound) g.sounds.click.play(g.sound_volume); // Click sound
                    // TODO: info about ingredient
                }
            }
        });

        Button menu = new Button(
                g,
                .725f * g.w,
                .025f * g.w,
                .25f * g.w,
                g.fonts.f_5.getFont(),
                g.textSystem.get("menu_btn"),
                1,
                "menu_btn"
        );
        menu.get().addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if (!change_screen) {
                    if (g.is_sound) g.sounds.click.play(g.sound_volume);
                    if (g.sounds.music_2.isPlaying()) g.sounds.music_2.stop();
                    change_screen = true;
                    next_screen = new Menu(g);
                    menu.get().setChecked(false);
                    for (Actor act : stage.getActors()) act.addAction(Actions.alpha(0f, .5f));
                }
            }
        });

        buttons.add(menu);
        buttons.add(add_ing);
        buttons.add(info);
        //
    }
    private void particles_initialization () {
        //
        potion_parts = new AdvParticle(
                g,
                "potion",
                .5f * g.w,
                kettle.getY() + .7f * kettle.getHeight(),
                .7f * kettle.getWidth(),
                AdvParticle.Type.CIRCLE
        );
        //
    }
    private void text_initialization () {
        //
        notifs = new ArrayList<>();

        title = new TextLine(
                g.fonts.f_0S,
                g.textSystem.get("lab_title"),
                .5f * (g.w - g.fonts.f_0S.getWidth(g.textSystem.get("lab_title"))),
                .975f * g.h
        );

        String string_date = g.textSystem.get("day") + ": " + g.day + "  " + g.textSystem.get("week") + ": " + g.week;
        date = new TextLine(
                g.fonts.f_5S,
                string_date,
                .5f * (g.w - g.fonts.f_5S.getWidth(string_date)),
                .975f * g.h - 3f * g.fonts.f_5S.getHeight("A")
        );

        t_ing_add_to = new TextLine(
                g.fonts.f_10S,
                "",
                .5f * (g.w - g.fonts.f_10S.getWidth("")),
                .6f * g.h
        );
        t_ing_add_to.addAction(Actions.alpha(0f));

        t_ing_added = new TextLine(
                g.fonts.f_10S,
                "",
                .5f * (g.w - g.fonts.f_10S.getWidth("")),
                .325f * g.h
        );
        t_ing_added.addAction(Actions.alpha(0f));

        potion_name = new InGameText(
                g,
                g.fonts.f_0S,
                new String[]{"ingredient","ingredient","ingredient","ingredient","ingredient"},
                new int[]{0,1,2,3,4},
                false
        );
        potion_name.setVisible(false);
        potion_name.setY(.325f * g.h - potion_name.getHeight());
        potion_name.updatePos(0f);
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

        kettle = new AdvSprite(
                g.atlas.createSprite("kettle"),
                .15f * g.w,
                .125f * g.h,
                .7f * g.w,
                .7f * g.w
        );

        potion = new AdvSprite(
                g.atlas.createSprite("kettle_potion"),
                .15f * g.w,
                .125f * g.h,
                .7f * g.w,
                .7f * g.w
        );
        potion.setColor(Color.SKY);

        library = new AdvSprite(
                g.atlas.createSprite("library"),
                .025f*g.w,
                .025f*g.w,
                .15f*g.w,
                .15f*g.w
        );

        rating = new AdvSprite(
                g.atlas.createSprite("rating"),
                .2f*g.w,
                .025f*g.w,
                .15f*g.w,
                .15f*g.w
        );
        rating.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!change_screen) {
                    rating.addAction(Actions.sequence(
                            Actions.parallel(
                                    Actions.sizeBy(-.05f*g.w, -.05f*g.w, .075f),
                                    Actions.moveBy(.025f*g.w, .025f*g.w, .075f)
                            ),
                            Actions.parallel(
                                    Actions.sizeBy(.05f*g.w, .05f*g.w, .075f),
                                    Actions.moveBy(-.025f*g.w, -.025f*g.w, .075f)
                            )
                    ));

                    //if (g.is_sound) g.sounds.click.play(g.sound_volume);
                    //change_screen = true;
                    //next_screen = new WeekResults(g);
                    //for (Actor act : stage.getActors()) act.addAction(Actions.alpha(0f, .5f));
                }
            }
        });

        history = new AdvSprite(
                g.atlas.createSprite("history"),
                .375f*g.w,
                .025f*g.w,
                .15f*g.w,
                .15f*g.w
        );
        history.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!change_screen) {
                    history.addAction(Actions.sequence(
                            Actions.parallel(
                                    Actions.sizeBy(-.05f*g.w, -.05f*g.w, .075f),
                                    Actions.moveBy(.025f*g.w, .025f*g.w, .075f)
                            ),
                            Actions.parallel(
                                    Actions.sizeBy(.05f*g.w, .05f*g.w, .075f),
                                    Actions.moveBy(-.025f*g.w, -.025f*g.w, .075f)
                            )
                    ));

                    if (g.is_sound) g.sounds.click.play(g.sound_volume);
                    change_screen = true;
                    next_screen = new History(g);
                    for (Actor act : stage.getActors()) act.addAction(Actions.alpha(0f, .5f));
                }
            }
        });

        // TODO: normal ingredients' delivery
        for (int i = 0; i < 10; ++i) {
            g.ing_to_add[i] = i + 1;
        }

        ing_to_add = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            if (g.ing_to_add[i] != 0) {
                ing_to_add.add(new AdvSprite(
                        g.atlas.createSprite("ing", g.ing_to_add[i]),
                        .0208f * g.w + (i % 5) * .1958f * g.w,
                        .65f * g.h + (i / 5) * .1958f * g.w,
                        .175f * g.w,
                        .175f * g.w
                ));
                int finalI = i;
                float time = .1f;
                float size = .04f * g.w;
                ing_to_add.get(i).addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (!is_adding) {
                            if (selected_ing_to_add == -1 || selected_ing_to_add != finalI) {
                                ing_to_add.get(finalI).addAction(
                                        Actions.parallel(
                                                Actions.sizeBy(size, size, time),
                                                Actions.moveBy(-.5f * size, -.5f * size, time)
                                        )
                                );
                                if (selected_ing_to_add != -1) {
                                    ing_to_add.get(selected_ing_to_add).addAction(
                                            Actions.parallel(
                                                    Actions.sizeBy(-size, -size, time),
                                                    Actions.moveBy(.5f * size, .5f * size, time)
                                            )
                                    );
                                }
                                if (selected_ing_added != -1) {
                                    ing_added.get(selected_ing_added).addAction(
                                            Actions.parallel(
                                                    Actions.sizeBy(-size, -size, time),
                                                    Actions.moveBy(.5f * size, .5f * size, time)
                                            )
                                    );
                                }
                                selected_ing_added = -1;
                                selected_ing_to_add = finalI;
                                t_ing_added.addAction(Actions.alpha(0f, .25f));
                                t_ing_add_to.setText(g.textSystem.get("ing_" + g.ing_to_add[selected_ing_to_add]));
                                t_ing_add_to.setX(.5f * (g.w - t_ing_add_to.getFont().getWidth(g.textSystem.get("ing_" + g.ing_to_add[selected_ing_to_add]))));
                                t_ing_add_to.addAction(Actions.alpha(1f, .25f));
                            } else {
                                ing_to_add.get(selected_ing_to_add).addAction(
                                        Actions.parallel(
                                                Actions.sizeBy(-size, -size, time),
                                                Actions.moveBy(.5f * size, .5f * size, time)
                                        )
                                );
                                selected_ing_added = -1;
                                selected_ing_to_add = -1;
                                t_ing_add_to.addAction(Actions.alpha(0f, .25f));
                            }
                        }
                    }
                });
            } else  {
                ing_to_add.add(null);
            }
        }

        ing_added = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            if (g.ing_added[i] != 0) {
                ing_added.add(new AdvSprite(
                        g.atlas.createSprite("ing", g.ing_added[i]),
                        .0208f * g.w + i * .1958f * g.w,
                        .175f * g.h,
                        .175f * g.w,
                        .175f * g.w
                ));
                int finalI = i;
                float time = .1f;
                float size = .04f * g.w;
                ing_added.get(i).addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (!is_adding) {
                            if (selected_ing_added == -1 || selected_ing_added != finalI) {
                                ing_added.get(finalI).addAction(
                                        Actions.parallel(
                                                Actions.sizeBy(size, size, time),
                                                Actions.moveBy(-.5f * size, -.5f * size, time)
                                        )
                                );
                                if (selected_ing_added != -1) {
                                    ing_added.get(selected_ing_added).addAction(
                                            Actions.parallel(
                                                    Actions.sizeBy(-size, -size, time),
                                                    Actions.moveBy(.5f * size, .5f * size, time)
                                            )
                                    );
                                }
                                if (selected_ing_to_add != -1) {
                                    ing_to_add.get(selected_ing_to_add).addAction(
                                            Actions.parallel(
                                                    Actions.sizeBy(-size, -size, time),
                                                    Actions.moveBy(.5f * size, .5f * size, time)
                                            )
                                    );
                                }
                                selected_ing_to_add = -1;
                                selected_ing_added = finalI;
                                t_ing_add_to.addAction(Actions.alpha(0f, .25f));
                                t_ing_added.setText(g.textSystem.get("ing_" + g.ing_added[selected_ing_added]));
                                t_ing_added.setX(.5f * (g.w - t_ing_added.getFont().getWidth(g.textSystem.get("ing_" + g.ing_added[selected_ing_added]))));
                                t_ing_added.addAction(Actions.alpha(1f, .25f));
                            } else {
                                ing_added.get(selected_ing_added).addAction(
                                        Actions.parallel(
                                                Actions.sizeBy(-size, -size, time),
                                                Actions.moveBy(.5f * size, .5f * size, time)
                                        )
                                );
                                selected_ing_added = -1;
                                selected_ing_to_add = -1;
                                t_ing_added.addAction(Actions.alpha(0f, .25f));
                            }
                        }
                    }
                });
            } else {
                ing_added.add(null);
            }
        }

        lighting = new AdvSprite(
                g.atlas.createSprite("lighting"),
                .5f * g.w,
                .5f * g.h,
                0f,
                0f
        );
        lighting.addAction(Actions.forever(
                Actions.rotateBy(36f, 1f)
        ));
        lighting.setVisible(false);

        bottle_potion = new AdvSprite(
                g.atlas.createSprite("bottle_p", 1),
                .5f * g.w,
                .5f * g.h,
                0f,
                0f
        );
        bottle = new AdvSprite(
                g.atlas.createSprite("bottle", 1),
                .5f * g.w,
                .5f * g.h,
                0f,
                0f
        );

        //
    }

    private void spawnNotification(String text, float lifetime) {
        //
        notifs.add(new TextNotification(
                g.fonts.f_0S,
                text,
                .5f * (g.w - g.fonts.f_0S.getWidth(text)),
                .5f * g.h,
                lifetime
        ));
        stage.addActor(notifs.get(notifs.size() - 1));
        //
    }
    private void stage_addition () {
        //
        for (AdvSprite s : background) stage.addActor(s);
        stage.addActor(kettle);
        stage.addActor(potion);
        stage.addActor(potion_parts);
        stage.addActor(library);
        stage.addActor(rating);
        stage.addActor(history);
        stage.addActor(title);
        stage.addActor(date);
        stage.addActor(t_ing_added);
        stage.addActor(t_ing_add_to);
        for (AdvSprite s : ing_added) if (s != null) stage.addActor(s);
        for (AdvSprite s : ing_to_add) if (s != null) stage.addActor(s);
        for (Button b : buttons) stage.addActor(b.get());
        stage.addActor(lighting);
        stage.addActor(bottle_potion);
        stage.addActor(bottle);
        stage.addActor(potion_name);
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
