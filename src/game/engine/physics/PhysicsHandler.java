package game.engine.physics;

import game.engine.geometry.collision.Collision;
import game.engine.myutils.Matrix;
import game.engine.myutils.Pair;

import java.util.ArrayList;
import java.util.List;

public class PhysicsHandler implements Runnable {
    List<PhysicsObject> physicsObjects = new ArrayList<PhysicsObject>();
    private float dt = 0.5f;
    private int iterationCount = 10;

    public void addObject(PhysicsObject po) {
        physicsObjects.add(po);
    }

    @Override
    public void run() {
        while (true) {
            for (PhysicsObject object : physicsObjects) {
                object.update(dt);
            }

            List<Collision> collisions = new ArrayList<Collision>();
            List<Pair<PhysicsObject, PhysicsObject>> objects = new ArrayList<Pair<PhysicsObject, PhysicsObject>>();
            for (int i = 0; i < physicsObjects.size(); i++) {
                for (int j = i + 1; j < physicsObjects.size(); j++) {
                    Collision c = null;
                    PhysicsObject p1 = physicsObjects.get(i);
                    PhysicsObject p2 = physicsObjects.get(j);
                    try {
                        c = new Collision(p1.getGeometryObject(), p2.getGeometryObject());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (c != null && c.isCollision()) {
                        collisions.add(c);
                        objects.add(new Pair<PhysicsObject, PhysicsObject>(p1, p2));
                    }
                }
            }

            for (int iteration = 0; iteration < iterationCount; iteration++) {
                for (int i = 0; i < collisions.size(); i++) {
                    Collision c = collisions.get(i);
                    PhysicsObject p1 = objects.get(i).a;
                    PhysicsObject p2 = objects.get(i).b;
                    Matrix J = PhysicsMatrixFactory.createSimpleJacobian(c.getNormal(), c.getContactVector(0), c.getContactVector(1));
                    Matrix M = PhysicsMatrixFactory.createMassMatrix(p1, p2);
                    Matrix V = PhysicsMatrixFactory.createVelocityMatrix(p1, p2);
                    float lyambda = -Matrix.mul(Matrix.mul(J, M), J.transpose()).get(0) * Matrix.mul(V, J).get(0);
                }
            }

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
