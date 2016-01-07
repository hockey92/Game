package game.engine.newengine;

import game.engine.physics.IConstraint;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PhysicsService {
    private static PhysicsService instance;
    private static final Object instanceMutex = new Object();

    private final Object mutex = new Object();
    private final List<NewGameObject> newGameObjects = new ArrayList<NewGameObject>();
    private final List<NewGameObject> objectsToAdd = new ArrayList<NewGameObject>();

    private PhysicsService() {
        this.new PhysicsServiceThread().start();
    }

    public static PhysicsService getInstance() {
        if (instance == null) {
            synchronized (instanceMutex) {
                if (instance == null) {
                    instance = new PhysicsService();
                }
            }
        }
        return instance;
    }

    public void addGameObject(NewGameObject gameObject) {
        synchronized (mutex) {
            objectsToAdd.add(gameObject);
        }
    }

    public void addGameObjects(List<? extends NewGameObject> newGameObjects) {
        synchronized (mutex) {
            this.newGameObjects.addAll(newGameObjects);
        }
    }

    private class PhysicsServiceThread extends Thread {

        @Override
        public void run() {
            while (true) {
                synchronized (mutex) {
                    newGameObjects.addAll(objectsToAdd);
                    objectsToAdd.clear();

                    for (NewGameObject object : newGameObjects) {
                        object.updateVel(NewEngineConstants.dt);
                    }

                    List<IConstraint> constraints = new LinkedList<IConstraint>();

                    for (int i = 0; i < newGameObjects.size(); i++) {
                        for (int j = i + 1; j < newGameObjects.size(); j++) {
                            NewGameObject o1 = newGameObjects.get(i);
                            NewGameObject o2 = newGameObjects.get(j);

                            if (o1.getInvM() == 0f && o2.getInvM() == 0f) {
                                continue;
                            }

                            for (IShape a : o1.getShape().getSimpleShapes()) {
                                for (IShape b : o2.getShape().getSimpleShapes()) {
                                    Collision collision = CollisionFactory.createCollision(a, b);
                                    if (collision != null) {
                                        constraints.add(new NewEngineConstraint(collision, o1, o2));
//                                    if (collision.getPenetrationDepth() >= 0) {
//                                        constraints.add(new NewEngineConstraint(collision, o1, o2));
//                                    } else {
//                                        constraintsToRemoveVelocity.add(new RemoveVelConstraint(collision, o1, o2));
//                                    }
                                    }
                                }
                            }
                        }
                    }

//                    System.err.println("=======================================");
//                for (int i = 0; i < 5; i++) {
                    for (int i = 0; i < 50; i++) {
                        Float maxx = null;
                        for (IConstraint constraint : constraints) {
                            constraint.fix();
                            if (maxx == null || maxx < NewEngineConstraint.j) {
                                maxx = NewEngineConstraint.j;
                            }
                        }
                        if (maxx == null || maxx < 0.01f) {
//                            System.err.println(i);
                            break;
                        }
                    }
//                    System.err.println("=======================================");
//                for (NewGameObject object : newGameObjects) {
//                    if (object.getInvM() == 0) {
//                        continue;
//                    }
//                    for (IShape shape : object.getShape().getSimpleShapes()) {
//                        AABB outerAABB = new AABB(
//                                shape.getAABB(),
//                                object.getVel().mulEq(NewEngineConstants.dt)
//                        );
//                        for (NewGameObject innerObject : newGameObjects) {
//                            if (innerObject != object) {
//                                for (IShape innerShape : innerObject.getShape().getSimpleShapes()) {
//                                    if (AABB.isIntersect(outerAABB, innerShape.getAABB())) {
//                                        Collision collision = CollisionFactory.createCollision(shape, innerShape);
//                                        if (collision != null) {
//                                            float len = collision.getPenetrationDepth();
//                                            if (len >= 0) {
//                                                continue;
//                                            } else {
//                                                len = -len;
//                                            }
//                                            float dotProduct = Vec2.getDotProd(collision.getN(), object.getVel());
//                                            if (dotProduct < 0) {
//                                                continue;
//                                            }
//                                            Vec2 relV = collision.getN().mulEq(dotProduct);
//                                            float realLen = relV.mulEq(NewEngineConstants.dt).len();
//                                            if (len > realLen) {
//                                                continue;
//                                            }
//                                            object.getVel().minus(collision.getN().mulEq((realLen - len) / NewEngineConstants.dt));
//                                            int i = 0;
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
                    for (NewGameObject object : newGameObjects) {
//                    if (object.getInvM() == 0) {
//                        continue;
//                    }
                        object.updatePos(NewEngineConstants.dt);

                        System.err.println(object.getVel());
                    }
                    System.err.println("===");
                }

                try {
                    Thread.sleep(1000 / 30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
