package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

public class Hero extends BaseActor{

    Animation north;
    Animation south;
    Animation east;
    Animation west;
    public Hero(float x, float y, Stage s) {
        super(x, y, s);
        String filename = "assets/hero.png";
        int rows = 4;
        int cols = 3;
        Texture texture = new Texture(Gdx.files.internal(filename), true);
        int frameWidth = texture.getWidth() / cols;
        int frameHeight = texture.getHeight() / rows;

        float frameDuration = 0.2f;

        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);
        Array<TextureRegion> textureRegionArray = new Array<>();
        for (int c = 0; c < cols; c++)
            textureRegionArray.add(temp[0][c]);
        south = new Animation<>(frameDuration, textureRegionArray, Animation.PlayMode.LOOP_PINGPONG);
        textureRegionArray.clear();

        for (int c = 0; c < cols; c++)
            textureRegionArray.add(temp[1][c]);
        west = new Animation<>(frameDuration, textureRegionArray, Animation.PlayMode.LOOP_PINGPONG);
        textureRegionArray.clear();

        for (int c = 0; c < cols; c++)
            textureRegionArray.add(temp[2][c]);
        east = new Animation<>(frameDuration, textureRegionArray, Animation.PlayMode.LOOP_PINGPONG);
        textureRegionArray.clear();

        for (int c = 0; c < cols; c++)
            textureRegionArray.add(temp[3][c]);
        north = new Animation<>(frameDuration, textureRegionArray, Animation.PlayMode.LOOP_PINGPONG);
        textureRegionArray.clear();

        setAnimation(south);
        setBoundaryPolygon(8);
        setAcceleration(800);
        setMaxSpeed(100);
        setDeceleration(800);
    }

    @Override
    public void act(float dt) {
        super.act(dt);

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            accelerateAtAngle(180);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            accelerateAtAngle(0);
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            accelerateAtAngle(90);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            accelerateAtAngle(270);

        if (getSpeed() == 0){
            setAnimationPaused(true);
        } else {
            setAnimationPaused(false);

            // set direction animation
            float angle = getMotionAngle();
            if (angle >= 45 && angle <= 135)
                setAnimation(north);
            else if (angle > 135 && angle < 225)
                setAnimation(west);
            else if (angle >= 225 && angle <= 315)
                setAnimation(south);
            else
                setAnimation(east);
        }

        applyPhysics(dt);
    }
}
