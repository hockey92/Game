package game.engine.newengine;

import java.util.ArrayList;
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
                    newGameObject.updateVel(0.2f);
                }

                for (int i = 0; i < newGameObjects.size(); i++) {
                    for (int j = i + 1; j < newGameObjects.size(); j++) {
                        Collision c = CollisionFactory.createCollision(
                                newGameObjects.get(i).getShape(),
                                newGameObjects.get(j).getShape()
                        );
                        if (c == null) {
                            for (NewGameObject newGameObject : newGameObjects) {
                                newGameObject.updatePos(0.2f);
                            }
                        }
                    }
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
