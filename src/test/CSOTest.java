package test;

import game.engine.gamefield.GameField;
import game.engine.gamefield.IDrawable;
import game.engine.geometry.collision.CSO;
import game.engine.geometry.collision.Collision;
import game.engine.geometry.figures.ConvexPolygon;
import game.engine.geometry.figures.ShapeFactory;
import game.engine.myutils.MathUtils;
import game.engine.physics.IConstraint;

import java.util.ArrayList;
import java.util.List;

public class CSOTest {
    public static void main(String args[]) throws InterruptedException {
        List<IDrawable> gameObjects = new ArrayList<IDrawable>();

//        float[] xs1 = {0f, -40f, 40f};
//        float[] ys1 = {50f, -50f, -50f};
//
//        float[] xs2 = {0f, -20f, 40f};
//        float[] ys2 = {50f, -50f, -50f};
//
//        gameObjects.add(new ConvexPolygon(xs1, ys1, 3));
//        gameObjects.add(new ConvexPolygon(xs2, ys2, 3));
//        gameObjects.add(new ConvexPolygon(xs2, ys2, 3));
//        gameObjects.add(new ConvexPolygon(xs2, ys2, 3));

        ConvexPolygon cp1;
        ConvexPolygon cp2;
        Collision c;
        gameObjects.add(cp1 = ShapeFactory.createRectangle(50, 50, 160, 200, 0));
        gameObjects.add(cp2 = ShapeFactory.createRectangle(50, 50, 200, 220, 0));
        gameObjects.add(c = new Collision(cp1, cp2));

//        for (int i = 1; i <= 3; i++) {
//            gameObjects.get(i).rotate(0.4f);
//        }
//
//        CSO cso;
//        gameObjects.add(cso = new CSO(gameObjects.get(0), gameObjects.get(1)));
//        gameObjects.add(cso);
//
//        gameObjects.get(1).move(0f, 50f);
//        gameObjects.get(2).move(-40f, -50f);
//        gameObjects.get(3).move(40f, -50f);
//
//        gameObjects.get(0).move(200f, 200f);
//        gameObjects.get(1).move(200f, 200f);
//        gameObjects.get(2).move(200f, 200f);
//        gameObjects.get(3).move(200f, 200f);
//
//        gameObjects.get(4).move(200f, 200f);
//
//        gameObjects.get(1).rotate(MathUtils.PI);
//        gameObjects.get(2).rotate(MathUtils.PI);
//        gameObjects.get(3).rotate(MathUtils.PI);

        SimpleGameContextImpl contextImp = new SimpleGameContextImpl();
        GameField gameField = new GameField(contextImp);
        gameField.addObjectsToDraw(gameObjects);

        Thread renderThread = new Thread(gameField);
        renderThread.start();

        while (true) {
            cp1.rotate(0.02f);
            cp2.rotate(0.02f);
            c.calculateCollision(cp1, cp2);
//            cso.createCSO(gameObjects.get(0), gameObjects.get(1));
            Thread.sleep(20);
        }
    }
}
