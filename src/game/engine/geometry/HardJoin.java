package game.engine.geometry;

import game.engine.myutils.Matrix;

import java.util.ArrayList;

public class HardJoin extends GeometryObject {

    private Matrix polarCoordsOfParentJoinPoint = Matrix.createCoords(0f, 20f); // (angle, r)
    private ArrayList<Matrix> polarCoordsOfChildJoinPoints = new ArrayList<Matrix>();
    private ArrayList<Float> anglesBetweenParentAndChild = new ArrayList<Float>();

    public HardJoin(GeometryObject parent) {
        this.parent = parent;
        polarCoordsOfChildJoinPoints.add(Matrix.createCoords((float) Math.PI, 20f));
        anglesBetweenParentAndChild.add(0.3f);
    }

    @Override
    protected void updateThisOne() {
        float parentAngle = parent.getShape().getAngle() + polarCoordsOfParentJoinPoint.get(0);
        Matrix relParPoint = Matrix.convertPolarCoords(
                parentAngle,
                polarCoordsOfParentJoinPoint.get(1)
        );
        float childAngle = parentAngle + anglesBetweenParentAndChild.get(0);
        Matrix relChPoint = Matrix.convertPolarCoords(
                childAngle,
                polarCoordsOfChildJoinPoints.get(0).get(1)
        );
        children.get(0).getShape().setCenterOfMass(parent.getShape().getRealCoords(relParPoint.applyLinComb(relChPoint, 1f, 1f)));
        float adjust = -children.get(0).getShape().getAngle() - polarCoordsOfChildJoinPoints.get(0).get(0) + childAngle + (float) Math.PI;
        children.get(0).getShape().setAngle(adjust);
    }

}
