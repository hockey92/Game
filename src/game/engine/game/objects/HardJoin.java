package game.engine.game.objects;

import game.engine.myutils.Matrix;
import test.Main;

import java.util.ArrayList;

public class HardJoin extends GameObject {
    private Matrix polarCoordsOfParentJoinPoint;
    private ArrayList<Matrix> polarCoordsOfChildJoinPoints;
    private ArrayList<Float> anglesBetweenParentAndChild;

    HardJoin(GameObject parent) {
        this.parent = parent;
    }

    @Override
    protected void updateThisOne() {
//        Matrix realCoordsOfParentJoin = Matrix.createCoords(0.0f, 0.0f);
//        float r = polarCoordsOfParentJoinPoint.get(0);
//        float phi = polarCoordsOfParentJoinPoint.get(1);
//        float angle = parent.getShape().getAngle();
//        Matrix centerOfMass = parent.getShape().getCenterOfMass();
//        realCoordsOfParentJoin.setCoords(r * (float)Math.cos(phi + angle) + centerOfMass.get(0), r * (float)Math.sin(phi + angle) + centerOfMass.get(1));
//        Matrix.getRotateMatrix(phi)
    }
}
