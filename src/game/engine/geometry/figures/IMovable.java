package game.engine.geometry.figures;

import game.engine.myutils.Matrix;

public interface IMovable {
    void move(float dx, float dy);

    void move(Matrix dCoords);

    void rotate(float dAngle);
}
