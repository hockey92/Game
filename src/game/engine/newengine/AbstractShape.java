package game.engine.newengine;

import game.engine.gamefield.IDrawContext;

import java.util.Collections;
import java.util.List;

public class AbstractShape implements IShape {

    protected Vec2 centerCoords = new Vec2(0f, 0f);

    @Override
    public Vec2 getCenter() {
        return centerCoords;
    }

    @Override
    public void move(Vec2 coords) {
        centerCoords.plus(coords);
    }

    @Override
    public void rotate(float angle) {

    }

    @Override
    public List<IShape> getSimpleShapes() {
        return Collections.singletonList((IShape) this);
    }

    @Override
    public void draw(IDrawContext drawContext) {

    }
}
