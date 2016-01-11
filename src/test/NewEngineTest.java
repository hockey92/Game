package test;

import game.engine.gamefield.GameField;
import game.engine.gamefield.IDrawContext;
import game.engine.gamefield.IDrawable;
import game.engine.newengine.*;
import game.engine.newengine.Box;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class NewEngineTest {
    public static void main(String args[]) throws Exception {
        List<NewGameObject> gameObjects = new ArrayList<NewGameObject>();
        List<IDrawable> drawableObjects = new ArrayList<IDrawable>();
//        final PhysicsServiceX physicsServiceX = new PhysicsServiceX();
//
//        Queue<NewGameObject> balls = new LinkedList<NewGameObject>();
//
//        float x = 310;
//        float y = 50;
//        for (int i = 0; i < 100; i++) {
//            float currX = x + 20 * (i % 10);
//            float currY = y + 20 * (i / 10);
//            NewGameObject ball = new NewGameObject(new Circle(new Vec2(currX, currY), 8), 1f).setAcceleration(new Vec2(0f, 0.4f));
//            balls.add(ball);
//            drawableObjects.add(ball);
//
//            float y = 10f + i * 10f;
//
//            gameObjects.add(new NewGameObject(new Circle(new Vec2(340, 10 + y), 8), 1f).setAcceleration(new Vec2(0f, 0.8f)));
//            gameObjects.add(new NewGameObject(new Circle(new Vec2(460, 10 + y), 8), 1f).setAcceleration(new Vec2(0f, 0.8f)));
//            gameObjects.add(new NewGameObject(new Circle(new Vec2(320, 10 + y), 8), 1f).setAcceleration(new Vec2(0f, 0.8f)));
//            gameObjects.add(new NewGameObject(new Circle(new Vec2(480, 10 + y), 8), 1f).setAcceleration(new Vec2(0f, 0.8f)));
//
//            gameObjects.add(new NewGameObject(new Circle(new Vec2(360, 10 + y), 8), 1f).setAcceleration(new Vec2(0f, 0.8f)));
//            gameObjects.add(new NewGameObject(new Circle(new Vec2(440, 10 + y), 8), 1f).setAcceleration(new Vec2(0f, 0.8f)));
//            gameObjects.add(new NewGameObject(new Circle(new Vec2(320, 10 + y), 8), 1f).setAcceleration(new Vec2(0f, 0.8f)));
//            gameObjects.add(new NewGameObject(new Circle(new Vec2(480, 10 + y), 8), 1f).setAcceleration(new Vec2(0f, 0.8f)));
//        }
//
//        final IShape container = new Container();
//        container.move(new Vec2(400, 250));
//
//        final IShape glass = new GlassShape();
//        glass.move(new Vec2(400, 700));

        final IShape box = new Box();
        box.move(new Vec2(3, 6));
        gameObjects.add(new NewGameObject(box, 0f));

//        NewGameObject glassObject = new NewGameObject(glass, 0f);
//
//        gameObjects.add(new NewGameObject(container, 0f));
//        gameObjects.add(glassObject);
//
//        drawableObjects.add(container);
//        drawableObjects.add(glass);
//
//        for (int i = 0; i < 20; i++) {
//            NewGameObject ball;
//            if ((ball = balls.poll()) != null) {
//                gameObjects.add(ball);
//            }
//        }

//        drawableObjects.add(box);
        PhysicsService.getInstance().addGameObjects(gameObjects);


        IDrawContext context = new SimpleGameContextImpl();
        final GameField gameField = new GameField(context);

        drawableObjects.addAll(gameObjects);
        gameField.addObjectsToDraw(drawableObjects);

        ((JFrame) context).addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
//                ((Container) container).open();
//
                IShape newCircle = new Circle(new Vec2((float) e.getX() / 100f, (float) e.getY() / 100f), 0.2f);

                PhysicsService.getInstance().addGameObject(
                        new NewGameObject(
                                newCircle, 1f).setAcceleration(new Vec2(0f, 10.0f)
                        )
                );

                gameField.addObjectsToDraw(newCircle);
            }
        });

        Thread renderThread = new Thread(gameField);
        renderThread.start();

//        Thread physicsThread = new Thread(physicsServiceX);
//        physicsThread.start();
//
//        float index = 1f;
//        Vec2[] d = {
//                new Vec2(-1f, 0f),
//                new Vec2(0f, -1f),
//                new Vec2(1f, 0f),
//                new Vec2(0f, 1f)
//        };
//
//        glassObject.setVel(new Vec2(1f, 0f));
//        while (true) {
//            if (glass.getCenter().get(0) > 400 + 200 || glass.getCenter().get(0) < 400 - 200) {
//                index *= -1f;
//                glassObject.setVel(new Vec2(1f * index, 0f));
//            }
//            Thread.sleep(20);
//        }
    }
}
