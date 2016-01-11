package game.engine.newengine;

import game.engine.physics.IConstraint;

public class NewEngineConstraint implements IConstraint {

    private float d2 = 0;

    private NewGameObject o1;
    private NewGameObject o2;
    private Collision c;
    private float totalImpulse = 0f;
    private Vec2 center1;
    private Vec2 center2;

    public NewEngineConstraint(Collision c, NewGameObject o1, NewGameObject o2) {
        this.c = c;
        this.o1 = o1;
        this.o2 = o2;

        center1 = new Vec2(o1.getShape().getCenter());
        center2 = new Vec2(o2.getShape().getCenter());
    }

    public boolean isEquals() {
        if (o1.getShape().getCenter().minusEq(center1).len2() > d2 || o2.getShape().getCenter().minusEq(center2).len2() > d2) {
            return false;
        }
        return true;
    }

    public void setCollision(Collision c) {
        this.c = c;
    }

    public void setTotalImpulse(float impulse) {
        totalImpulse = impulse;
    }

    public float getTotalImpulse() {
        return totalImpulse;
    }

    private float clamp(float j) {
        return j > 0f ? j : 0.0f;
    }

    @Override
    public void fix() {
        /*float */
        float j = ImpulseFactory.createImpulse(o1, o2, c);
        float oldImpulse = totalImpulse;
        totalImpulse = clamp(oldImpulse + j);
        j = totalImpulse - oldImpulse;
//        if (j > 0) {
        o1.applyImpulse(c.getN().mulEq(-j));
        o2.applyImpulse(c.getN().mulEq(j));
//        }
    }
}
