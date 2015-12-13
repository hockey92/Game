package game.engine.physics;

import game.engine.myutils.Matrix;

abstract public class AbstractConstraint implements IConstraint {
    protected IPhysicsObject po1;
    protected IPhysicsObject po2;
    private Matrix J = null;
    private Matrix b = null;
    protected Matrix M;
    protected Matrix totalImpulse = null;

    public AbstractConstraint(IPhysicsObject po1, IPhysicsObject po2) {
        this.po1 = po1;
        this.po2 = po2;
        this.M = PhysicsMatrixUtils.createMassMatrix(po1, po2);
    }

    private Matrix getJacobian() {
        if (J == null) {
            J = createJacobian();
        }
        return J;
    }

    private Matrix getB() {
//        if (b == null) {
//            b = createB();
//        }
//        return b;
        return createB();
    }

    abstract protected Matrix createJacobian();

    abstract protected Matrix createB();

    protected float clamp(float lyambda) {
        return lyambda;
    }

    protected Matrix clamp(Matrix lyambda) {
        return lyambda;
    }

    private Matrix calculateLyambda() {
        Matrix J = getJacobian();
        Matrix V = PhysicsMatrixUtils.createVelocityMatrix(po1, po2);

        Matrix m1 = Matrix.mul(J, V.transpose()).plus(getB()).mul(-1f);
        Matrix m2 = Matrix.mul(Matrix.mul(J, M), J.transpose()).getInv();

        return Matrix.mul(m2, m1);
//        return -Matrix.mul(V, transposeJ).plus(getB()).get(0) / Matrix.mul(Matrix.mul(J, M), transposeJ).get(0);
//        return -Matrix.mul(V, transposeJ).get(0) / Matrix.mul(Matrix.mul(J, M), transposeJ).get(0);
    }

    @Override
    public void fix() {
        Matrix lyambda = calculateLyambda();
//        System.out.println("lyambda = " + lyambda);
        Matrix oldImpulse = new Matrix(totalImpulse);
//        totalImpulse = clamp(oldImpulse.plusEq(lyambda));//oldImpulse + lyambda > 0 ? 0 : oldImpulse + lyambda;
        totalImpulse = clamp(oldImpulse.plusEq(lyambda));

        lyambda = totalImpulse.minusEq(oldImpulse);

        Matrix J = getJacobian();
        Matrix dV = Matrix.mul(Matrix.mul(M, J.transpose()), lyambda);
        IPhysicsObject[] pos = {po1, po2};
        for (int i = 0; i < 2; i++) {
            pos[i].applyVelocityFix(Matrix.createCoords(dV.get(6 * i), dV.get(6 * i + 1)), dV.get(6 * i + 5));
            System.out.println(pos[i].getV() + " --- " + pos[i].getAV());
        }
    }
}
