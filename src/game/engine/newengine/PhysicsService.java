package game.engine.newengine;

import game.engine.physics.IConstraint;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PhysicsService implements Runnable {

    private final List<NewGameObject> newGameObjects = new ArrayList<NewGameObject>();

    public void setNewGameObjects(List<NewGameObject> newGameObjects) {
        synchronized (this.newGameObjects) {
            this.newGameObjects.addAll(newGameObjects);
        }
    }

    @Override
    public void run() {
        while (true) {

            synchronized (newGameObjects) {
                for (NewGameObject newGameObject : newGameObjects) {
                    newGameObject.updateVel(NewEngineConstants.dt);
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
                                Collision c = CollisionFactory.createCollision(a, b);
                                if (c != null) {
                                    constraints.add(new NewEngineConstraint(c, o1, o2));
                                }
                            }
                        }
                    }
                }

                for (int i = 0; i < 1; i++) {
                    for (IConstraint constraint : constraints) {
                        constraint.fix();
                    }
                }

                for (NewGameObject newGameObject : newGameObjects) {
                    newGameObject.updatePos(NewEngineConstants.dt);
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
