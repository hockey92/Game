package game.engine.geometry;

import game.engine.gamefield.IDrawContext;
import game.engine.gamefield.IDrawable;
import game.engine.geometry.figures.ConvexPolygon;
import game.engine.geometry.figures.IMovable;
import game.engine.myutils.Matrix;

import java.util.AbstractList;
import java.util.ArrayList;

public class GeometryObject implements IDrawable, IMovable, IAnimationable {
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

    public int childCount() {
        return children.size();
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

    public GeometryObject getChild(int index) {
        return children.get(index);
    }

    protected void drawThisOne(IDrawContext drawContext) {
        if (shape != null) {
            shape.draw(drawContext);
        }
    }

    @Override
    public void draw(IDrawContext drawContext) {
        drawThisOne(drawContext);
        if (children != null) {
            for (GeometryObject geometryObject : children) {
                geometryObject.draw(drawContext);
            }
        }
    }

    @Override
    public void move(float dx, float dy) {
        if (shape != null) {
            shape.move(dx, dy);
        }
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

    @Override
    public void nextFrame() {

    }
}
