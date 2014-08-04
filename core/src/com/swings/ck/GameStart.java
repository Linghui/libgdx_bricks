package com.swings.ck;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class GameStart extends Game {

	private static GameStart self;
	private AssetManager manager;

	@Override
	public void create() {
		manager = new AssetManager();
		this.setScreen(new MainScreen(this, manager));
	}

	public static GameStart getInstance() {
		// System.out.println("GameStart getInstance");
		if (self == null) {
			self = new GameStart();
		}

		return self;
	}

}
