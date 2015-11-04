package game.engine.physics;

import game.engine.geometry.collision.Collision;
import game.engine.myutils.Matrix;

public class ContactConstraint extends Constraint {
    private Collision c;
    private float dt;

    public ContactConstraint(PhysicsObject po1, PhysicsObject po2, Collision c, float dt) {
        super(po1, po2);
        this.c = c;
        this.dt = dt;
    }

    private Matrix calculateAdjustment() {
        Matrix d1 = Matrix.getLinComb((new Matrix(po1.getV())).mul(-1f), po2.getV(), 1f, 1f);
        Matrix av1 = new Matrix(1, 3);
        av1.set(2, po1.getAV());
        Matrix av2 = new Matrix(1, 3);
        av2.set(2, po2.getAV());
        Matrix d2 = Matrix.getLinComb(Matrix.getCrossProduct(av1, Matrix.convert(c.getContactVector(0))).mul(-1f),
                Matrix.getCrossProduct(av2, Matrix.convert(c.getContactVector(1))), 1f, 1f);
        Matrix answ = new Matrix(1, 1);
        answ.set(0, Math.max(Matrix.getScalarProduct(Matrix.getLinComb(Matrix.convert(d1), d2, 1f, 1f), Matrix.convert(c.getNormal())) - 0.1f, 0) * 0.2f);
        return answ;
    }

    @Override
    protected Matrix createJacobian() {
        return PhysicsMatrixFactory.createSimpleJacobian(c.getNormal(), c.getContactVector(0), c.getContactVector(1));
    }

    @Override
    protected Matrix createB() {
//        Matrix transposeJ = (new Matrix(getJacobian())).transpose();
//        return Matrix.mul(M, transposeJ).mul(calculateLyambda());
//        Matrix normal = new Matrix(c.getNormal());
//        return normal.mul(c.getPenetrationDepth() * (1f / dt));
        Matrix b = new Matrix(1, 1);
        b.set(0, Math.max(c.getPenetrationDepth() - 0.1f, 0) * (1f / dt)).mul(0.1f);
        return b.applyLinearCombination(calculateAdjustment(), 1f, 1f);
    }
}
