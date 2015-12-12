package game.engine.geometry.collision;

import game.engine.myutils.Matrix;

public class Line {
    private float[] coeffs = new float[3];
    private float invSqrt;

    public Line(Matrix point1, Matrix point2) {
        coeffs[0] = point1.get(1) - point2.get(1);
        coeffs[1] = point2.get(0) - point1.get(0);
        coeffs[2] = point1.get(0) * point2.get(1) - point1.get(1) * point2.get(0);
        invSqrt = (float) (1f / Math.sqrt(coeffs[0] * coeffs[0] + coeffs[1] * coeffs[1]));
    }

    public float getValueOfExpression(Matrix point) {
        float answer = coeffs[2];
        for (int i = 0; i < 2; i++) {
            answer += coeffs[i] * point.get(i);
        }
        return answer;
    }

    public float getDistanceToPoint(Matrix point) {
        return getValueOfExpression(point) * invSqrt;
    }

    public Matrix getNormal() {
        return Matrix.createCoords(coeffs[0] * invSqrt, coeffs[1] * invSqrt);
    }

    public static float getMainDeterminant(Line l1, Line l2) {
        return l1.coeffs[0] * l2.coeffs[1] - l1.coeffs[1] * l2.coeffs[0];
    }

    public static Matrix getMutualPoint(Line l1, Line l2) {
        float delta = getMainDeterminant(l1, l2);
        if (delta == 0f) {
            throw new RuntimeException("Lines are parallel, there is no the mutual point.");
        }
        float delta1 = -l1.coeffs[2] * l2.coeffs[1] + l1.coeffs[1] * l2.coeffs[2];
        float delta2 = -l1.coeffs[0] * l2.coeffs[2] + l1.coeffs[2] * l2.coeffs[0];
        return Matrix.createCoords(delta1 / delta, delta2 / delta);
    }
}
