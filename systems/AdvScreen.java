package ru.erked.potionsmaster.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import ru.erked.potionsmaster.GameStarter;

public class AdvScreen implements Screen {

    protected GameStarter g;
    protected Stage stage;

    protected boolean change_screen = false;
    protected Screen next_screen;

    public AdvScreen (GameStarter g) {
        this.g = g;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
    }

    // Values that will be saved by default
    private void save_data() {
        g.prefs.putFloat("music_volume", g.music_volume);
        g.prefs.putFloat("sound_volume", g.sound_volume);
        g.prefs.putBoolean("is_music_on", g.is_music);
        g.prefs.putBoolean("is_sound_on", g.is_sound);
        g.prefs.putInteger("language", g.lang);
        g.prefs.putInteger("week", g.week);
        g.prefs.putInteger("day", g.day);

        StringBuilder temp_1 = new StringBuilder();
        StringBuilder temp_2 = new StringBuilder();
        for (int i = 0; i < 5; ++i) temp_1.append(g.ing_added[i]).append(" ");
        for (int i = 0; i < 10; ++i) temp_2.append(g.ing_to_add[i]).append(" ");
        g.prefs.putString("ing_added", temp_1.toString());
        g.prefs.putString("ing_to_add", temp_2.toString());

        for (int i = 0; i < g.potions.size(); ++i) {
            StringBuilder temp = new StringBuilder();
            temp.append(g.potions.get(i).getBottle()).append(" ");
            for (int j = 0; j < 5; ++j) {
                temp.append(g.potions.get(i).getIngredients()[j]).append(" ");
            }
            g.prefs.putString("potion_" + i, temp.toString());
        }

        StringBuilder weeks = new StringBuilder();
        for (int i = 0; i < 10; ++i) {
            weeks.append(g.weeks[i]).append(" ");
        }
        g.prefs.putString("weeks", weeks.toString());

        StringBuilder trends = new StringBuilder();
        for (int i = 0; i < 50; ++i) {
            trends.append(g.trends[i]).append(" ");
        }
        g.prefs.putString("trends", trends.toString());

        g.prefs.flush();
    }

    protected void addDialog (Dialog dialog) {
        stage.addActor(dialog);
        stage.addActor(dialog.getText());
        for (Button b : dialog.getButtons()) stage.addActor(b.get());
    }

    protected boolean dialogsAreNotDisplayed () {
        for (Actor a : stage.getActors()) if (a instanceof Dialog) return false;
        return true;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        save_data();
        for (Music m : g.sounds.m_list) if (m.isPlaying()) m.pause();
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        save_data();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
