package test;

import game.engine.geometry.GeometryObject;
import game.engine.physics.PhysicsObject;
import game.engine.gamefield.Drawable;
import game.engine.gamefield.GameField;
import game.engine.geometry.figures.ShapeFactory;
import game.engine.myutils.Matrix;

import java.util.ArrayList;
import java.util.List;

public class PhysicsObjectTest {
    public static void main(String args[]) throws Exception {
        List<Drawable> gameObjects = new ArrayList<Drawable>();

        float[] xs1 = {-50f, 50f, -50f};
        float[] ys1 = {-50f, -50f, 50f};

        float[] xs2 = {-50f, 50f, -50f};
        float[] ys2 = {-50f, -50f, 50f};

        PhysicsObject.PhysicsObjectBuilder builder = (new PhysicsObject.PhysicsObjectBuilder())
                .setGeometryObject(new GeometryObject(ShapeFactory.createRectangle(100f, 100f, 0f, 0f, 0f), null))
                .setV(Matrix.createCoords(1f, 0f))
                .setA(Matrix.createCoords(0f, 0.002f))
                .setAV(0.02f);

        PhysicsObject.PhysicsObjectBuilder platformBuilder = (new PhysicsObject.PhysicsObjectBuilder())
                .setGeometryObject(new GeometryObject(ShapeFactory.createRectangle(600f, 10f, 500f, 500f, 0f), null))
                .setV(Matrix.createCoords(1f, 0f))
                .setA(Matrix.createCoords(0f, 0.002f))
                .setAV(0.02f);

        gameObjects.add(builder.createPhysicsObject());
        gameObjects.add(platformBuilder.createPhysicsObject());

//        Collision collision = null;
//
//        try {
//            collision = new Collision((ConvexPolygon) gameObjects.get(0), (ConvexPolygon) gameObjects.get(1));
//            gameObjects.add(collision);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        SimpleGameContextImpl contextImp = new SimpleGameContextImpl();
        GameField gameField = new GameField(contextImp);
        gameField.setObjectsToDraw(gameObjects);

        Thread renderThread = new Thread(gameField);
        renderThread.start();

        while (true) {
            ((PhysicsObject) gameObjects.get(0)).update(1f);
//            ((ConvexPolygon) gameObjects.get(0)).rotate(0.05f);
//            ((ConvexPolygon) gameObjects.get(1)).rotate(-0.05f);
//            if (collision != null) {
//                collision.calculateCollision((ConvexPolygon) gameObjects.get(0), (ConvexPolygon) gameObjects.get(1));
//            }
            Thread.sleep(20);
        }
    }
}
