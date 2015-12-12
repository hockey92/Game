package game.engine.physics;

import game.engine.geometry.collision.ICollision;
import game.engine.myutils.Matrix;

public class PhysicsMatrixUtils {

//    public static Matrix createCollisionJacobian(ICollision c) {
//        Matrix jacobian = new Matrix(1, 12);
//        Matrix n3 = Matrix.convert(normal);
//        Matrix cp1 = Matrix.getCrossProduct(Matrix.convert(r1).mul(-1f), n3);
//        Matrix cp2 = Matrix.getCrossProduct(Matrix.convert(r2), n3);
//        float[][] values = {
//                {-normal.get(0), -normal.get(1), 0, cp1.get(0), cp1.get(1), cp1.get(2), normal.get(0), normal.get(1), 0, cp2.get(0), cp2.get(1), cp2.get(2)}
//        };
//        jacobian.setValues(values);
//        return jacobian;
//    }

    public static Matrix createSimpleJacobian(Matrix normal, Matrix r1, Matrix r2) {
        Matrix jacobian = new Matrix(1, 12);
        Matrix n3 = Matrix.convert(normal);
        Matrix cp1 = Matrix.getCrossProduct(Matrix.convert(r1).mul(-1f), n3);
        Matrix cp2 = Matrix.getCrossProduct(Matrix.convert(r2), n3);
        float[][] values = {
                {-normal.get(0), -normal.get(1), 0, cp1.get(0), cp1.get(1), cp1.get(2), normal.get(0), normal.get(1), 0, cp2.get(0), cp2.get(1), cp2.get(2)}
        };
        jacobian.setValues(values);
        return jacobian;
    }

    public static Matrix createMassMatrix(IPhysicsObject p1, IPhysicsObject p2) {
        Matrix massMatrix = new Matrix(12, 12);
        float[] mainDiag = {p1.getInvM(), p1.getInvM(), p1.getInvM(), 0, 0, p1.getInvI(), p2.getInvM(), p2.getInvM(), p2.getInvM(), 0, 0, p2.getInvI()};
        massMatrix.setMainDiag(mainDiag);
        return massMatrix;
    }

    public static Matrix createVelocityMatrix(IPhysicsObject p1, IPhysicsObject p2) {
        Matrix velocityMatrix = new Matrix(1, 12);
        float[][] values = {
                {p1.getV().get(0), p1.getV().get(1), 0, 0, 0, p1.getAV(), p2.getV().get(0), p2.getV().get(1), 0, 0, 0, p2.getAV()}
        };
        velocityMatrix.setValues(values);
        return velocityMatrix;
    }
}
