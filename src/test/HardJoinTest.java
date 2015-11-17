package test;

import game.engine.gamefield.Drawable;
import game.engine.gamefield.GameField;
import game.engine.geometry.GeometryObject;
import game.engine.geometry.HardJoin;
import game.engine.geometry.figures.ConvexPolygon;
import game.engine.geometry.figures.ShapeFactory;

import java.util.ArrayList;
import java.util.List;

public class HardJoinTest {
    public static void main(String args[]) {
        GeometryObject geometryObject;
        GeometryObject parent = geometryObject = new GeometryObject(ShapeFactory.createRectangle(40f, 40f, 300f, 300f, 0f));

        List<Drawable> objectsToDraw = new ArrayList<Drawable>();
        objectsToDraw.add(parent);

        for (int i = 0; i < 10; i++) {
            GeometryObject hardJoin = new HardJoin(parent);
            parent.addChild(hardJoin);
            GeometryObject child = new GeometryObject(ShapeFactory.createRectangle(40f, 40f, 300f, 300f, 0f), parent);
            hardJoin.addChild(child);
            objectsToDraw.add(child);
            parent = child;
        }

        SimpleGameContextImpl contextImp = new SimpleGameContextImpl();
        GameField gameField = new GameField(contextImp);
        gameField.setObjectsToDraw(objectsToDraw);

        Thread renderThread = new Thread(gameField);
        renderThread.start();

        while (true) {
            try {
                geometryObject.rotate(0.01f);
                geometryObject.update();
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
