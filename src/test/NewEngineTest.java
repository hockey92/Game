package test;

import game.engine.gamefield.GameField;
import game.engine.newengine.*;

import java.util.ArrayList;
import java.util.List;

public class NewEngineTest {
    public static void main(String args[]) throws Exception {
        List<NewGameObject> gameObjects = new ArrayList<NewGameObject>();
        PhysicsService physicsService = new PhysicsService();

        gameObjects.add(new NewGameObject(new Circle(new Vec2(200, 200), 20), 1f).setAcceleration(new Vec2(0f, 0.5f)));
        gameObjects.add(new NewGameObject(new Segment(new Vec2(0, 300), new Vec2(190, 300)), 0f));
        physicsService.setNewGameObjects(gameObjects);

        SimpleGameContextImpl contextImp = new SimpleGameContextImpl();
        GameField gameField = new GameField(contextImp);
        gameField.setObjectsToDraw(gameObjects);

        Thread renderThread = new Thread(gameField);
        renderThread.start();

        Thread physicsThread = new Thread(physicsService);
        physicsThread.start();
    }
}
