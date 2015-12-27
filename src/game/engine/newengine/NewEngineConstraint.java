package game.engine.newengine;

import game.engine.physics.IConstraint;

public class NewEngineConstraint implements IConstraint {

    private NewGameObject o1;
    private NewGameObject o2;
    private Collision c;

    public NewEngineConstraint(Collision c, NewGameObject o1, NewGameObject o2) {
        this.c = c;
        this.o1 = o1;
        this.o2 = o2;
    }

    @Override
    public void fix() {
        float j = ImpulseFactory.createImpulse(o1, o2, c);
        if (j > 0) {
            o1.applyImpulse(c.getN().mulEq(-j));
            o2.applyImpulse(c.getN().mulEq(j));
        }
    }
}
