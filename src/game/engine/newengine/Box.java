package game.engine.newengine;

import game.engine.gamefield.IDrawContext;

import java.util.ArrayList;
import java.util.List;

public class Box extends AbstractShape {

    private List<IShape> segments = new ArrayList<IShape>();

    public Box() {
        segments.add(new Segment(new Vec2(-100, -100), new Vec2(-100, 100)));
        segments.add(new Segment(new Vec2(-100, 100), new Vec2(100, 100)));
        segments.add(new Segment(new Vec2(100, 100), new Vec2(100, -100)));
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

