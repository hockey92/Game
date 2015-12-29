package test;

import game.engine.gamefield.GameField;
import game.engine.gamefield.IDrawContext;
import game.engine.gamefield.IDrawable;
import game.engine.newengine.*;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class NewEngineTest {
    public static void main(String args[]) throws Exception {
        List<NewGameObject> gameObjects = new ArrayList<NewGameObject>();
        List<IDrawable> drawableObjects = new ArrayList<IDrawable>();
        PhysicsService physicsService = new PhysicsService();

        for (int i = 0; i < 1; i++) {

            float y = 10f + i * 10f;

//            gameObjects.add(new NewGameObject(new Circle(new Vec2(340, 10 + y), 7), 1f).setAcceleration(new Vec2(0f, 0.5f)));
//            gameObjects.add(new NewGameObject(new Circle(new Vec2(460, 10 + y), 7), 1f).setAcceleration(new Vec2(0f, 0.5f)));
//            gameObjects.add(new NewGameObject(new Circle(new Vec2(320, 10 + y), 7), 1f).setAcceleration(new Vec2(0f, 0.5f)));
//            gameObjects.add(new NewGameObject(new Circle(new Vec2(480, 10 + y), 7), 1f).setAcceleration(new Vec2(0f, 0.5f)));
//
//            gameObjects.add(new NewGameObject(new Circle(new Vec2(360, 10 + y), 7), 1f).setAcceleration(new Vec2(0f, 1.0f)));
//            gameObjects.add(new NewGameObject(new Circle(new Vec2(440, 10 + y), 7), 1f).setAcceleration(new Vec2(0f, 1.0f)));
//            gameObjects.add(new NewGameObject(new Circle(new Vec2(320, 10 + y), 7), 1f).setAcceleration(new Vec2(0f, 1.0f)));
            gameObjects.add(new NewGameObject(new Circle(new Vec2(480, 10 + y), 7), 1f).setAcceleration(new Vec2(0f, 1.0f)));
        }

        gameObjects.add(new NewGameObject(new Segment(new Vec2(0, 250), new Vec2(800, 250)), 0f));
        final IShape container = new Container();
        container.move(new Vec2(400, 250));

        final IShape glass = new Glass();
        glass.move(new Vec2(400, 700));

        NewGameObject glassObject = new NewGameObject(glass, 0f);

        gameObjects.add(new NewGameObject(container, 0f));
        gameObjects.add(glassObject);

        drawableObjects.add(container);
        drawableObjects.add(glass);
        physicsService.setNewGameObjects(gameObjects);

        IDrawContext context = new SimpleGameContextImpl();
        GameField gameField = new GameField(context);

        drawableObjects.addAll(gameObjects);
        gameField.setObjectsToDraw(drawableObjects);

        ((JFrame) context).addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ((Container) container).open();
            }
        });

        Thread renderThread = new Thread(gameField);
        renderThread.start();

        Thread physicsThread = new Thread(physicsService);
        physicsThread.start();

        float index = 1f;
        Vec2[] d = {
                new Vec2(-1f, 0f),
                new Vec2(0f, -1f),
                new Vec2(1f, 0f),
                new Vec2(0f, 1f)
        };

        glassObject.setVel(new Vec2(6f, 0f));
        while (true) {
            if (glass.getCenter().get(0) > 400 + 200 || glass.getCenter().get(0) < 400 - 200) {
                index *= -1f;
                glassObject.setVel(new Vec2(6f * index, 0f));
            }
            Thread.sleep(20);
        }

    }
}
