package game.engine.geometry;

public class PolarCoords {
    private float r;
    private float angle;

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
}
