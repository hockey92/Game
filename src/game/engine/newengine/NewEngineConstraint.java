package game.engine.newengine;

import game.engine.gamefield.IDrawContext;
import game.engine.gamefield.IDrawable;
import game.engine.physics.IConstraint;

public class NewEngineConstraint implements IConstraint, IDrawable {

    private float d2 = 0;

    private NewGameObject o1;
    private NewGameObject o2;
    private Collision c;
    private float totalImpulse = 0f;
    private Vec2 center1;
    private Vec2 center2;
    private float invM;
    private float coeff1;
    private float coeff2;

    public NewEngineConstraint(Collision c, NewGameObject o1, NewGameObject o2) {
        this.c = c;
        this.o1 = o1;
        this.o2 = o2;

        coeff1 = 0;//o1.getInvI() * Vec2.getCrossProd(c.getR1(), c.getN());
        coeff2 = 0;//o2.getInvI() * Vec2.getCrossProd(c.getR2(), c.getN());

        invM = 1 / (o1.getInvM() + o2.getInvM()/* + Vec2.getDotProd(Vec2.getCrossProd(coeff1, c.getR1()).plus(Vec2.getCrossProd(coeff2, c.getR2())), c.getN())*/);

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
        float j = ImpulseFactory.createImpulse(o1, o2, c, invM);
        float oldImpulse = totalImpulse;
        totalImpulse = clamp(oldImpulse + j);
        j = totalImpulse - oldImpulse;
//        if (j > 0) {
        o1.applyImpulse(c.getN().mulEq(-j * o1.getInvM()), -j * coeff1);
        o2.applyImpulse(c.getN().mulEq(j * o2.getInvM()), j * coeff2);
//        }
    }

    @Override
    public void draw(IDrawContext drawContext) {
//        if (c.getPenetrationDepth() >= 0) {
//            Vec2 p1 = o1.getShape().getCenter().plusEq(c.getR1()).mul(100f);
//            drawContext.drawCircle(p1.get(0), p1.get(1), 2f);
//
//            Vec2 p2 = o2.getShape().getCenter().plusEq(c.getR2()).mul(100f);
//            drawContext.drawCircle(p2.get(0), p2.get(1), 2f);
//        }
    }
}
