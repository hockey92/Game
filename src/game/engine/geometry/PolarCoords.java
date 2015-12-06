package game.engine.geometry;

import game.engine.myutils.Matrix;

public class PolarCoords {
    private float r;
    private float angle;
    private Matrix cartesianCoords = null;

    public PolarCoords(float angle, float r) {
        this.angle = angle;
        this.r = r;
    }

    public float getR() {
        return r;
    }

    public float getAngle() {
        return angle;
    }

    public Matrix getCartesianCoords() {
        if (cartesianCoords == null) {
            cartesianCoords = createCartesianCoords(angle, r);
        }
        return cartesianCoords;
    }

    public static Matrix createCartesianCoords(float angle, float r) {
        return Matrix.createCoords(
                r * (float) Math.cos(angle),
                r * (float) Math.sin(angle)
        );
    }
}
