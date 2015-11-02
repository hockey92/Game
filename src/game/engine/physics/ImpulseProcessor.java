package game.engine.physics;

import game.engine.geometry.collision.Collision;
import game.engine.myutils.Matrix;

public class ImpulseProcessor {
//    private Matrix getImpulseMagnitude(PhysicsObject p1, PhysicsObject p2, Collision c) {
//        if (c != null && c.isCollision()) {
//            Matrix[] pv = {
//                    calculatePointVelocity(p1.getV(), p1.getAV(), c.getContactVector(0)),
//                    calculatePointVelocity(p2.getV(), p2.getAV(), c.getContactVector(1))
//            };
//            Matrix relationPointVelocity = Matrix.getLinComb(pv[0], pv[1], -1f, 1f);
//            float numerator = -(1f + 0.5f) * Matrix.getScalarProduct(relationPointVelocity, c.getNormal());
//            float denominator = calculateDenominator(p1, p2, c);
//            if (denominator == 0) {
//                return null;
//            }
//            return (new Matrix(c.getNormal())).mul(numerator / denominator);
//        }
//        return null;
//    }
//
//    private Matrix calculatePointVelocity(Matrix v, float aV, Matrix contactVector) {
//        return Matrix.getLinComb(v, Matrix.getCrossProduct(aV, contactVector), 1f, 1f);
//    }
//
//    private float calculateDenominator(PhysicsObject p1, PhysicsObject p2, Collision c) {
//        PhysicsObject[] ps = {p1, p2};
//        Matrix t[] = new Matrix[2];
//        for (int i = 0; i < 2; i++) {
//            t[i] = Matrix.getCrossProduct(Matrix.getCrossProduct(c.getContactVector(i), c.getNormal()), c.getContactVector(i));
//            t[i].mul(ps[i].getInvI());
//        }
//        return ps[0].getInvM() + ps[1].getInvM() + Matrix.getCrossProduct(Matrix.getLinComb(t[0], t[1], 1f, 1f), c.getNormal());
//    }
}
