package game.engine.myutils;

public class Angle implements Comparable<Angle> {
    private float value;
    private final float DELTA = 0.00001f;

    public Angle(Matrix matrix) {
        value = adjustValue((float) Math.atan2(matrix.get(1), matrix.get(0)));
    }

    public Angle(float x, float y) {
        value = adjustValue((float) Math.atan2(y, x));
    }

    public float getValue() {
        return value;
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

    private float adjustValue(float value) {
        return value < 0f ? value + 2f * MathUtils.PI : value;
    }

    @Override
    public int compareTo(Angle angle) {
        float diff = angle.value - value;
        if (Math.abs(diff) < DELTA) {
            return 0;
        } else if (diff < 0) {
            return 1;
        }
        return -1;
    }
}