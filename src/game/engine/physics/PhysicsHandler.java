package game.engine.physics;

import java.util.ArrayList;
import java.util.List;

public class PhysicsHandler {
    List<PhysicsObject> physicsObjects = new ArrayList<PhysicsObject>();
    private float dt = 0.5f;

    public void handle() {
        for (PhysicsObject object : physicsObjects) {
            object.update(dt);
        }

        for (int i = 0; i < physicsObjects.size(); i++) {
            for (int j = 0; j < physicsObjects.size(); j++) {

            }
        }
    }
}
