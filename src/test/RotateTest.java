package test;

import game.engine.gamefield.GameField;
import game.engine.geometry.figures.ConvexPolygon;
import game.engine.geometry.figures.ShapeFactory;

import java.util.ArrayList;
import java.util.List;

public class RotateTest {
    public static void main(String args[]) {
        List<ConvexPolygon> gameObjects = new ArrayList<ConvexPolygon>();
        gameObjects.add(ShapeFactory.createRectangle(20, 300, 200, 200, 0));
        gameObjects.add(ShapeFactory.createRectangle(20, 300, 300, 300, 0));
        gameObjects.add(ShapeFactory.createRectangle(20, 300, 200, 300, 0));
        gameObjects.add(ShapeFactory.createRectangle(20, 300, 300, 200, 0));

        SimpleGameContextImpl contextImp = new SimpleGameContextImpl();
        GameField gameField = new GameField(contextImp);
        gameField.setObjectsToDraw(gameObjects);

        Thread renderThread = new Thread(gameField);
        renderThread.start();

        while (true) {
            try {
                float d[] = {-1f, 1f};
                int count = 0;
                for (ConvexPolygon gameObject : gameObjects) {
                    gameObject.rotate(d[(count++) % 2] * 0.01f);
                }
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
