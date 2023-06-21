package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class LevelScreen extends BaseScreen
{
    Maze maze;
    Hero hero;
    Ghost ghost;
    Label coinsLabel;
    Label messageLabel;

    Sound coinSound;
    Music windMusic;
    public void initialize() 
    {        
        BaseActor background = new BaseActor(0, 0, mainStage);
        background.loadTexture("assets/white.png");
        background.setColor(Color.GRAY);
        background.setSize(768, 700);

        maze = new Maze(mainStage);
        hero = new Hero(0, 0, mainStage);
        hero.centerAtActor(maze.getRoom(0, 0));

        ghost = new Ghost(0, 0, mainStage);
        ghost.centerAtActor(maze.getRoom(11, 9));

        for (BaseActor room : BaseActor.getList(mainStage, "com.mygdx.game.Room")) {
            Coin coin = new Coin(0, 0, mainStage);
            coin.centerAtActor(room);
        }

        ghost.toFront();

        coinsLabel = new Label("Coins Left:", BaseGame.labelStyle);
        coinsLabel.setColor(Color.GOLD);

        messageLabel = new Label("...", BaseGame.labelStyle);
        coinsLabel.setFontScale(2);
        messageLabel.setVisible(false);

        uiTable.pad(10);
        uiTable.add(coinsLabel);
        uiTable.row();
        uiTable.add(messageLabel).expandY();

        coinSound = Gdx.audio.newSound(Gdx.files.internal("assets/coin.wav"));
        windMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/wind.mp3"));

        windMusic.setLooping(true);
        windMusic.setVolume(0.1f);
        windMusic.play();
    }

    public void update(float dt)
    {
        for (BaseActor wall : BaseActor.getList(mainStage, "com.mygdx.game.Wall")) {
            hero.preventOverlap(wall);
        }

        if (ghost.getActions().size == 0) {
            maze.resetRooms();
            ghost.findPath(maze.getRoom(ghost), maze.getRoom(hero));
        }

        for (BaseActor coin: BaseActor.getList(mainStage, "com.mygdx.game.Coin")) {
            if (hero.overlaps(coin)) {
                coin.remove();
                coinSound.play(0.10f);
            }
        }

        int coins = BaseActor.count(mainStage, "com.mygdx.game.Coin");
        coinsLabel.setText("Coins Left: " + coins);

        if (coins == 0) {
            ghost.remove();
            ghost.setPosition(-1000, -1000);
            ghost.clearActions();
            ghost.addAction(Actions.forever(Actions.delay(1)));
            messageLabel.setText("You win!");
            messageLabel.setColor(Color.GREEN);
            messageLabel.setVisible(true);
        }

        if (hero.overlaps(ghost)) {
            hero.remove();
            hero.setPosition(-1000, -1000);
            ghost.clearActions();
            ghost.addAction(Actions.forever(Actions.delay(1)));
            messageLabel.setText("Game over");
            messageLabel.setColor(Color.RED);
            messageLabel.setVisible(true);
        }

        if (!messageLabel.isVisible()) {
            float distance = new Vector2(hero.getX() - ghost.getX(), hero.getY() - ghost.getY()).len();
            float volume = - (distance - 64) / (300 - 64) + 1;
            volume = MathUtils.clamp(volume, 0.10f, 1.00f);
            windMusic.setVolume(volume);
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.R) {
            BaseGame.setActiveScreen(new LevelScreen());
        }
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}