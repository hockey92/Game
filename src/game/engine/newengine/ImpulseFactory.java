package game.engine.newengine;

public class ImpulseFactory {

    private final static float penetrationSlop = 0.1f;
    private final static float velocitySlop = 0.4f;
    private final static float beta = 1.0f;
    private final static float alpha = 0.0f;

    public static float createImpulse(NewGameObject o1, NewGameObject o2, Collision c) {

        Vec2 vr = o2.getVel().minusEq(o1.getVel());
        float vrn = Vec2.getDotProd(vr, c.getN());

//        System.err.println(vrn);

        vrn -= c.getPenetrationDepth() / NewEngineConstants.dt;

        if (vrn > 0) {
            return 0;
        }

        return (-1f * vrn/* + getB(c, vrn)*/) / (o1.getInvM() + o2.getInvM());
    }

    public static float createRemoveVelImpulse(NewGameObject o1, NewGameObject o2, Collision c) {

        Vec2 vr = o2.getVel().minusEq(o1.getVel());
        float vrn = Vec2.getDotProd(vr, c.getN()) - c.getPenetrationDepth() / NewEngineConstants.dt;

        return (-1f * vrn) / (o1.getInvM() + o2.getInvM());
    }

    private static float getB(Collision c, float vrn) {
//        return alpha * Math.max(-vrn - velocitySlop, 0f) + beta * (c.getPenetrationDepth() - penetrationSlop < 0 ? 0 : c.getPenetrationDepth()) / NewEngineConstants.dt;
        if (c.getPenetrationDepth() <= 0) {
            return 0;
        }
        return beta * Math.max(c.getPenetrationDepth() - penetrationSlop, 0) / NewEngineConstants.dt;
    }
}
