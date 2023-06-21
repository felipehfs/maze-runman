package com.mygdx.game;

public class MazeRunman extends BaseGame {
	@Override
	public void create() {
		super.create();
		setActiveScreen(new LevelScreen());
	}
}
