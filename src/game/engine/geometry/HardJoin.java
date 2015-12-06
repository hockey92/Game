package game.engine.geometry;

import game.engine.myutils.MathUtils;
import game.engine.myutils.Matrix;

import java.util.ArrayList;

public class HardJoin extends GeometryObject {

    private PolarCoords parendPolarCoords = null; // (angle, r)
    private ArrayList<PolarCoords> childrenPolarCoords = new ArrayList<PolarCoords>();
    private ArrayList<Float> anglesBetweenParentAndChild = new ArrayList<Float>();

    public HardJoin(GeometryObject parent, PolarCoords parendPolarCoords) {
        this.parent = parent;
        this.parendPolarCoords = parendPolarCoords;
    }

    @Override
    public void addChild(GeometryObject gObject) {
        throw new UnsupportedOperationException("You can't use addChild(GeometryObject gObject) in HardJoin. Use addChild(GeometryObject gObject, Matrix childPolarCoords) instead.");
    }

    public void addChild(GeometryObject gObject, PolarCoords childPolarCoords) {
        super.addChild(gObject);
        childrenPolarCoords.add(childPolarCoords);
        anglesBetweenParentAndChild.add(0f);
    }

    @Override
    protected void updateThisOne() {
        for (int i = 0; i < childCount(); i++) {
            float angleBetweenParentAndChild = anglesBetweenParentAndChild.get(i);
            PolarCoords childPolarCoords = childrenPolarCoords.get(i);
            GeometryObject child = children.get(i);

            float parentAngle = parent.getShape().getAngle() + parendPolarCoords.getAngle();
            Matrix relationParentPoint = PolarCoords.createCartesianCoords(parentAngle, parendPolarCoords.getR());

            float childAngle = parentAngle + angleBetweenParentAndChild;
            Matrix relationChildPoint = PolarCoords.createCartesianCoords(childAngle, childPolarCoords.getR());

            child.getShape().setCenterOfMass(parent.getShape().getRealCoords(relationParentPoint.applyLinComb(relationChildPoint, 1f, 1f)));

            float adjustedAngle = -child.getShape().getAngle() - childPolarCoords.getAngle() + childAngle + MathUtils.PI;
            child.getShape().setAngle(adjustedAngle);
        }
    }
}
