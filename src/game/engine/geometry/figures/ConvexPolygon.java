package game.engine.geometry.figures;

import game.engine.gamefield.IDrawContext;
import game.engine.myutils.MathUtils;
import game.engine.myutils.Matrix;

public class ConvexPolygon implements IConvexPolygon, IMovable {
    protected int verticesCount;
    protected Matrix[] initialVertices;
    protected Matrix[] vertices;
    protected Matrix centerOfMass = Matrix.createCoords(0, 0);
    protected float angle = 0.0f;
    protected Matrix rightTopPoint = Matrix.createCoords(0, 0);
    protected Matrix leftBottomPoint = Matrix.createCoords(0, 0);

    public ConvexPolygon() {

    }

    public int getVerticesCount() {
        return verticesCount;
    }

    public Matrix getRightTopPoint() {
        return new Matrix(rightTopPoint);
    }

    public Matrix getLeftBottomPoint() {
        return new Matrix(leftBottomPoint);
    }

    public ConvexPolygon(float[] x, float[] y, int verticesCount) {
        this.verticesCount = verticesCount;
        initialVertices = new Matrix[verticesCount];
        vertices = new Matrix[verticesCount];
        for (int i = 0; i < verticesCount; i++) {
            initialVertices[i] = Matrix.createCoords(x[i], y[i]);
            vertices[i] = Matrix.createCoords(x[i], y[i]);
        }
        calculateOuterRectangleBorders();
    }

    public void setCenterOfMass(float x, float y) {
        centerOfMass.setCoords(x, y);
    }

    public void setCenterOfMass(Matrix centerOfMass) {
        this.centerOfMass = new Matrix(centerOfMass);
    }

    public Matrix getCenterOfMass() {
        return new Matrix(centerOfMass);
    }

    public void setAngle(float angle) {
        rotate(angle);
    }

    public float getAngle() {
        return angle;
    }

    public Matrix[] getRealCoords() {
        Matrix[] realCoords = new Matrix[verticesCount];
        for (int i = 0; i < verticesCount; i++) {
            realCoords[i] = getRealCoords(i);
        }
        return realCoords;
    }

    public Matrix getRealCoords(int vertexNumber) {
        return getRealCoords(vertices[vertexNumber]);
    }

    public Matrix getRealCoords(Matrix coords) {
        return coords.plusEq(centerOfMass);
    }

    public Matrix getCoords(int vertexNum) {
        return new Matrix(vertices[vertexNum]);
    }

    public ConvexPolygon(float x, float y) {
        centerOfMass = Matrix.createCoords(x, y);
    }

    @Override
    public void move(float dx, float dy) {
        centerOfMass.plus(Matrix.createCoords(dx, dy));
    }

    @Override
    public void move(Matrix dCoords) {
        centerOfMass.plus(dCoords);
    }

    @Override
    public void rotate(float dAngle) {
        angle += dAngle;

        if (angle < 0f) {
            angle += MathUtils.DOUBLE_PI;
        } else if (angle > MathUtils.DOUBLE_PI) {
            angle -= MathUtils.DOUBLE_PI;
        }

        for (int i = 0; i < verticesCount; i++) {
            vertices[i] = Matrix.mul(Matrix.getRotateMatrix(angle), initialVertices[i]);
        }
        calculateOuterRectangleBorders();
    }

    protected void calculateOuterRectangleBorders() {
        Matrix rightTopPoint = Matrix.createCoords(Float.MIN_VALUE, Float.MIN_VALUE);
        Matrix leftBottomPoint = Matrix.createCoords(Float.MAX_VALUE, Float.MAX_VALUE);
        for (int i = 0; i < verticesCount; i++) {
            float x = vertices[i].get(0);
            float y = vertices[i].get(1);
            if (x > rightTopPoint.get(0)) {
                rightTopPoint.set(0, x);
            }
            if (y > rightTopPoint.get(1)) {
                rightTopPoint.set(1, y);
            }
            if (x < leftBottomPoint.get(0)) {
                leftBottomPoint.set(0, x);
            }
            if (y < leftBottomPoint.get(1)) {
                leftBottomPoint.set(1, y);
            }
        }
        this.rightTopPoint = rightTopPoint;
        this.leftBottomPoint = leftBottomPoint;
    }

    private void drawRectangle(IDrawContext IDrawContext) {
        Matrix a = getRealCoords(leftBottomPoint);
        Matrix b = getRealCoords(rightTopPoint);
        IDrawContext.drawRect(a.get(0), a.get(1), b.get(0), b.get(1));
    }

    @Override
    public void draw(IDrawContext IDrawContext) {

//        for (int i = 0; i < verticesCount; i++) {
//            Matrix realCoords = getRealCoords(i);
//            IDrawContext.drawCircle(realCoords.get(0), realCoords.get(1), 4);
//        }

        IDrawContext.drawPolygon(getRealCoords());
//        drawRectangle(IDrawContext);
    }
}
