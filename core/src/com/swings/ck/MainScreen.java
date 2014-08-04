package com.swings.ck;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.SnapshotArray;
import com.swings.ck.ui.Brick;

public class MainScreen extends CoreScreen {

	private AssetManager manager;
	private boolean srcLoadDone;
	private Skin uiskin;
	private String TAG = "MainScreen";
	private Group numG;
	private Image rb;

	public MainScreen(Game game, AssetManager manager) {
		super(game);

		this.manager = manager;

	}

	@Override
	public void show() {
		super.show();
		manager.load("uiskin.json", Skin.class);
		manager.load("drop.atlas", TextureAtlas.class);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		if (!manager.update()) {

		}
		// if (manager.isLoaded(CommonRes.resBase + "logo.png")
		// && this.logoLoadDone == false) {
		// this.logoLoadDone = true;
		// init();
		// return;
		// }

		if (manager.update() && this.srcLoadDone == false) {
			srcLoadDone = true;
			selfInit();
		}

		selfRender(delta);

	}

	private void selfRender(float delta) {

	}

	private void selfInit() {
		uiskin = manager.get("uiskin.json", Skin.class);

		numG = new Group();
		numG.setWidth(this.getRootWidth());
		numG.setHeight(this.getRootHeight());
		this.addActorBottom(numG);

		rb = new Image(uiskin.getDrawable("baikuangbj"));
		rb.setX(10);
		rb.setY(10);
		this.addActorBottom(rb);
		rb.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				nextLevel();
			}
		});

	}

	private int level = 1;
	private List<Integer> runningRandNums;

	protected void nextLevel() {

		rb.setVisible(false);
		restart(level);
		level++;
	}

	private void restart(int level) {

		this.catchNumCount = 0;
		runningRandNums = getBrickNumByLevel(level);

		numG.clearChildren();
		numG.addAction(Actions.sequence(Actions.delay(2f), new Action() {

			@Override
			public boolean act(float delta) {
				hideNum();
				return true;
			}

		}));

		TextureAtlas atlas = manager.get("drop.atlas", TextureAtlas.class);

		int scropW = (int) this.getRootWidth();
		int scropH = (int) this.getRootHeight();

		Gdx.app.log(TAG, "scropW " + scropW);
		Gdx.app.log(TAG, "scropH " + scropH);

		List<Integer> xAdded = new ArrayList<Integer>();
		List<Integer> yAdded = new ArrayList<Integer>();

		for (int index = 0; index < runningRandNums.size(); index++) {
			final Brick b = new Brick(uiskin, atlas, runningRandNums.get(index));

			Random rand = new Random();
			int x = rand.nextInt(scropW - (int) (b.getWidth() * 2));
			int y = rand.nextInt(scropH - (int) (b.getHeight() * 2));

			// make sure two bricks do not overlap
			if (index != 1) {
				while (!isDisEnough(x, y, xAdded, yAdded, (int) (b.getWidth()))) {
					x = rand.nextInt(scropW - (int) (b.getWidth() * 2));
					y = rand.nextInt(scropH - (int) (b.getHeight() * 2));
				}
			}

			b.setX(x);
			b.setY(y);
			numG.addActor(b);
			b.addListener(new ClickListener() {
				public void clicked(InputEvent event, float x, float y) {
					clickNum(b);
				}
			});

			Gdx.app.log(TAG, "x " + x);
			Gdx.app.log(TAG, "y " + y);

			xAdded.add(x);
			yAdded.add(y);
		}
	}

	int catchNumCount = 0;

	protected void clickNum(Brick b) {
		Gdx.app.log(TAG, "clickNum " + b.getNum());
		int seeingNum = this.runningRandNums.get(catchNumCount);
		if (seeingNum != b.getNum()) {
			GameOver();
		} else {
			catchNumCount++;
			if (catchNumCount == this.runningRandNums.size()) {
				GameWin();
			}
			b.remove();
		}

	}

	private void GameWin() {
		Gdx.app.log(TAG, "GameWin");
		this.level++;
		this.restart(level);
	}

	private void GameOver() {
		Gdx.app.log(TAG, "GameOver");
	}

	protected void hideNum() {
		SnapshotArray<Actor> children = numG.getChildren();
		for (Actor one : children) {
			Brick b = (Brick) one;
			b.hideNum();
		}
	}

	private List<Integer> getBrickNumByLevel(int level2) {

		List<Integer> nums = new ArrayList<Integer>();
		nums.add(1);
		nums.add(3);
		nums.add(9);

		return nums;
	}

	private boolean isDisEnough(int x1, int y1, List<Integer> xAdded,
			List<Integer> yAdded, int d) {

		Gdx.app.log(TAG, "isDisEnough start");
		for (int index = 0; index < xAdded.size(); index++) {

			int x2 = xAdded.get(index);
			int y2 = yAdded.get(index);

			int xd = Math.abs(x1 - x2);
			int yd = Math.abs(y1 - y2);

			if (xd > d && yd > d) {
				continue;
				// return true;
			} else {
				return false;
			}
		}

		Gdx.app.log(TAG, "isDisEnough end");
		return true;
	}

}
