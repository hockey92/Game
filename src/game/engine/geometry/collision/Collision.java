package game.engine.geometry.collision;

import game.engine.gamefield.DrawContext;
import game.engine.gamefield.Drawable;
import game.engine.geometry.figures.ConvexPolygon;
import game.engine.myutils.Matrix;
import game.engine.myutils.Pair;

import java.util.*;

public class Collision implements Drawable {
    private boolean objectsArePenetrated = false;
    private float penetrationDepth;
    private Matrix normal;
    private Matrix point;
    private CSO cso;
    private Matrix mutualPoint;
    private Matrix firstVector = Matrix.createCoords(0.0f, 0.0f);
    private Matrix secondVector = Matrix.createCoords(0.0f, 0.0f);

    public Collision() {

    }

    public Collision(ConvexPolygon p1, ConvexPolygon p2) throws Exception {
        calculateCollision(p1, p2);
    }

    public float getPenetrationDepth() {
        return penetrationDepth;
    }

    public boolean isCollision() {
        return objectsArePenetrated;
    }

    public boolean checkBroadPhase(ConvexPolygon p1, ConvexPolygon p2) {
        return true;
    }

    public void calculateCollision(ConvexPolygon p1, ConvexPolygon p2) throws Exception {
        objectsArePenetrated = false;

        if (!checkBroadPhase(p1, p2)) {
            return;
        }

        cso = new CSO(p1, p2);
        point = p2.getCenterOfMass().applyLinearCombination(p1.getCenterOfMass(), 1f, -1f);

        int theNearestVertexNumber = 0;
        penetrationDepth = cso.getLine(0).getDistanceToPoint(point);

        for (int i = 0; i < cso.getVerticesCount(); i++) {
            float distance = cso.getLine(i).getDistanceToPoint(point);
            if (distance < 0) {
                return;
            } else if (penetrationDepth > distance) {
                penetrationDepth = distance;
                theNearestVertexNumber = i;
            }
        }

        objectsArePenetrated = true;
        normal = cso.getLine(theNearestVertexNumber).getNormal();

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
                throw new Exception("CSOEdge must contains one or two edges from convex polygons.");
        }

//        System.out.println(theNearestVertexNumber);
//        System.out.println(normal.get(0) + " " + normal.get(1));
    }

    private void calculateEdgeToPointContact(CSO cso, int theNearestVertexNumber, Matrix point, ConvexPolygon[] ps) {
        Line perpendicularLine = new Line(point, (new Matrix(point)).applyLinearCombination(normal, 1, 1));
        mutualPoint = Line.getMutualPoint(perpendicularLine, cso.getLine(theNearestVertexNumber));
        List<Pair<Integer, Integer>> csoEdge = cso.getCSOEdge(theNearestVertexNumber);

        float[] coeffs = {1f, -1f};
        int plgNum = csoEdge.get(0).a; // polygon number
        int vrtNum = csoEdge.get(0).b; // vertex number

        Matrix csoPoint = cso.getRealCoords(theNearestVertexNumber + plgNum < cso.getVerticesCount() ? theNearestVertexNumber + plgNum : 0);
        Matrix d = Matrix.getLinearCombination(mutualPoint, csoPoint, 1f, -1f); // relative shift
        firstVector = Matrix.getLinearCombination(ps[plgNum].getRealCoords(vrtNum + plgNum < ps[plgNum].getVerticesCount() ? vrtNum + plgNum : 0), d, 1f, coeffs[plgNum]);
        secondVector = Matrix.getLinearCombination(firstVector, Matrix.getMul(normal, penetrationDepth), 1f, coeffs[plgNum]);
    }

    private void calculateEdgeToEdgeContact(CSO cso, int theNearestVertexNumber, ConvexPolygon[] ps) {
        List<Pair<Integer, Integer>> csoEdge = cso.getCSOEdge(theNearestVertexNumber);
        List<Pair<Matrix, Line>> pointsAndLines = new ArrayList<Pair<Matrix, Line>>(4);

        int coeffs[][] = new int[2][2];

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                int vertexNumber = csoEdge.get(i).b + j;
                coeffs[csoEdge.get(i).a][j] = vertexNumber == ps[csoEdge.get(i).a].getVerticesCount() ? 0 : vertexNumber;
            }
        }

        Line firstPLine = new Line(ps[0].getRealCoords(coeffs[0][0]), ps[0].getRealCoords(coeffs[0][1]));

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                Matrix currPoint = ps[i].getRealCoords(coeffs[i][j]);
                Line currLine = new Line(currPoint, (new Matrix(currPoint)).applyLinearCombination(normal, 1, 1));
                pointsAndLines.add(new Pair<Matrix, Line>(Line.getMutualPoint(firstPLine, currLine), currLine));
            }
        }

        Collections.sort(pointsAndLines, new EdgeToEdgeContactPairComparator());

        firstVector = Matrix.getLinearCombination(pointsAndLines.get(1).a, Matrix.getLinearCombination(pointsAndLines.get(2).a, pointsAndLines.get(1).a, 1f, -1f).mul(0.5f), 1f, 1f);
        secondVector = Matrix.getLinearCombination(firstVector, Matrix.getMul(normal, penetrationDepth), 1f, 1f);
    }

    private static class EdgeToEdgeContactPairComparator implements Comparator<Pair<Matrix, Line>> {

        @Override
        public int compare(Pair<Matrix, Line> obj1, Pair<Matrix, Line> obj2) {
            return (int) Math.signum(obj1.b.getValueOfExpression(obj2.a));
        }
    }

    @Override
    public void draw(DrawContext drawContext) {
        Matrix d = Matrix.createCoords(400f, 300f);
        cso.move(d.get(0), d.get(1));

        cso.draw(drawContext);

        cso.move(-d.get(0), -d.get(1));

        if (objectsArePenetrated) {
            mutualPoint.applyLinearCombination(d, 1f, 1f);
            point.applyLinearCombination(d, 1f, 1f);
//            drawContext.drawCircle(mutualPoint.get(0), mutualPoint.get(1), 2f);
            drawContext.drawCircle(point.get(0), point.get(1), 2f);
            drawContext.drawCircle(firstVector.get(0), firstVector.get(1), 2f);
            drawContext.drawCircle(secondVector.get(0), secondVector.get(1), 2f);
            mutualPoint.applyLinearCombination(d, 1f, -1f);
            point.applyLinearCombination(d, 1f, -1f);

//            Matrix shiftPoint = Matrix.getLinearCombination(point, d, 1f, 1f);
//            drawContext.drawCircle(shiftPoint.get(0), shiftPoint.get(1), 4f);
//            shiftPoint.applyLinearCombination(Matrix.mul(normal, -penetrationDepth), 1f, 1f);
//            drawContext.drawCircle(shiftPoint.get(0), shiftPoint.get(1), 4f);
        }
    }
}
