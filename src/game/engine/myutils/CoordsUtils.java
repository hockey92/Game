package game.engine.myutils;

import game.engine.geometry.PolarCoords;

public class CoordsUtils {

    public static Matrix convertToCartesianCoords(float angle, float r) {
        return Matrix.createCoords(
                r * (float) Math.cos(angle),
                r * (float) Math.sin(angle)
        );
    }

    public static PolarCoords convertToPolarCoords(float x, float y) {
        float r = (float) Math.sqrt(x * x + y * y);
        float angle = new Angle(x, y).getValue();
        return new PolarCoords(angle, r);
    }
}
