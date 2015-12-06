package game.engine.myutils;

public class Angle implements Comparable<Angle> {
    private float value;
    private static final float DELTA = 0.0001f;

    public Angle(Matrix matrix) {
        value = (float) Math.atan2(matrix.get(1), matrix.get(0));
    }

    public Angle(float x, float y) {
        value = (float) Math.atan2(y, x);
    }

    public Angle(float value) {
        this.value = value;
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
        return isValuesEqual(angle.value, value);
    }

    @Override
    public int compareTo(Angle angle) {
        float diff = angle.value - value;
        if (isValuesEqual(angle.value, value)) {
            return 0;
        } else if (diff < 0) {
            return 1;
        }
        return -1;
    }

    private boolean isValuesEqual(float value1, float value2) {
        float diff = Math.abs(value1 - value2);
        return diff < DELTA;
    }

    @Override
    public String toString() {
        return "angle: " + value;
    }
}