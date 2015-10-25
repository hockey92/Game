package test;

import game.engine.gamefield.Drawable;
import game.engine.gamefield.GameField;
import game.engine.geometry.collision.Collision;
import game.engine.geometry.figures.ConvexPolygon;

import java.util.ArrayList;
import java.util.List;

public class CollisionTest {
    public static void main(String args[]) throws Exception {
        List<Drawable> gameObjects = new ArrayList<Drawable>();

        float[] xs1 = {-50f, 50f, -50f};
        float[] ys1 = {-50f, -50f, 50f};

        float[] xs2 = {-50f, 50f, -50f};
        float[] ys2 = {-50f, -50f, 50f};

        gameObjects.add(new ConvexPolygon(xs1, ys1, 3));
        gameObjects.add(new ConvexPolygon(xs2, ys2, 3));
        ((ConvexPolygon) gameObjects.get(0)).move(210f, 160f);
        ((ConvexPolygon) gameObjects.get(1)).move(150f, 160f);

        Collision collision = null;

        try {
            collision = new Collision((ConvexPolygon) gameObjects.get(0), (ConvexPolygon) gameObjects.get(1));
            gameObjects.add(collision);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SimpleGameContextImpl contextImp = new SimpleGameContextImpl();
        GameField gameField = new GameField(contextImp);
        gameField.setObjectsToDraw(gameObjects);

        Thread renderThread = new Thread(gameField);
        renderThread.start();

        while (true) {
            ((ConvexPolygon) gameObjects.get(0)).rotate(0.05f);
            ((ConvexPolygon) gameObjects.get(1)).rotate(-0.05f);
            if (collision != null) {
                collision.calculateCollision((ConvexPolygon) gameObjects.get(0), (ConvexPolygon) gameObjects.get(1));
            }
            Thread.sleep(40);
        }
    }
}
