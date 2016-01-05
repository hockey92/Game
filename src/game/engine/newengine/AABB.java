package game.engine.newengine;

import game.engine.gamefield.IDrawContext;
import game.engine.gamefield.IDrawable;

public class AABB implements IDrawable {
    private Vec2 leftDown;
    private Vec2 rightUp;

    public AABB(Vec2 leftDown, Vec2 rightUp) {
        this.leftDown = leftDown;
        this.rightUp = rightUp;
    }

    public AABB(float right, float left, float up, float down) {
        leftDown = new Vec2(left, down);
        rightUp = new Vec2(right, up);
    }

    public AABB(IShape shape) {
        if (shape instanceof Circle) {
            Circle circle = (Circle) shape;
            float r = circle.getR();
            Vec2 centerCoords = circle.getCenterCoords();
            leftDown = new Vec2(
                    centerCoords.get(0) - r,
                    centerCoords.get(1) - r
            );
            rightUp = new Vec2(
                    centerCoords.get(0) + r,
                    centerCoords.get(1) + r
            );
        } else if (shape instanceof Segment) {
            Segment segment = (Segment) shape;
            Vec2 a = segment.getCoord(0);
            Vec2 b = segment.getCoord(1);
            leftDown = new Vec2(
                    Math.min(a.get(0), b.get(0)),
                    Math.min(a.get(1), b.get(1))
            );
            rightUp = new Vec2(
                    Math.max(a.get(0), b.get(0)),
                    Math.max(a.get(1), b.get(1))
            );
        }
    }

    public AABB(AABB aabb, Vec2 d) {
        Vec2 dLeftDown = aabb.leftDown.plusEq(d);
        Vec2 dRightUp = aabb.rightUp.plusEq(d);

        leftDown = new Vec2(
                Math.min(aabb.leftDown.get(0), dLeftDown.get(0)),
                Math.min(aabb.leftDown.get(1), dLeftDown.get(1))
        );
        rightUp = new Vec2(
                Math.max(aabb.rightUp.get(0), dRightUp.get(0)),
                Math.max(aabb.rightUp.get(1), dRightUp.get(1))
        );
    }

    public static boolean isIntersect(AABB a, AABB b) {
        return !(a.leftDown.get(0) > b.rightUp.get(0)
                || b.leftDown.get(0) > a.rightUp.get(0)
                || a.leftDown.get(1) > b.rightUp.get(1)
                || b.leftDown.get(1) > a.rightUp.get(1));
    }

    @Override
    public void draw(IDrawContext drawContext) {
        drawContext.drawRect(
                leftDown.get(0),
                leftDown.get(1),
                rightUp.get(0),
                rightUp.get(1)
        );
    }
}
