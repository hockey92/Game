package game.engine.newengine;

import game.engine.gamefield.IDrawContext;

import java.util.ArrayList;
import java.util.List;

public class GlassShape extends AbstractShape {

    private List<IShape> segments = new ArrayList<IShape>();

    public GlassShape() {
        segments.add(new Segment(new Vec2(-30, -30), new Vec2(-20, 30)));
        segments.add(new Segment(new Vec2(-20, 30), new Vec2(20, 30)));
        segments.add(new Segment(new Vec2(20, 30), new Vec2(30, -30)));
    }

    @Override
    public void move(Vec2 coords) {
        super.move(coords);
        for (IShape segment : segments) {
            segment.move(coords);
        }
    }

    @Override
    public void rotate(float angle) {
        for (IShape shape : segments) {
            shape.rotate(angle);
        }
    }

    @Override
    public void draw(IDrawContext drawContext) {
        for (IShape segment : segments) {
            segment.draw(drawContext);
        }
    }

    @Override
    public List<IShape> getSimpleShapes() {
        return segments;
    }
}
