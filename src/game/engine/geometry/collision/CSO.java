package game.engine.geometry.collision;

import game.engine.geometry.figures.ConvexPolygon;
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
            vertices[i + 1] = Matrix.getLinearCombination(csoEdges[i].getVectorCoords(), vertices[i], 1, 1);
        }
        calculateOuterRectangleBorders();
        setCenterOfMass(Matrix.getLinearCombination(rightTopPoint, Matrix.getLinearCombination(p1.getRightTopPoint(), p2.getLeftBottomPoint().mul(-1f), 1f, 1f), -1f, 1f));
        calculateLines();
    }

    private void createCSOEdges(ConvexPolygon p1, ConvexPolygon p2) {
        Map<Angle, CSOEdge> sortedEdgesMap = new TreeMap<Angle, CSOEdge>();
        float coeffs[][] = {{1f, -1f}, {-1f, 1f}};
        ConvexPolygon[] ps = {p1, p2};
        for (int polygonNumber = 0; polygonNumber < ps.length; polygonNumber++) {
            for (int vertexNumber = 0; vertexNumber < ps[polygonNumber].getVerticesCount(); vertexNumber++) {

                int nextVertexNumber = vertexNumber + 1 == ps[polygonNumber].getVerticesCount() ? 0 : vertexNumber + 1;
                Matrix vectorCoords = ps[polygonNumber].getCoords(nextVertexNumber)
                        .applyLinearCombination(ps[polygonNumber].getCoords(vertexNumber), coeffs[polygonNumber][0], coeffs[polygonNumber][1]);

                Angle angle = new Angle(vectorCoords);
                CSOEdge csoEdge = sortedEdgesMap.get(angle);
                if (csoEdge == null) {
                    sortedEdgesMap.put(angle, new CSOEdge(vectorCoords, polygonNumber, vertexNumber));
                } else {
                    csoEdge.addEdge(vectorCoords, polygonNumber, vertexNumber);
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
            return Math.abs(diff) < 0.01;
        }

        @Override
        public int compareTo(Angle angle) {
            float diff = angle.value - value;
            if (Math.abs(diff) < 0.01) {
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
            this.vectorCoords.applyLinearCombination(vectorCoords, 1, 1);
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
