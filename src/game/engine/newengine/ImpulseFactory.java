package game.engine.newengine;

public class ImpulseFactory {

    private final static float penetrationSlop = 0.01f;
    private final static float velocitySlop = 0f;
    private final static float beta = 0.1f;
    private final static float alpha = 0.4f;

    public static float createImpulse(NewGameObject o1, NewGameObject o2, Collision c, float invM) {

        Vec2 v1 = o1.getVel().plusEq(Vec2.getCrossProd(o1.getAngleVel(), c.getR1()));
        Vec2 v2 = o2.getVel().plusEq(Vec2.getCrossProd(o2.getAngleVel(), c.getR2()));

        Vec2 vr = v2.minusEq(v1);
        float vrn = Vec2.getDotProd(vr, c.getN());

//        System.err.println(vrn);

        vrn -= c.getPenetrationDepth() / Constants.dt;

//        if (vrn >= 0) {
//            return 0;
//        }

//        if (c.getPenetrationDepth() >= 0) {
        return (-1f * vrn /*+ getB(c, vrn)*/) * invM;
//        } else {
//            vrn -= c.getPenetrationDepth() / Constants.dt;
//            if (vrn >= 0) {
//                return 0;
//            }
//            return -1f * vrn / (o1.getInvM() + o2.getInvM());
//        }
    }

    private static float getB(Collision c, float vrn) {
//        return alpha * Math.max(-vrn - velocitySlop, 0f)/* + beta * (c.getPenetrationDepth() - penetrationSlop < 0 ? 0 : c.getPenetrationDepth()) / Constants.dt*/;
//        if (c.getPenetrationDepth() <= 0) {
//            return 0;
//        }
        return /*alpha * Math.max(-vrn - velocitySlop, 0f) + */beta * Math.max(c.getPenetrationDepth() - penetrationSlop, 0) / Constants.dt;
    }

    private Vec2 getPointVel() {
        return null;
    }
}
