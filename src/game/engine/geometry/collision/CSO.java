package game.engine.geometry.collision;

import game.engine.geometry.figures.ConvexPolygon;
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

    private void createCSO(ConvexPolygon p1, ConvexPolygon p2) {
        createCSOEdges(p1, p2);
        verticesCount = csoEdges.length;
        vertices = new Matrix[csoEdges.length];
        vertices[0] = Matrix.createCoords(0f, 0f);
        for (int i = 0; i < csoEdges.length - 1; i++) {
            vertices[i + 1] = Matrix.getLinComb(csoEdges[i].getVectorCoords(), vertices[i], 1, 1);
        }
        calculateOuterRectangleBorders();
        setCenterOfMass(Matrix.getLinComb(rightTopPoint, Matrix.getLinComb(p1.getRightTopPoint(), p2.getLeftBottomPoint().mul(-1f), 1f, 1f), -1f, 1f));
        calculateLines();
    }

    private void createCSOEdges(ConvexPolygon p1, ConvexPolygon p2) {
        Map<Angle, CSOEdge> sortedEdgesMap = new TreeMap<Angle, CSOEdge>();
        float coeffs[][] = {{1f, -1f}, {-1f, 1f}};
        ConvexPolygon[] ps = {p1, p2};
        for (int polygonNum = 0; polygonNum < ps.length; polygonNum++) {
            for (int vertexNum = 0; vertexNum < ps[polygonNum].getVerticesCount(); vertexNum++) {

                int nextVertexNumber = vertexNum + 1 == ps[polygonNum].getVerticesCount() ? 0 : vertexNum + 1;
                Matrix vectorCoords = ps[polygonNum].getCoords(nextVertexNumber).applyLinComb(ps[polygonNum].getCoords(vertexNum), coeffs[polygonNum][0], coeffs[polygonNum][1]);

                Angle angle = new Angle(vectorCoords);
                CSOEdge csoEdge = sortedEdgesMap.get(angle);
                if (csoEdge == null) {
                    sortedEdgesMap.put(angle, new CSOEdge(vectorCoords, polygonNum, vertexNum));
                } else {
                    csoEdge.addEdge(vectorCoords, polygonNum, vertexNum);
                }
            }
        }

        csoEdges = new CSOEdge[sortedEdgesMap.size()];
        int count = 0;
        for (CSOEdge edge : sortedEdgesMap.values()) {
            csoEdges[count++] = edge;
        }
    }

    private void calculateLines() {
        lines = new Line[verticesCount];
        for (int vertexNumber = 0; vertexNumber < verticesCount; vertexNumber++) {
            int nextVertexNumber = vertexNumber + 1 == verticesCount ? 0 : vertexNumber + 1;
            lines[vertexNumber] = new Line(getRealCoords(vertexNumber), getRealCoords(nextVertexNumber));
        }
    }

    private static class Angle implements Comparable<Angle> {
        private float value;
        private final float DELTA = 0.00001f;

        public Angle(Matrix matrix) {
            value = (float) Math.atan2(matrix.get(1), matrix.get(0));
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof Angle)) {
                return false;
            }

            Angle angle = (Angle) object;
            float diff = angle.value - value;
            return Math.abs(diff) < DELTA;
        }

        float adjustValue(float value) {
            return value < 0f ? value + 2f * MathUtils.PI : value;
        }

        @Override
        public int compareTo(Angle angle) {
            float diff = adjustValue(angle.value) - adjustValue(value);
            if (Math.abs(diff) < DELTA) {
                return 0;
            } else if (diff < 0) {
                return 1;
            }
            return -1;
        }
    }

    private static class CSOEdge {
        private Matrix vectorCoords = Matrix.createCoords(0, 0);
        List<Pair<Integer, Integer>> edges = new ArrayList<Pair<Integer, Integer>>();

        public CSOEdge(Matrix vectorCoords, int polygonNumber, int vertexNumber) {
            addEdge(vectorCoords, polygonNumber, vertexNumber);
        }

        public void addEdge(Matrix vectorCoords, int polygonNumber, int vertexNumber) {
            this.vectorCoords.applyLinComb(vectorCoords, 1, 1);
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
