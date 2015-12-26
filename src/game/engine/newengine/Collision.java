package game.engine.newengine;

public class Collision {
    private Vec2[] r = new Vec2[2];
    private Vec2 n;
    private float penetrationDepth;

    public Collision(Vec2 r1, Vec2 r2, Vec2 n, float penetrationDepth) {
        r[0] = r1;
        r[1] = r2;
        this.n = n;
        this.penetrationDepth = penetrationDepth;
    }

    public Vec2 getR(int i) {
        return r[i];
    }

    public float getPenetrationDepth() {
        return penetrationDepth;
    }

    public Vec2 getN() {
        return n;
    }
}
