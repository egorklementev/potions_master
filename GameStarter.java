package ru.erked.potionsmaster;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.ArrayList;

import ru.erked.potionsmaster.screens.Preview;
import ru.erked.potionsmaster.systems.Potion;
import ru.erked.potionsmaster.systems.TextSystem;
import ru.erked.potionsmaster.utils.Fonts;
import ru.erked.potionsmaster.utils.Ingredients;
import ru.erked.potionsmaster.utils.Sounds;


public class GameStarter extends Game {

	public int lang;
	public TextureAtlas atlas;
	public TextSystem textSystem;
	public Sounds sounds;
	public Fonts fonts;
	public Preferences prefs;
	public float w;
	public float h;

	/* Values to memorise */
	public boolean is_sound = true;
	public boolean is_music = true;

	public float music_volume = 1f; // From 0f to 1f
	public float sound_volume = 1f; // From 0f to 1f

	public int day = 1;
	public int week = 1;
	public int money = 0;
	public int[] weeks;
	public int[] trends;

	public Ingredients ings;
	public ArrayList<Potion> potions;

	public int[] ing_added;
	public int[] ing_to_add;

	public GameStarter(int lang) { this.lang = lang; }

	@Override
	public void create () {
		setScreen(new Preview(this));
	}

	@Override
	public void dispose () {}
}
