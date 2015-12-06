package test;

import game.engine.gamefield.IDrawable;
import game.engine.gamefield.GameField;
import game.engine.geometry.GeometryObject;
import game.engine.geometry.HumanBeing;

import java.util.ArrayList;
import java.util.List;

public class HardJoinTest {
    public static void main(String args[]) {
        GeometryObject geometryObject = new HumanBeing();
//        GeometryObject parent = geometryObject = new GeometryObject(ShapeFactory.createRectangle(40f, 40f, 300f, 300f, 0f));

        List<IDrawable> objectsToDraw = new ArrayList<IDrawable>();
        objectsToDraw.add(geometryObject);

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
