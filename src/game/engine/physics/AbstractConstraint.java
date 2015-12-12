package game.engine.physics;

import game.engine.myutils.Matrix;

abstract public class AbstractConstraint implements IConstraint {
    protected IPhysicsObject po1;
    protected IPhysicsObject po2;
    private Matrix J = null;
    private Matrix b = null;
    protected Matrix M;
    private float totalImpulse = 0;

    public AbstractConstraint(IPhysicsObject po1, IPhysicsObject po2) {
        this.po1 = po1;
        this.po2 = po2;
        this.M = PhysicsMatrixUtils.createMassMatrix(po1, po2);
    }

    protected Matrix getJacobian() {
        if (J == null) {
            J = createJacobian();
        }
        return J;
    }

    private Matrix getB() {
        if (b == null) {
            b = createB();
        }
        return b;
    }


    abstract protected Matrix createJacobian();

    abstract protected Matrix createB();

    protected float clamp(float lyambda) {
        return lyambda;
    }

    protected float calculateLyambda() {
        Matrix J = getJacobian();
        Matrix transposeJ = J.transpose();
        Matrix V = PhysicsMatrixUtils.createVelocityMatrix(po1, po2);
        return -Matrix.mul(V, transposeJ).plus(getB()).get(0) / Matrix.mul(Matrix.mul(J, M), transposeJ).get(0);
//        return -Matrix.mul(V, transposeJ).get(0) / Matrix.mul(Matrix.mul(J, M), transposeJ).get(0);
    }

    @Override
    public void fix() {
        float lyambda = calculateLyambda();
//        System.out.println("lyambda = " + lyambda);
        float oldImpulse = totalImpulse;
        totalImpulse = clamp(oldImpulse + lyambda);//oldImpulse + lyambda > 0 ? 0 : oldImpulse + lyambda;
        lyambda = totalImpulse - oldImpulse;
        Matrix J = getJacobian();
        Matrix dV = Matrix.mul(M, J.transpose()).mul(lyambda);
        IPhysicsObject[] pos = {po1, po2};
        for (int i = 0; i < 2; i++) {
            pos[i].applyVelocityFix(Matrix.createCoords(dV.get(6 * i), dV.get(6 * i + 1)), dV.get(6 * i + 5));
        }
    }
}
