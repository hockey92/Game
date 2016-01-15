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
    private float velValue = 2.0f;
    public final static float left = 1.5f;
    public final static float right = 4.5f;
    public final static float up = 1.5f;
    public final static float down = 5.5f;
    private static Segment[] path = {
            new Segment(new Vec2(right, up), new Vec2(right, down)),
            new Segment(new Vec2(right, down), new Vec2(left, down)),
            new Segment(new Vec2(left, down), new Vec2(left, up)),
            new Segment(new Vec2(left, up), new Vec2(right, up))
    };
    private static float distFromPath = 0.5f;

    public GlassPhysicsObject(IShape shape, float invM) {
        super(shape, invM);
        setVel(velocities[direction].mulEq(velValue));
    }

    public void checkDirection() {
//        if (direction == 2) {
//            getShape().rotate(0.1f);
//        }
        float x = getShape().getCenter().get(0);
        float y = getShape().getCenter().get(1);
        if (x < Constants.left && direction == 0
                || x > Constants.right && direction == 2
                || y < Constants.up && direction == 1
                || y > Constants.down && direction == 3) {
            direction = (direction + 1) % 4;
            setVel(velocities[direction].mulEq(velValue));
        }
    }

    @Override
    public void update() {
        Vec2 dist = null;
        float len = 0;
        for (Segment segment : path) {
            Vec2 tempDist = CollisionFactory.createDistance(getShape().getCenter(), segment);
            float tempLen = tempDist.len();
            if (dist == null || tempLen < len) {
                dist = tempDist;
                len = tempLen;
            }
        }
        Vec2 norm = dist.mulEq(1 / len);
        setVel((Vec2.getCrossProd(norm, 1).mul(velValue)).plus(norm.mulEq(((len - distFromPath) * 0.1f) / Constants.dt)));
    }
}
