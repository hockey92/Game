package game.engine.newengine;

import game.engine.gamefield.IDrawContext;

import java.util.Collections;
import java.util.List;

public class AbstractShape implements IShape {

    protected Vec2 centerCoords = new Vec2(0f, 0f);
    protected AABB aabb = null;
    protected int id;

    @Override
    public Vec2 getCenter() {
        return centerCoords;
    }

    @Override
    public void move(Vec2 coords) {
        centerCoords.plus(coords);
        if (aabb != null) {
            aabb = new AABB(this);
        }
    }

    @Override
    public void rotate(float angle) {

    }

    @Override
    public List<IShape> getSimpleShapes() {
        return Collections.singletonList((IShape) this);
    }

    @Override
    public AABB getAABB() {
        return aabb;
    }

    @Override
    public void setId(ShapeId shapeId) {
        this.id = shapeId.getId();
        shapeId.increment();
        List<IShape> shapes = getSimpleShapes();
        if (shapes.size() == 1) {
            return;
        }
        for (IShape shape : shapes) {
            shape.setId(shapeId);
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void draw(IDrawContext drawContext) {

    }
}
