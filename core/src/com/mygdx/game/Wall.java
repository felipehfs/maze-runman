package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class Wall extends BaseActor{
    public Wall(float x, float y, float width, float height, Stage s) {
        super(x, y, s);
        loadTexture("assets/square.jpg");
        setSize(width, height);
        setBoundaryRectangle();
    }

}
