package game.engine.newengine;

import game.engine.physics.IConstraint;

public class RemoveVelConstraint implements IConstraint {

    private NewGameObject o1;
    private NewGameObject o2;
    private Collision c;
    private float totalImpulse = 0f;

    public RemoveVelConstraint(Collision c, NewGameObject o1, NewGameObject o2) {
        this.c = c;
        this.o1 = o1;
        this.o2 = o2;
    }

    @Override
    public void fix() {
        float j = ImpulseFactory.createRemoveVelImpulse(o1, o2, c);
//        float oldImpulse = totalImpulse;
//        totalImpulse = clamp(oldImpulse + j);
//        j = totalImpulse - oldImpulse;
        if (j > 0) {
            o1.applyImpulse(c.getN().mulEq(-j));
            o2.applyImpulse(c.getN().mulEq(j));
        }
    }
}
