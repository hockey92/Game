package game.engine.physics;

import game.engine.myutils.Matrix;

public class JointAbstractConstraint extends AbstractConstraint {
    private float dt;
    private float angle1 = 0f;
    private float angle2 = 0f;
    private float r1 = 30f;
    private float r2 = 30f;
    private Matrix rv1;
    private Matrix rv2;
    private Matrix v1;
    private Matrix v2;
    private Matrix d;

    public JointAbstractConstraint(IPhysicsObject po1, IPhysicsObject po2, float r1, float angle1, float r2, float angle2, float dt) {
        super(po1, po2);

        this.r1 = r1;
        this.r2 = r2;
        this.angle1 = angle1;
        this.angle2 = angle2;

        this.dt = dt;

        rv1 = getVector(po1, angle1, r1);
        rv2 = getVector(po2, angle2, r2);

        v1 = po1.getGeometryObject().getShape().getRealCoords(rv1);
        v2 = po2.getGeometryObject().getShape().getRealCoords(rv2);

        d = Matrix.getLinComb(v2, v1, 1f, -1f);
        if (d.length() == 0.0f) {
            d = Matrix.createCoords(0.001f, 0.001f);
        }
        System.out.println("l = " + d.length());
    }

    private Matrix getVector(IPhysicsObject po, float angle, float r) {
        angle += po.getGeometryObject().getShape().getAngle();
        return Matrix.createCoords(
                r * (float) Math.cos(angle),
                r * (float) Math.sin(angle)
        );
    }

    private Matrix calculateAdjustment() {
        Matrix answ = new Matrix(1, 1);
        return answ;
    }

    @Override
    protected Matrix createJacobian() {
        return PhysicsMatrixFactory.createSimpleJacobian(d, rv1, rv2);
    }

    @Override
    protected Matrix createB() {
        Matrix b = new Matrix(1, 1);
        float len = d.length();
        b.set(0, (len < 0.1f ? 0 : len) * (1f / dt)).mul(0.1f);
        return b.applyLinComb(calculateAdjustment(), 1f, 1f);
    }
}
