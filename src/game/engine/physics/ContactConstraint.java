package game.engine.physics;

import game.engine.geometry.collision.ICollision;
import game.engine.myutils.Matrix;

public class ContactConstraint extends AbstractConstraint {
    private ICollision c;
    private float dt;

    public ContactConstraint(IPhysicsObject po1, IPhysicsObject po2, ICollision c, float dt) {
        super(po1, po2);
        this.c = c;
        this.dt = dt;
    }

    private Matrix calculateAdjustment() {
        Matrix d1 = po2.getV().minusEq(po1.getV());
        Matrix av1 = new Matrix(1, 3);
        av1.set(2, po1.getAV());
        Matrix av2 = new Matrix(1, 3);
        av2.set(2, po2.getAV());
        Matrix d2 = Matrix.getCrossProduct(av2, Matrix.convert(c.getContactVector(1))).minusEq(Matrix.getCrossProduct(av1, Matrix.convert(c.getContactVector(0))));
        Matrix answ = new Matrix(1, 1);
        answ.set(0, Math.max(Matrix.getScalarProduct(Matrix.convert(d1).plusEq(d2), Matrix.convert(c.getNormal())) - 0.09f, 0) * 0.3f);
        return answ;
    }

    @Override
    protected Matrix createJacobian() {
        return PhysicsMatrixUtils.createSimpleJacobian(c.getNormal(), c.getContactVector(0), c.getContactVector(1));
    }

    @Override
    protected Matrix createB() {
        Matrix b = new Matrix(1, 1);
        b.set(0, -Math.max(c.getPenetrationDepth() - 0.5f, 0) * (1f / dt)).mul(0.4f);
        return b.plus(calculateAdjustment());
    }

    @Override
    protected float clamp(float lyambda) {
        return lyambda < 0 ? 0 : lyambda;
    }
}
