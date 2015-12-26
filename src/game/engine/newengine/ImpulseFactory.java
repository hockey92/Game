package game.engine.newengine;

public class ImpulseFactory {

    public static float create(NewGameObject o1, NewGameObject o2, Collision c) {

        Vec2 vr = o2.getVel().minusEq(o1.getVel());
        float vrn = Vec2.getDotProd(vr, c.getN());

        float j = -vrn / (o1.getInvM() + o2.getInvM());

        return 0;
    }
}
