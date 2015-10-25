package test;

import game.engine.geometry.GeometryObject;
import game.engine.physics.PhysicsHandler;
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
        PhysicsHandler physicsHandler = new PhysicsHandler();

        float[] xs1 = {-50f, 50f, -50f};
        float[] ys1 = {-50f, -50f, 50f};

        float[] xs2 = {-50f, 50f, -50f};
        float[] ys2 = {-50f, -50f, 50f};

        PhysicsObject.PhysicsObjectBuilder builder = (new PhysicsObject.PhysicsObjectBuilder())
                .setGeometryObject(new GeometryObject(ShapeFactory.createRectangle(100f, 100f, 0f, 0f, 0f), null))
                .setV(Matrix.createCoords(4f, 0f))
                .setA(Matrix.createCoords(0f, 0.05f))
                .setAV(0.02f)
                .setInvM(0.1f)
                .setInvI(0.1f);

        PhysicsObject.PhysicsObjectBuilder platformBuilder = (new PhysicsObject.PhysicsObjectBuilder())
                .setGeometryObject(new GeometryObject(ShapeFactory.createRectangle(600f, 10f, 450f, 500f, 0f), null))
                .setV(Matrix.createCoords(0f, 0f))
                .setA(Matrix.createCoords(0f, 0f))
                .setAV(0f);

        PhysicsObject po1 = builder.createPhysicsObject();
        PhysicsObject platform = platformBuilder.createPhysicsObject();

        gameObjects.add(po1);
        gameObjects.add(platform);

        physicsHandler.addObject(po1);
        physicsHandler.addObject(platform);

        SimpleGameContextImpl contextImp = new SimpleGameContextImpl();
        GameField gameField = new GameField(contextImp);
        gameField.setObjectsToDraw(gameObjects);

        Thread renderThread = new Thread(gameField);
        renderThread.start();

        Thread physicsThread = new Thread(physicsHandler);
        physicsThread.start();
    }
}
