package test;

import game.engine.gamefield.GameField;
import game.engine.geometry.collision.CSO;
import game.engine.geometry.figures.ConvexPolygon;

import java.util.ArrayList;
import java.util.List;

public class CSOTest {
    public static void main(String args[]) {
        List<ConvexPolygon> gameObjects = new ArrayList<ConvexPolygon>();

        float[] xs1 = {0f, -40f, 40f};
        float[] ys1 = {50f, -50f, -50f};

        float[] xs2 = {0f, -20f, 40f};
        float[] ys2 = {50f, -50f, -50f};

        gameObjects.add(new ConvexPolygon(xs1, ys1, 3));
        gameObjects.add(new ConvexPolygon(xs2, ys2, 3));
        gameObjects.add(new ConvexPolygon(xs2, ys2, 3));
        gameObjects.add(new ConvexPolygon(xs2, ys2, 3));

        for (int i = 1; i <= 3; i++) {
            gameObjects.get(i).rotate(0.4f);
        }

        gameObjects.add(new CSO(gameObjects.get(0), gameObjects.get(1)));

        gameObjects.get(1).move(0f, 50f);
        gameObjects.get(2).move(-40f, -50f);
        gameObjects.get(3).move(40f, -50f);


        gameObjects.get(0).move(200f, 200f);
        gameObjects.get(1).move(200f, 200f);
        gameObjects.get(2).move(200f, 200f);
        gameObjects.get(3).move(200f, 200f);

        gameObjects.get(4).move(200f, 200f);

        gameObjects.get(1).rotate((float) Math.PI);
        gameObjects.get(2).rotate((float) Math.PI);
        gameObjects.get(3).rotate((float) Math.PI);

        SimpleGameContextImpl contextImp = new SimpleGameContextImpl();
        GameField gameField = new GameField(contextImp);
        gameField.setObjectsToDraw(gameObjects);

        Thread renderThread = new Thread(gameField);
        renderThread.start();
    }
}
