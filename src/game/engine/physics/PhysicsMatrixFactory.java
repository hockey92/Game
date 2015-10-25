package game.engine.physics;

import game.engine.myutils.Matrix;

public class PhysicsMatrixFactory {

    public static Matrix createSimpleJacobian(Matrix normal, Matrix r1, Matrix r2) {
        Matrix jacobian = new Matrix(1, 6);
        float[][] values = {{-normal.get(0), -normal.get(1), Matrix.getCrossProduct((new Matrix(r1)).mul(-1f), normal),
                normal.get(0), normal.get(1), Matrix.getCrossProduct(r2, normal)}};
        return jacobian;
    }

    public static Matrix createMassMatrix(PhysicsObject p1, PhysicsObject p2) {
        Matrix massMatrix = new Matrix(6, 6);
        float[] mainDiag = {p1.getInvM(), p1.getInvM(), p1.getInvI(),
                p2.getInvM(), p2.getInvM(), p2.getInvI()};
        massMatrix.setMainDiag(mainDiag);
        return massMatrix;
    }

    public static Matrix createVelocityMatrix(PhysicsObject p1, PhysicsObject p2) {
        Matrix velocityMatrix = new Matrix(1, 6);
        float[][] values = {{p1.getV().get(0), p1.getV().get(1), p1.getAV(),
                p2.getV().get(0), p2.getV().get(1), p2.getAV()}};
        return velocityMatrix;
    }

}
