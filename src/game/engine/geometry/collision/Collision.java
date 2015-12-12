package game.engine.geometry.collision;

import game.engine.gamefield.IDrawContext;
import game.engine.gamefield.IDrawable;
import game.engine.geometry.GeometryObject;
import game.engine.geometry.figures.ConvexPolygon;
import game.engine.myutils.Matrix;
import game.engine.myutils.Pair;

import java.util.*;

public class Collision implements ICollision, IDrawable {
    private CollisionType collisionType;
    private boolean objectsArePenetrated = false;
    private float penetrationDepth;
    private Matrix normal;
    private Matrix point;
    private CSO cso;
    private Matrix mutualPoint;
    private Matrix[] contactVectors = {
            Matrix.createCoords(0.0f, 0.0f),
            Matrix.createCoords(0.0f, 0.0f),
            Matrix.createCoords(0.0f, 0.0f),
            Matrix.createCoords(0.0f, 0.0f)
    };
    private ConvexPolygon[] convexPolygons = new ConvexPolygon[2];

    public Collision(GeometryObject go1, GeometryObject go2) {
        calculateCollision(go1.getShape(), go2.getShape());
    }

    public Collision(ConvexPolygon p1, ConvexPolygon p2) {
        calculateCollision(p1, p2);
    }

    @Override
    public Matrix getContactVector(int index) {
        if (index > 3 || collisionType == CollisionType.EDGE_TO_POINT && index > 1) {
            throw new RuntimeException("Wrong index");
        }
        return contactVectors[index];
    }

    @Override
    public float getPenetrationDepth() {
        return penetrationDepth;
    }

    @Override
    public boolean isCollision() {
        return objectsArePenetrated;
    }

    public static boolean checkBroadPhase(ConvexPolygon p1, ConvexPolygon p2) {
        Matrix[] lbps = {p1.getRealCoords(p1.getLeftBottomPoint()), p2.getRealCoords(p2.getLeftBottomPoint())};
        Matrix[] rtps = {p1.getRealCoords(p1.getRightTopPoint()), p2.getRealCoords(p2.getRightTopPoint())};
        return !(lbps[0].get(0) > rtps[1].get(0) ||
                lbps[1].get(0) > rtps[0].get(0) ||
                lbps[0].get(1) > rtps[1].get(1) ||
                lbps[1].get(1) > rtps[0].get(1));
    }

    @Override
    public Matrix getNormal() {
        return normal;
    }

    @Override
    public CollisionType getCollisionType() {
        return null;
    }

    public void calculateCollision(ConvexPolygon p1, ConvexPolygon p2) {
        objectsArePenetrated = false;

        convexPolygons[0] = p1;
        convexPolygons[1] = p2;

        if (!checkBroadPhase(p1, p2)) {
            return;
        }

        cso = new CSO(p1, p2);
        point = p2.getCenterOfMass().minus(p1.getCenterOfMass());

        int theNearestVertexNumber = 0;
        penetrationDepth = cso.getLine(0).getDistanceToPoint(point);

        for (int i = 0; i < cso.getVertexCount(); i++) {
            float distance = cso.getLine(i).getDistanceToPoint(point);
            if (distance < 0) {
                return;
            } else if (penetrationDepth > distance) {
                penetrationDepth = distance;
                theNearestVertexNumber = i;
            }
        }

        objectsArePenetrated = true;
        normal = cso.getLine(theNearestVertexNumber).getNormal().mul(-1f);

        List<Pair<Integer, Integer>> csoEdge = cso.getCSOEdge(theNearestVertexNumber);

        ConvexPolygon[] ps = {p1, p2};

        switch (csoEdge.size()) {
            case 1:
                calculateEdgeToPointContact(cso, theNearestVertexNumber, point, ps);
                break;
            case 2:
                calculateEdgeToEdgeContact(cso, theNearestVertexNumber, ps);
                break;
            default:
                throw new RuntimeException("CSOEdge must contains one or two edges from convex polygons.");
        }
    }

    private void calculateEdgeToPointContact(CSO cso, int theNearestVertexNumber, Matrix point, ConvexPolygon[] ps) {
        collisionType = CollisionType.EDGE_TO_POINT;

        Line perpendicularLine = new Line(point, point.plusEq(normal));
        mutualPoint = Line.getMutualPoint(perpendicularLine, cso.getLine(theNearestVertexNumber));
        List<Pair<Integer, Integer>> csoEdge = cso.getCSOEdge(theNearestVertexNumber);

        float[] coeffs = {1f, -1f};
        int plgNum = csoEdge.get(0).a; // polygon number
        int vrtNum = csoEdge.get(0).b; // vertex number

        Matrix csoPoint = cso.getRealCoords(theNearestVertexNumber + plgNum < cso.getVertexCount() ? theNearestVertexNumber + plgNum : 0);
        Matrix d = mutualPoint.minusEq(csoPoint); // relative shift
        contactVectors[plgNum] = Matrix.getLinComb(ps[plgNum].getRealCoords(vrtNum + plgNum < ps[plgNum].getVertexCount() ? vrtNum + plgNum : 0), d, 1f, coeffs[plgNum]);
        int nxtPlgNum = plgNum + 1 > 1 ? 0 : 1;
        contactVectors[nxtPlgNum] = Matrix.getLinComb(contactVectors[plgNum], normal.mulEq(penetrationDepth), 1f, -coeffs[plgNum]);
        adjustResult(ps, collisionType);
    }

