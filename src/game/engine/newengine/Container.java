package game.engine.newengine;

import game.engine.gamefield.IDrawContext;

import java.util.ArrayList;
import java.util.List;

public class Container extends AbstractShape {

    private List<IShape> segments = new ArrayList<IShape>();
    private boolean isOpen = false;

    public Container() {
        segments.add(new Segment(new Vec2(-100, -80), new Vec2(-100, 20)));
        segments.add(new Segment(new Vec2(-100, 20), new Vec2(-10, 100)));
        segments.add(new Segment(new Vec2(-10, 100), new Vec2(-10, 120)));
        segments.add(new Segment(new Vec2(-10, 120), new Vec2(10, 120)));
        segments.add(new Segment(new Vec2(10, 120), new Vec2(10, 100)));
        segments.add(new Segment(new Vec2(10, 100), new Vec2(100, 20)));
        segments.add(new Segment(new Vec2(100, 20), new Vec2(100, -80)));

    }

    public void open() {
        if (isOpen) {
            segments.get(3).move(new Vec2(40f, 0f));
        } else {
            segments.get(3).move(new Vec2(-40f, 0f));
        }
        isOpen = !isOpen;
    }

    @Override
    public void move(Vec2 coords) {
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
