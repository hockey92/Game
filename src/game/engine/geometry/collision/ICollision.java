package game.engine.geometry.collision;

import game.engine.myutils.Matrix;

public interface ICollision {
    Matrix getContactVector(int index);

    float getPenetrationDepth();

    boolean isCollision();

    Matrix getNormal();
}
