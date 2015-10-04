package game.engine.geometry.figures;

public class ShapeFactory {
    public static ConvexPolygon createRectangle(float w, float h, float x, float y, float angle) {
        float[] ys = {-h / 2, h / 2, h / 2, -h / 2};
        float[] xs = {w / 2, w / 2, -w / 2, -w / 2};
        ConvexPolygon shape = new ConvexPolygon(xs, ys, 4);
        shape.setCenterOfMass(x, y);
        shape.setAngle(angle);
        return shape;
    }
}
