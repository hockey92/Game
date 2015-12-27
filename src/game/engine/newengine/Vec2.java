package game.engine.newengine;

import game.engine.myutils.IVector;

public class Vec2 implements IVector {
    private float[] values = new float[2];

    public Vec2(float x, float y) {
        values[0] = x;
        values[1] = y;
    }

    public Vec2(Vec2 v) {
        for (int i = 0; i < 2; i++) {
            values[i] = v.values[i];
        }
    }

    public Vec2(IVector v) {
        for (int i = 0; i < 2; i++) {
            values[i] = v.get(i);
        }
    }

    public Vec2() {

    }

    public float x() {
        return values[0];
    }

    public float y() {
        return values[1];
    }

    public Vec2 plus(Vec2 v) {
        for (int i = 0; i < 2; i++) {
            values[i] += v.values[i];
        }
        return this;
    }

    public Vec2 minus(Vec2 v) {
        for (int i = 0; i < 2; i++) {
            values[i] -= v.values[i];
        }
        return this;
    }

    public Vec2 plusEq(Vec2 v) {
        Vec2 newVec2 = new Vec2();
        for (int i = 0; i < 2; i++) {
            newVec2.values[i] = values[i] + v.values[i];
        }
        return newVec2;
    }

    public Vec2 minusEq(Vec2 v) {
        Vec2 newVec2 = new Vec2();
        for (int i = 0; i < 2; i++) {
            newVec2.values[i] = values[i] - v.values[i];
        }
        return newVec2;
    }

    public Vec2 mul(float c) {
        for (int i = 0; i < 2; i++) {
            values[i] *= c;
        }
        return this;
    }

    public Vec2 mulEq(float c) {
        Vec2 newVec2 = new Vec2();
        for (int i = 0; i < 2; i++) {
            newVec2.values[i] = values[i] * c;
        }
        return newVec2;
    }

    public float len() {
        return (float) Math.sqrt(values[0] * values[0] + values[1] * values[1]);
    }

    public static float getDotProd(Vec2 v1, Vec2 v2) {
        float dotProd = 0;
        for (int i = 0; i < 2; i++) {
            dotProd += v1.values[i] * v2.values[i];
        }
        return dotProd;
    }

    @Override
    public float get(int i) {
        return values[i];
    }
}