    private void calculateEdgeToEdgeContact(CSO cso, int theNearestVertexNumber, ConvexPolygon[] ps) {
        collisionType = CollisionType.EDGE_TO_EDGE;

        List<Pair<Integer, Integer>> csoEdge = cso.getCSOEdge(theNearestVertexNumber);
        List<Pair<Matrix, Line>> pointsAndLines = new ArrayList<Pair<Matrix, Line>>(4);

        int coeffs[][] = new int[2][2];

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                int vertexNumber = csoEdge.get(i).b + j;
                coeffs[csoEdge.get(i).a][j] = vertexNumber == ps[csoEdge.get(i).a].getVertexCount() ? 0 : vertexNumber;
            }
        }

        Line firstPLine = new Line(ps[0].getRealCoords(coeffs[0][0]), ps[0].getRealCoords(coeffs[0][1]));

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                Matrix currPoint = ps[i].getRealCoords(coeffs[i][j]);
                Line currLine = new Line(currPoint, currPoint.plusEq(normal));
                pointsAndLines.add(new Pair<Matrix, Line>(Line.getMutualPoint(firstPLine, currLine), currLine));
            }
        }

        Collections.sort(pointsAndLines, new EdgeToEdgeContactPairComparator());

        Matrix shift = normal.mulEq(penetrationDepth);
        contactVectors[0] = pointsAndLines.get(1).a;
        contactVectors[1] = contactVectors[0].minusEq(shift);
        contactVectors[2] = pointsAndLines.get(2).a;
        contactVectors[3] = contactVectors[2].minusEq(shift);
        adjustResult(ps, collisionType);
    }

    private void adjustResult(ConvexPolygon[] ps, CollisionType collisionType) {
        int shift[] = {0, 2};
        for (int i = 0; i < collisionType.getIndex() + 1; i++) {
            for (int j = 0; j < 2; j++) {
                contactVectors[shift[i] + j].minus(ps[j].getCenterOfMass());
            }
        }
    }

    private static class EdgeToEdgeContactPairComparator implements Comparator<Pair<Matrix, Line>> {
        @Override
        public int compare(Pair<Matrix, Line> obj1, Pair<Matrix, Line> obj2) {
            return (int) Math.signum(obj1.b.getValueOfExpression(obj2.a));
        }
    }

    @Override
    public void draw(IDrawContext IDrawContext) {
//        Matrix d = Matrix.createCoords(400f, 300f);
//        cso.move(d.get(0), d.get(1));
//
//        cso.draw(IDrawContext);
//
//        cso.move(-d.get(0), -d.get(1));

        if (objectsArePenetrated) {
//            mutualPoint.applyLinComb(d, 1f, 1f);
//            point.applyLinComb(d, 1f, 1f);
//            IDrawContext.drawCircle(mutualPoint.get(0), mutualPoint.get(1), 2f);
//            IDrawContext.drawCircle(point.get(0), point.get(1), 2f);
            for (int i = 0; i < 2; i++) {
                IDrawContext.drawCircle(convexPolygons[i].getCenterOfMass().get(0) + contactVectors[i].get(0),
                        convexPolygons[i].getCenterOfMass().get(1) + contactVectors[i].get(1), 4f);
            }
            if (collisionType == CollisionType.EDGE_TO_EDGE) {
                for (int i = 2; i < 4; i++) {
                    IDrawContext.drawCircle(convexPolygons[i % 2].getCenterOfMass().get(0) + contactVectors[i].get(0),
                            convexPolygons[i % 2].getCenterOfMass().get(1) + contactVectors[i].get(1), 4f);
                }
            }
//            mutualPoint.applyLinComb(d, 1f, -1f);
//            point.applyLinComb(d, 1f, -1f);

//            Matrix shiftPoint = Matrix.getLinComb(point, d, 1f, 1f);
//            IDrawContext.drawCircle(shiftPoint.get(0), shiftPoint.get(1), 4f);
//            shiftPoint.applyLinComb(Matrix.mul(normal, -penetrationDepth), 1f, 1f);
//            IDrawContext.drawCircle(shiftPoint.get(0), shiftPoint.get(1), 4f);
        }
    }
}
