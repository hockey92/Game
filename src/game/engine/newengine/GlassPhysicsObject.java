package game.engine.newengine;

public class GlassPhysicsObject extends NewGameObject {
    private int direction = 0;
    private float angle = 0;
    private boolean rotate = false;
    private static final Vec2[] velocities = {
            new Vec2(-1f, 0f),
            new Vec2(0f, -1f),
            new Vec2(1f, 0f),
            new Vec2(0f, 1f)
    };
    private float vel = 2f;

    public GlassPhysicsObject(IShape shape, float invM) {
        super(shape, invM);
        setVel(velocities[direction].mulEq(vel));
    }

    public void checkDirection() {
//        if (direction == 2) {
//            getShape().rotate(0.1f);
//        }
        float x = getShape().getCenter().get(0);
        float y = getShape().getCenter().get(1);
        if (x < NewEngineConstants.left && direction == 0
                || x > NewEngineConstants.right && direction == 2
                || y < NewEngineConstants.up && direction == 1
                || y > NewEngineConstants.down && direction == 3) {
            direction = (direction + 1) % 4;
            setVel(velocities[direction].mulEq(vel));
        }
    }
}
