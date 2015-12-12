package game.engine.geometry.collision;

import game.engine.geometry.figures.ConvexPolygon;
import game.engine.myutils.Angle;
import game.engine.myutils.MathUtils;
import game.engine.myutils.Matrix;
import game.engine.myutils.Pair;

import java.util.*;

public class CSO extends ConvexPolygon {

    private CSOEdge[] csoEdges;
    private Line[] lines;

    public CSO(ConvexPolygon p1, ConvexPolygon p2) {
        createCSO(p1, p2);
    }

    public Line getLine(int lineNumber) {
        return lines[lineNumber];
    }

    public List<Pair<Integer, Integer>> getCSOEdge(int edgeNumber) {
        return csoEdges[edgeNumber].getEdges();
    }

    public void createCSO(ConvexPolygon p1, ConvexPolygon p2) {
        createCSOEdges(p1, p2);
        verticesCount = csoEdges.length;
        vertices = new Matrix[csoEdges.length];
        vertices[0] = Matrix.createCoords(0f, 0f);
        for (int i = 0; i < csoEdges.length - 1; i++) {
            vertices[i + 1] = csoEdges[i].getVectorCoords().plusEq(vertices[i]);
        }
        calculateOuterRectangleBorders();
        setCenterOfMass(p1.getRightTopPoint().minusEq(p2.getLeftBottomPoint()).minus(rightTopPoint));
        calculateLines();
    }

    private void createCSOEdges(ConvexPolygon p1, ConvexPolygon p2) {
        Map<Angle, CSOEdge> edgesMap = new TreeMap<Angle, CSOEdge>();
        float coeffs[][] = {{1f, -1f}, {-1f, 1f}};
        ConvexPolygon[] ps = {p1, p2};
        for (int plgNum = 0; plgNum < ps.length; plgNum++) {
            for (int vrtNum = 0; vrtNum < ps[plgNum].getVerticesCount(); vrtNum++) {
                int nextVrtNum = vrtNum + 1 == ps[plgNum].getVerticesCount() ? 0 : vrtNum + 1;
                Matrix vectorCoords = ps[plgNum].getCoords(nextVrtNum).applyLinComb(ps[plgNum].getCoords(vrtNum), coeffs[plgNum][0], coeffs[plgNum][1]);
                Angle angle = new Angle(vectorCoords);
                CSOEdge csoEdge = edgesMap.get(angle);
                if (csoEdge == null && MathUtils.PI - Math.abs(angle.getValue()) < Angle.DELTA) {
                    Angle adjustedAngle = createAdjustedAngle(angle);
                    csoEdge = edgesMap.get(adjustedAngle);
                }
                if (csoEdge == null) {
                    edgesMap.put(angle, new CSOEdge(vectorCoords, plgNum, vrtNum));
                } else {
                    csoEdge.addEdge(vectorCoords, plgNum, vrtNum);
                }
            }
        }

        csoEdges = new CSOEdge[edgesMap.size()];
        int count = 0;
        for (CSOEdge edge : edgesMap.values()) {
            csoEdges[count++] = edge;
        }
    }

    private Angle createAdjustedAngle(Angle angle) {
        float angleValue = angle.getValue();
        return new Angle(angleValue > 0 ? angleValue - MathUtils.DOUBLE_PI : angleValue + MathUtils.DOUBLE_PI);
    }

    private void calculateLines() {
        lines = new Line[verticesCount];
        for (int vertexNumber = 0; vertexNumber < verticesCount; vertexNumber++) {
            int nextVertexNumber = vertexNumber + 1 == verticesCount ? 0 : vertexNumber + 1;
            lines[vertexNumber] = new Line(getRealCoords(vertexNumber), getRealCoords(nextVertexNumber));
        }
    }

//    @Override
//    public void draw(IDrawContext drawContext) {
//        move(400, 400);
//        super.draw(drawContext);
//        move(-400, -400);
//    }

    private static class CSOEdge {
        private Matrix vectorCoords = Matrix.createCoords(0, 0);
        List<Pair<Integer, Integer>> edges = new ArrayList<Pair<Integer, Integer>>();

        public CSOEdge(Matrix vectorCoords, int polygonNumber, int vertexNumber) {
            addEdge(vectorCoords, polygonNumber, vertexNumber);
        }

        public void addEdge(Matrix vectorCoords, int polygonNumber, int vertexNumber) {
            this.vectorCoords.plus(vectorCoords);
            edges.add(new Pair<Integer, Integer>(polygonNumber, vertexNumber));
        }

        public Matrix getVectorCoords() {
            return vectorCoords;
        }

        public List<Pair<Integer, Integer>> getEdges() {
            return edges;
        }
    }
}
