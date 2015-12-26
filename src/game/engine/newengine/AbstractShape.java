package game.engine.newengine;

import game.engine.gamefield.IDrawContext;

public class AbstractShape implements IShape {

    protected Vec2 centerCoords = new Vec2(0f, 0f);

    @Override
    public void move(Vec2 coords) {
        centerCoords.plus(coords);
    }

    @Override
    public void rotate(float angle) {

    }

    @Override
    public void draw(IDrawContext drawContext) {

    }
}
