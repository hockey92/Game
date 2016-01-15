package game.engine.newengine;

import game.engine.physics.IConstraint;

import java.util.*;
import java.util.List;

public class PhysicsService {
    private static PhysicsService instance;
    private static final Object instanceMutex = new Object();

    private final Object mutex = new Object();
    private final List<NewGameObject> newGameObjects = new ArrayList<NewGameObject>();
    private final List<NewGameObject> objectsToAdd = new ArrayList<NewGameObject>();

    private Map<Pair, NewEngineConstraint> prevConstraints = new HashMap<Pair, NewEngineConstraint>();

    private ShapeId shapeId = new ShapeId();

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
            gameObject.getShape().setId(shapeId);
            objectsToAdd.add(gameObject);
        }
    }

    public void addGameObjects(List<? extends NewGameObject> newGameObjects) {
        synchronized (mutex) {
            for (NewGameObject gameObject : newGameObjects) {
                gameObject.getShape().setId(shapeId);
            }
            this.newGameObjects.addAll(newGameObjects);
        }
    }

    private class PhysicsServiceThread extends Thread {

        @Override
        public void run() {
            while (true) {
                synchronized (mutex) {
                    System.err.println("time" + System.currentTimeMillis());
                    newGameObjects.addAll(objectsToAdd);
                    objectsToAdd.clear();

                    for (NewGameObject object : newGameObjects) {
                        object.update();
                        if (object.getInvM() == 0f) {
                            continue;
                        }
                        object.updateVel(Constants.dt);
                    }


                    Map<Pair, NewEngineConstraint> constraints = new HashMap<Pair, NewEngineConstraint>();
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
                                    if (collision != null/* && collision.getPenetrationDepth() >= 0*/) {
                                        Pair key = new Pair(a.getId(), b.getId());
//                                        NewEngineConstraint constraint = new NewEngineConstraint(collision, o1, o2);
                                        NewEngineConstraint prevConstraint = prevConstraints.get(key);
//                                        if (prevConstraint != null && prevConstraint.isEquals()) {
//                                            constraints.put(key, prevConstraint);
//                                            prevConstraint.setCollision(collision);
//                                            System.err.println("here");
//                                        } else {
                                        constraints.put(key, new NewEngineConstraint(collision, o1, o2));
//                                    }
                                    }
                                }
                            }
                        }
                    }

//                    System.err.println("=======================================");
//                for (int i = 0; i < 5; i++) {
                    for (int i = 0; i < 10; i++) {
//                        Float maxx = null;
                        for (IConstraint constraint : constraints.values()) {
                            constraint.fix();
//                            float value = Math.abs(NewEngineConstraint.j);
//                            if (maxx == null || maxx < value) {
//                                maxx = value;
//                            }
                        }
//                        if (maxx == null || maxx < 0.1f) {
//                            System.err.println("Iteration Count: " + i);
//                            break;
//                        }
                    }
//                    System.err.println("=======================================");
//                for (NewGameObject object : newGameObjects) {
//                    if (object.getInvM() == 0) {
//                        continue;
//                    }
//                    for (IShape shape : object.getShape().getSimpleShapes()) {
//                        AABB outerAABB = new AABB(
//                                shape.getAABB(),
//                                object.getVel().mulEq(Constants.dt)
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
//                                            float realLen = relV.mulEq(Constants.dt).len();
//                                            if (len > realLen) {
//                                                continue;
//                                            }
//                                            object.getVel().minus(collision.getN().mulEq((realLen - len) / Constants.dt));
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
                        object.updatePos(Constants.dt);

//                        System.err.println(object.getVel());
                    }
//                    System.err.println("===");

                    prevConstraints = constraints;
                }

                try {
                    Thread.sleep(1000 / 60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class Pair {
        private int a;
        private int b;

        public Pair(int a, int b) {
            if (a <= b) {
                this.a = a;
                this.b = b;
            } else {
                this.a = b;
                this.b = a;
            }
        }

        @Override
        public int hashCode() {
            int hash = 17;
            hash = 37 * hash + a;
            hash = 37 * hash + b;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Pair)) {
                return false;
            }
            Pair pair = (Pair) obj;
            return a == pair.a && b == pair.b;
        }
    }
}