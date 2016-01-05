package game.engine.newengine;

import game.engine.gamefield.IDrawContext;

public class Circle extends AbstractShape {
    private float r;

    private final static float d2 = 0.9f * 0.9f;

    private Vec2 drawCoords = null;

    public Circle(Vec2 coords, float r) {
        this.centerCoords = coords;
        this.r = r;
        this.aabb = new AABB(this);
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
        aabb = new AABB(this);
    }

    @Override
    public void draw(IDrawContext drawContext) {
//        if (drawCoords == null || drawCoords.minusEq(centerCoords).len2() > d2) {
//            drawCoords = new Vec2(centerCoords);
//        }
//        drawContext.drawCircle(
//                drawCoords.get(0),
//                drawCoords.get(1),
//                r
//        );
//        aabb.draw(drawContext);

        drawContext.drawCircle(
                centerCoords.get(0),
                centerCoords.get(1),
                r
        );
    }
}
