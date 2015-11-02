package game.engine.physics;

import game.engine.geometry.collision.Collision;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PhysicsHandler implements Runnable {
    List<PhysicsObject> physicsObjects = new ArrayList<PhysicsObject>();
    private float dt = 0.5f;
    private int iterationCount = 1;

    public void addObject(PhysicsObject po) {
        physicsObjects.add(po);
    }

    @Override
    public void run() {
        while (true) {
            long time1 = System.currentTimeMillis();
            for (PhysicsObject object : physicsObjects) {
                object.update(dt);
            }
            long time2 = System.currentTimeMillis();
            List<Constraint> contactConstraints = new LinkedList<Constraint>();
            for (int i = 0; i < physicsObjects.size(); i++) {
                for (int j = i + 1; j < physicsObjects.size(); j++) {
                    Collision c = null;
                    PhysicsObject p1 = physicsObjects.get(i);
                    PhysicsObject p2 = physicsObjects.get(j);

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
            for (int iteration = 0; iteration < iterationCount; iteration++) {
                for (Constraint constraint : contactConstraints) {
                    constraint.fix();
                }
            }
            long time4 = System.currentTimeMillis();

            System.out.println("1: " + (time2 - time1) + " 2: " + (time3 - time2) + " 3: " + (time4 - time3));

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
