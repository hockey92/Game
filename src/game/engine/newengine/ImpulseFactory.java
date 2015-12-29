package game.engine.newengine;

public class ImpulseFactory {

    private final static float penetrationSlop = 0.1f;

    public static float createImpulse(NewGameObject o1, NewGameObject o2, Collision c) {

        Vec2 vr = o2.getVel().minusEq(o1.getVel());
        float vrn = Vec2.getDotProd(vr, c.getN());

        return (((Math.abs(vrn) < 0.5f ? 0f : -0.4f) + -1f) * vrn + getB(c)) / (o1.getInvM() + o2.getInvM());
    }

    private static float getB(Collision c) {
        return (c.getPenetrationDepth() < penetrationSlop ? 0f : 0.25f) * (c.getPenetrationDepth()) / NewEngineConstants.dt;
    }
}
