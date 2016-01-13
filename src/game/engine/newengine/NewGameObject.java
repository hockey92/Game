package game.engine.newengine;

import game.engine.gamefield.IDrawContext;
import game.engine.gamefield.IDrawable;

public class NewGameObject implements IDrawable {
    private IShape shape;
    private float invM;
    private Vec2 vel = new Vec2(0, 0);
    private Vec2 acc = new Vec2(0, 0);
    private float angleVel = 0;

    private final static float MAX_VEL = 15f;

    public NewGameObject(IShape shape, float invM) {
        this.shape = shape;
        this.invM = invM;
    }

    public NewGameObject setAcceleration(Vec2 acc) {
        this.acc = acc;
        return this;
    }

    public Vec2 getVel() {
        return vel;
    }

    public IShape getShape() {
        return shape;
    }

    public float getInvM() {
        return invM;
    }

    public void setVel(Vec2 vel) {
        this.vel = vel;
    }

    public float getAngleVel() {
        return angleVel;
    }

    public void updateVel(float dt) {
        vel.plus(acc.mulEq(dt));
//        adjustVelocity();
    }

    public void updatePos(float dt) {
        shape.move(vel.mulEq(dt));
    }

    public void applyImpulse(Vec2 impulse) {
        vel.plus(impulse.mulEq(invM));
//        adjustVelocity();
    }

    private void adjustVelocity() {
        float len = vel.len();
        if (len > MAX_VEL) {
            vel.mul(MAX_VEL / len);
        }
    }

    @Override
    public void draw(IDrawContext drawContext) {
        shape.draw(drawContext);
    }
}
