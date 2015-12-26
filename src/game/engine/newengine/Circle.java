package game.engine.newengine;

import game.engine.gamefield.IDrawContext;

public class Circle extends AbstractShape {
    private float r;

    public Circle(Vec2 coords, float r) {
        this.centerCoords = coords;
        this.r = r;
    }

    public Vec2 getCenterCoords() {
        return centerCoords;
    }

    public float getR() {
        return r;
    }

    @Override
    public void move(Vec2 coords) {
        centerCoords.plus(coords);
    }

    @Override
    public void draw(IDrawContext drawContext) {
        drawContext.drawCircle(
                centerCoords.get(0),
                centerCoords.get(1),
                r
        );
    }
}
