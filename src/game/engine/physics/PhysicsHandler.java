package game.engine.physics;

import game.engine.geometry.collision.Collision;
import game.engine.geometry.collision.ICollision;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PhysicsHandler implements Runnable {
    List<IPhysicsObject> physicsObjects = new ArrayList<IPhysicsObject>();
    List<IConstraint> constraints = new ArrayList<IConstraint>();
    private float dt = 0.5f;
    private int iterationCount = 1;

    public void addObject(IPhysicsObject physicsObject) {
        physicsObjects.add(physicsObject);
    }

    public void addConstraint(IConstraint constraint) {
        constraints.add(constraint);
    }

    @Override
    public void run() {
        while (true) {
            long time1 = System.currentTimeMillis();
            for (IPhysicsObject object : physicsObjects) {
                object.updateVelocity(dt);
            }
            long time2 = System.currentTimeMillis();
            List<IConstraint> contactConstraints = new LinkedList<IConstraint>();
            for (int i = 0; i < physicsObjects.size(); i++) {
                for (int j = i + 1; j < physicsObjects.size(); j++) {
//                    if (i != 23 && i != 24 && j != 23 && j != 24 && i > 3 && j > 3) {
//                        continue;
//                    }
                    ICollision c = null;
                    IPhysicsObject p1 = physicsObjects.get(i);
                    IPhysicsObject p2 = physicsObjects.get(j);

                    if (p1.getInvM() + p1.getInvI() == 0 && p2.getInvM() + p2.getInvI() == 0) {
                        continue;
                    }

                    try {
                        c = new Collision(p1.getGeometryObject(), p2.getGeometryObject());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (c != null && c.isCollision()) {
                        contactConstraints.add(new ContactConstraint(p1, p2, c, dt));
                    }
                }
            }
            long time3 = System.currentTimeMillis();
            long time4 = 0;
//            constraints.add(new JointConstraint(physicsObjects.get(4), physicsObjects.get(5), 0f, 0f, 10f, (float) -Math.PI, 1f));
//
//            for (int i = 0; i < 17; i++) {
//                constraints.add(new JointConstraint(physicsObjects.get(5 + i), physicsObjects.get(6 + i), 10f, 0f, 10f, (float) -Math.PI, dt));
//            }
            for (int iteration = 0; iteration < iterationCount; iteration++) {
                for (IConstraint constraint : contactConstraints) {
                    constraint.fix();
                }
//                for (IConstraint constraint : constraints) {
//                    constraint.fix();
//                }
                time4 = System.currentTimeMillis();
//                System.out.println("---------------------");
            }
            constraints.clear();
            System.out.println("1: " + (time2 - time1) + " 2: " + (time3 - time2) + " 3: " + (time4 - time3));
            for (IPhysicsObject object : physicsObjects) {
                object.updatePosition(dt);
            }
            try {
//                long sleepTime = 10 - (time4 - time1);
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}