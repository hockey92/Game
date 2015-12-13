package test;

import game.engine.geometry.GeometryObject;
import game.engine.geometry.collision.Collision;
import game.engine.geometry.collision.ICollision;
import game.engine.physics.IPhysicsObject;
import game.engine.physics.PhysicsHandler;
import game.engine.physics.PhysicsObject;
import game.engine.gamefield.IDrawable;
import game.engine.gamefield.GameField;
import game.engine.geometry.figures.ShapeFactory;
import game.engine.myutils.Matrix;

import java.util.ArrayList;
import java.util.List;

public class PhysicsObjectTest {
    public static void main(String args[]) throws Exception {
        List<IDrawable> gameObjects = new ArrayList<IDrawable>();
        PhysicsHandler physicsHandler = new PhysicsHandler();

        float[] xs1 = {-50f, 50f, -50f};
        float[] ys1 = {-50f, -50f, 50f};

        float[] xs2 = {-50f, 50f, -50f};
        float[] ys2 = {-50f, -50f, 50f};

        float invI = 0.001f;
        int n = 20;

        IPhysicsObject pos[] = new PhysicsObject[n];

        float x = 200f;
        for (int i = 0; i < n; i++) {
            float invM = 1f;
            pos[i] = (new PhysicsObject.PhysicsObjectBuilder())
                    .setGeometryObject(new GeometryObject(ShapeFactory.createRectangle(50f, 50f, 100f + (i % 6) * 60f, 100f + (i / 6) * 60f, -0.4f), null))
                    .setV(Matrix.createCoords(0f, 0f))
                    .setA(Matrix.createCoords(0f, 0.1f))
                    .setAV(0f)
                    .setInvM(invM)
                    .setInvI(invM * (12f / (2500f + 2500f)))
                    .build();
        }

        PhysicsObject.PhysicsObjectBuilder gPlatformBuilder = (new PhysicsObject.PhysicsObjectBuilder())
                .setGeometryObject(new GeometryObject(ShapeFactory.createRectangle(3000f, 10f, 500f, 550f, 0f), null))
                .setV(Matrix.createCoords(0f, 0f))
                .setA(Matrix.createCoords(0f, 0f))
                .setAV(0f);

        PhysicsObject.PhysicsObjectBuilder gPlatformBuilder2 = (new PhysicsObject.PhysicsObjectBuilder())
                .setGeometryObject(new GeometryObject(ShapeFactory.createRectangle(3000f, 10f, 500f, 50f, 0f), null))
                .setV(Matrix.createCoords(0f, 0f))
                .setA(Matrix.createCoords(0f, 0f))
                .setAV(0f);

        PhysicsObject.PhysicsObjectBuilder vPlatformBuilder = (new PhysicsObject.PhysicsObjectBuilder())
                .setGeometryObject(new GeometryObject(ShapeFactory.createRectangle(10f, 3000f, 500f, 300f, 0f), null))
                .setV(Matrix.createCoords(0f, 0f))
                .setA(Matrix.createCoords(0f, 0f))
                .setAV(0f);

        PhysicsObject.PhysicsObjectBuilder vPlatformBuilder2 = (new PhysicsObject.PhysicsObjectBuilder())
                .setGeometryObject(new GeometryObject(ShapeFactory.createRectangle(10f, 3000f, 20f, 300f, 0f), null))
                .setV(Matrix.createCoords(0f, 0f))
                .setA(Matrix.createCoords(0f, 0f))
                .setAV(0f);

        IPhysicsObject gPlatform = gPlatformBuilder.build();
        IPhysicsObject gPlatform2 = gPlatformBuilder2.build();
        IPhysicsObject vPlatform = vPlatformBuilder.build();
        IPhysicsObject vPlatform2 = vPlatformBuilder2.build();

        gameObjects.add(gPlatform);
        gameObjects.add(gPlatform2);
        gameObjects.add(vPlatform);
        gameObjects.add(vPlatform2);

        physicsHandler.addObject(gPlatform);
        physicsHandler.addObject(gPlatform2);
        physicsHandler.addObject(vPlatform);
        physicsHandler.addObject(vPlatform2);

        for (int i = 0; i < n; i++) {
            physicsHandler.addObject(pos[i]);
            gameObjects.add(pos[i]);
        }

        SimpleGameContextImpl contextImp = new SimpleGameContextImpl();
        GameField gameField = new GameField(contextImp);
        gameField.setObjectsToDraw(gameObjects);

        Thread renderThread = new Thread(gameField);
        renderThread.start();

        Thread physicsThread = new Thread(physicsHandler);
        physicsThread.start();

//        while (true) {
//            c.calculateCollision(gPlatform.getGeometryObject().getShape(), pos[1].getGeometryObject().getShape());
//            Thread.sleep(20);
//        }

//        while(true) {
//            c.calculateCollision(pos[2].getGeometryObject().getShape(), gPlatform.getGeometryObject().getShape());
//            Thread.sleep(2);
//        }
    }
}
