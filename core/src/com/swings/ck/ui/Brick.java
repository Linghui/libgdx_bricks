package com.swings.ck.ui;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Brick extends Group {

	private int num;
	private Image numI;

	public Brick(Skin skin, TextureAtlas numAtlas, int num) {
		this.num = num;

		Image back = new Image(skin.getDrawable("baikuangbj"));
		back.setWidth(40);
		back.setHeight(40);
		this.addActor(back);

		this.setWidth(back.getWidth());
		this.setHeight(back.getHeight());

		numI = new Image(numAtlas.findRegion("" + num));
		numI.setX((this.getWidth() - numI.getWidth()) / 2);
		numI.setY((this.getHeight() - numI.getHeight()) / 2);
		this.addActor(numI);
	}

	public void hideNum() {
		numI.setVisible(false);
	}

	public int getNum() {
		return num;
	}
}
