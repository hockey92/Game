package game.engine.physics;

import game.engine.gamefield.IDrawable;
import game.engine.geometry.GeometryObject;
import game.engine.myutils.Matrix;

public interface IPhysicsObject extends IDrawable {
    GeometryObject getGeometryObject();

    void update(float dt);

    Matrix getV();

    float getAV();

    float getInvM();

    float getInvI();

    void applyVelocityFix(Matrix dV, float dAV);
}
