package game.engine.newengine;

import game.engine.gamefield.IDrawable;

public interface IShape extends IDrawable {
    void move(Vec2 coords);

    void rotate(float angle);
}
