package game.engine.geometry.collision;

import game.engine.gamefield.DrawContext;
import game.engine.gamefield.Drawable;
import game.engine.geometry.figures.ConvexPolygon;
import game.engine.myutils.Matrix;
import game.engine.myutils.Pair;

import java.util.Arrays;
import java.util.List;

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
                break;
            default:
                throw new Exception("CSOEdge must contains one or two edges from convex polygons.");
        }

        System.out.println(theNearestVertexNumber);
        System.out.println(normal.get(0) + " " + normal.get(1));
    }

    private void calculateEdgeToPointContact(CSO cso, int theNearestVertexNumber, Matrix point, ConvexPolygon[] ps) {
        Line perpendicularLine = new Line(point, (new Matrix(point)).applyLinearCombination(normal, 1, 1));
        mutualPoint = Line.getMutualPoint(perpendicularLine, cso.getLine(theNearestVertexNumber));
        List<Pair<Integer, Integer>> csoEdge = cso.getCSOEdge(theNearestVertexNumber);

        if (csoEdge.get(0).a == 0) {
            Matrix csoPoint = cso.getRealCoords(theNearestVertexNumber);
            Matrix d = Matrix.getLinearCombination(mutualPoint, csoPoint, 1f, -1f);
            firstVector = Matrix.getLinearCombination(ps[0].getRealCoords(csoEdge.get(0).b), d, 1f, 1f);
            secondVector = Matrix.getLinearCombination(firstVector, Matrix.getMul(normal, penetrationDepth), 1f, 1f);
        } else {
            Matrix csoPoint = cso.getRealCoords((theNearestVertexNumber + 1) % cso.getVerticesCount());
            Matrix d = Matrix.getLinearCombination(mutualPoint, csoPoint, 1f, -1f);
            firstVector = Matrix.getLinearCombination(ps[1].getRealCoords((csoEdge.get(0).b + 1) % ps[1].getVerticesCount()), d, 1f, -1f);
            secondVector = Matrix.getLinearCombination(firstVector, Matrix.getMul(normal, penetrationDepth), 1f, -1f);
        }
    }

    private void calculateEdgeToEdgeContact() {

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
            drawContext.drawCircle(mutualPoint.get(0), mutualPoint.get(1), 2f);
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
