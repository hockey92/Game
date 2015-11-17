package game.engine.geometry;

import game.engine.gamefield.DrawContext;
import game.engine.gamefield.Drawable;
import game.engine.geometry.figures.ConvexPolygon;
import game.engine.geometry.figures.Movable;
import game.engine.myutils.Matrix;

import java.util.AbstractList;
import java.util.ArrayList;

public class GeometryObject implements Drawable, Movable {
    protected ConvexPolygon shape = null;

    protected GeometryObject parent = null;
    protected AbstractList<GeometryObject> children = null;

    public GeometryObject() {
    }

    public GeometryObject(ConvexPolygon shape) {
        this.shape = shape;
    }

    public GeometryObject(ConvexPolygon shape, GeometryObject parent) {
        this.shape = shape;
        this.parent = parent;
    }

    public ConvexPolygon getShape() {
        return shape;
    }

    protected void updateThisOne() {
    }

    public void update() {
        updateThisOne();
        if (children != null) {
            for (GeometryObject geometryObject : children) {
                geometryObject.update();
            }
        }
    }

    public void addChild(GeometryObject child) {
        if (children == null) {
            children = new ArrayList<GeometryObject>();
        }
        children.add(child);
    }

    protected void drawThisOne(DrawContext drawContext) {
        if (shape != null) {
            shape.draw(drawContext);
        }
    }

    @Override
    public void draw(DrawContext drawContext) {
        drawThisOne(drawContext);
        if (children != null) {
            for (GeometryObject geometryObject : children) {
                geometryObject.draw(drawContext);
            }
        }
    }

    @Override
    public void move(float dx, float dy) {

    }

    @Override
    public void move(Matrix dCoords) {
        if (shape != null) {
            shape.move(dCoords);
        }
    }

    @Override
    public void rotate(float dAngle) {
        shape.rotate(dAngle);
        update();
    }
}
