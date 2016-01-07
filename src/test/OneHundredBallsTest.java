package test;

import game.engine.gamefield.GameField;
import game.engine.gamefield.IDrawContext;
import game.engine.gamefield.IDrawable;
import game.engine.newengine.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class OneHundredBallsTest {
    public static void main(String args[]) throws Exception {
        List<NewGameObject> gameObjects = new ArrayList<NewGameObject>();
        List<IDrawable> drawableObjects = new ArrayList<IDrawable>();
        final PhysicsServiceX physicsServiceX = new PhysicsServiceX();

        Queue<NewGameObject> balls = new LinkedList<NewGameObject>();

        IDrawContext context = new SimpleGameContextImpl();
        final GameField gameField = new GameField(context);

        gameField.addObjectsToDraw(new OneHundredBalls());

        Thread renderThread = new Thread(gameField);
        renderThread.start();
    }
}
