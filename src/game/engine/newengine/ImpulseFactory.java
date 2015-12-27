package game.engine.newengine;

public class ImpulseFactory {

    public static float createImpulse(NewGameObject o1, NewGameObject o2, Collision c) {

        Vec2 vr = o2.getVel().minusEq(o1.getVel());
        float vrn = Vec2.getDotProd(vr, c.getN());

        float penetrationSlop = 0.2f;

        if (c.getPenetrationDepth() < 0) {
            System.err.println(c.getPenetrationDepth());
        }

        return (((vrn < 1.0f ? 0f : -0.4f) + -1f) * vrn + ((c.getPenetrationDepth() < penetrationSlop ? 0f : 0.2f) * (c.getPenetrationDepth() - penetrationSlop) / NewEngineConstants.dt)) / (o1.getInvM() + o2.getInvM());
    }
}
