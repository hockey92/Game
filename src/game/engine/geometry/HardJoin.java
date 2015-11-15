package game.engine.geometry;

import game.engine.myutils.Matrix;

import java.util.ArrayList;

public class HardJoin extends GeometryObject {

    private Matrix polarCoordsOfParentJoinPoint; // (angle, r)
    private ArrayList<Matrix> polarCoordsOfChildJoinPoints;
    private ArrayList<Float> anglesBetweenParentAndChild;

    public HardJoin(GeometryObject parent) {
        this.parent = parent;
    }

    @Override
    protected void updateThisOne() {
        float parentAngle = parent.getShape().getAngle() + polarCoordsOfParentJoinPoint.get(0);
        float parentR = polarCoordsOfParentJoinPoint.get(1);

//        Matrix relParPoint = Matrix.convertPolarCoords(parentAngle, parentR);

//        Matrix relChPoint = Matrix.convertPolarCoords(parentAngle);
    }

}
