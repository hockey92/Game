package game.engine.physics;

import game.engine.myutils.Matrix;

abstract public class Constraint {
    protected PhysicsObject po1;
    protected PhysicsObject po2;
    private Matrix J = null;
    private Matrix b = null;
    protected Matrix V;
    protected Matrix M;
    private float totalImpulse = 0;

    public Constraint(PhysicsObject po1, PhysicsObject po2) {
        this.po1 = po1;
        this.po2 = po2;
        this.M = PhysicsMatrixFactory.createMassMatrix(po1, po2);
        this.V = PhysicsMatrixFactory.createVelocityMatrix(po1, po2);
    }

    public Matrix getJacobian() {
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

    protected float calculateLyambda() {
        Matrix J = getJacobian();
        Matrix transposeJ = (new Matrix(J)).transpose();
        return Matrix.getLinComb(Matrix.mul(V, transposeJ), getB(), -1f, -1f).get(0) / Matrix.mul(Matrix.mul(J, M), transposeJ).get(0);
//        return -Matrix.mul(V, transposeJ).get(0) / Matrix.mul(Matrix.mul(J, M), transposeJ).get(0);
    }

    public void fix() {
        float lyambda = calculateLyambda();
//        System.err.println("lyambda = " + lyambda);
        float oldImpulse = totalImpulse;
        totalImpulse = oldImpulse + lyambda > 0 ? 0 : oldImpulse + lyambda;
        lyambda = totalImpulse + oldImpulse;
//        if (lyambda > 0) return;
        Matrix J = getJacobian();
        Matrix transposeJ = (new Matrix(J)).transpose();
        Matrix dV = Matrix.mul(M, transposeJ).mul(lyambda);
        PhysicsObject[] pos = {po1, po2};
        for (int i = 0; i < 2; i++) {
            pos[i].applyVelocityFix(Matrix.createCoords(dV.get(6 * i), dV.get(6 * i + 1)), dV.get(6 * i + 5));
        }
    }
}
