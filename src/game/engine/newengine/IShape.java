package game.engine.newengine;

import game.engine.gamefield.IDrawable;

import java.util.List;

public interface IShape extends IDrawable {
    Vec2 getCenter();

    void move(Vec2 coords);

    void rotate(float angle);

    List<IShape> getSimpleShapes();

    AABB getAABB();
}