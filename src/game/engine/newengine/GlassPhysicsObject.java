package game.engine.newengine;

public class GlassPhysicsObject extends NewGameObject {

    public final static float LEFT = 1.7f;
    public final static float RIGHT = 4.3f;
    public final static float UP = 1.5f;
    public final static float DOWN = 5.5f;

    private float velValue = 1.3f;
    private static Segment[] path = {
            new Segment(new Vec2(RIGHT, UP), new Vec2(RIGHT, DOWN)),
            new Segment(new Vec2(RIGHT, DOWN), new Vec2(LEFT, DOWN)),
            new Segment(new Vec2(LEFT, DOWN), new Vec2(LEFT, UP)),
            new Segment(new Vec2(LEFT, UP), new Vec2(RIGHT, UP))
    };
    private static float distFromPath = 0.5f;

    public GlassPhysicsObject(IShape shape, float invM) {
        super(shape, invM);
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
