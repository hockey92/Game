package game.engine.geometry;

import game.engine.myutils.CoordsUtils;
import game.engine.myutils.MathUtils;
import game.engine.myutils.Matrix;

import java.util.ArrayList;

public class HardJoin extends GeometryObject {

    private PolarCoords parendPolarCoords = null; // (angle, r)
    private ArrayList<PolarCoords> childrenPolarCoords = new ArrayList<PolarCoords>();
    private ArrayList<Value> anglesBetweenParentAndChild = new ArrayList<Value>();

    public HardJoin(GeometryObject parent, PolarCoords parentPolarCoords) {
        this.parent = parent;
        this.parendPolarCoords = parentPolarCoords;
    }

    @Override
    public void addChild(GeometryObject gObject) {
        throw new UnsupportedOperationException("You can't use addChild(GeometryObject gObject) in HardJoin. Use addChild(GeometryObject gObject, Matrix childPolarCoords) instead.");
    }

    public void setAngleBetweenParentAndChild(int index, float angle) {
        if (index > anglesBetweenParentAndChild.size()) {
            throw new RuntimeException("Wrong index");
        }

        anglesBetweenParentAndChild.get(index).setValue(angle);
    }

    public void shiftAngleBetweenParentAndChild(int index, float dAngle) {
        if (index > anglesBetweenParentAndChild.size()) {
            throw new RuntimeException("Wrong index");
        }

        anglesBetweenParentAndChild.get(index).plus(dAngle);
    }

    public void addChild(
            GeometryObject child,
            PolarCoords childPolarCoords
    ) {
        addChild(child, childPolarCoords, 0f);
    }

    public void addChild(
            GeometryObject child,
            PolarCoords childPolarCoords,
            float angleBetweenParentAndChild
    ) {
        super.addChild(child);
        childrenPolarCoords.add(childPolarCoords);
        anglesBetweenParentAndChild.add(new Value(angleBetweenParentAndChild));
    }

    @Override
    protected void updateThisOne() {
        for (int i = 0; i < childCount(); i++) {
            float angleBetweenParentAndChild = anglesBetweenParentAndChild.get(i).getValue();
            PolarCoords childPolarCoords = childrenPolarCoords.get(i);
            GeometryObject child = children.get(i);

            float parentAngle = parent.getShape().getAngle() + parendPolarCoords.getAngle();
            Matrix relationParentPoint = CoordsUtils.convertToCartesianCoords(parentAngle, parendPolarCoords.getR());

            float childAngle = parentAngle + angleBetweenParentAndChild;
            Matrix relationChildPoint = CoordsUtils.convertToCartesianCoords(childAngle, childPolarCoords.getR());

            child.getShape().setCenterOfMass(parent.getShape().getRealCoords(relationParentPoint.plus(relationChildPoint)));

            float adjustedAngle = -child.getShape().getAngle() - childPolarCoords.getAngle() + childAngle + MathUtils.PI;
            child.getShape().setAngle(adjustedAngle);
        }
    }

    private static class Value {
        private float value;

        public Value(float value) {
            this.value = value;
        }

        public Value() {
            this.value = 0;
        }

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }

        public void plus(float dValue) {
            this.value += dValue;
        }
    }
}
