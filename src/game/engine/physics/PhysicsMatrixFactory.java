package game.engine.physics;

import game.engine.myutils.Matrix;

public class PhysicsMatrixFactory {
    public static Matrix createSimpleJacobian(Matrix normal, Matrix r1, Matrix r2) {
        Matrix jacobian = new Matrix(1, 12);
        Matrix n3 = Matrix.convert(normal);
        Matrix r31 = Matrix.convert(r1);
        Matrix r32 = Matrix.convert(r2);
        Matrix cp1 = Matrix.getCrossProduct(r31.mul(-1f), n3);
        Matrix cp2 = Matrix.getCrossProduct(r32, n3);
        float[][] values = {{-normal.get(0), -normal.get(1), 0, cp1.get(0), cp1.get(1), cp1.get(2),
                normal.get(0), normal.get(1), 0, cp2.get(0), cp2.get(1), cp2.get(2)}};
        jacobian.setValues(values);
        return jacobian;
    }

    public static Matrix createMassMatrix(PhysicsObject p1, PhysicsObject p2) {
        Matrix massMatrix = new Matrix(12, 12);
        float[] mainDiag = {p1.getInvM(), p1.getInvM(), p1.getInvM(), 0, 0, p1.getInvI(),
                p2.getInvM(), p2.getInvM(), p2.getInvM(), 0, 0, p2.getInvI()};
        massMatrix.setMainDiag(mainDiag);
        return massMatrix;
    }

    public static Matrix createVelocityMatrix(PhysicsObject p1, PhysicsObject p2) {
        Matrix velocityMatrix = new Matrix(1, 12);
        float[][] values = {{p1.getV().get(0), p1.getV().get(1), 0, 0, 0, p1.getAV(),
                p2.getV().get(0), p2.getV().get(1), 0, 0, 0, p2.getAV()}};
        velocityMatrix.setValues(values);
        return velocityMatrix;
    }
}
