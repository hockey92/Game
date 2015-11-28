package game.engine.physics;

import game.engine.geometry.GeometryObject;
import game.engine.gamefield.IDrawContext;
import game.engine.gamefield.IDrawable;
import game.engine.myutils.Matrix;

public class PhysicsObject implements IPhysicsObject, IDrawable {
    private float invM; // 1/mass
    private float invI; // 1/inertia_moment
    private Matrix v = Matrix.createCoords(0f, 0f); // velocity
    private float av; // angle velocity
    private Matrix a = Matrix.createCoords(0f, 0f); // acceleration
    private GeometryObject geometryObject;

    PhysicsObject(
            float invM,
            float invI,
            Matrix v,
            float av,
            Matrix a,
            GeometryObject geometryObject
    ) {
        this.invM = invM;
        this.invI = invI;
        this.v = v;
        this.av = av;
        this.a = a;
        this.geometryObject = geometryObject;
    }

    @Override
    public GeometryObject getGeometryObject() {
        return geometryObject;
    }

    @Override
    public void update(float dt) {
        Matrix newV = Matrix.getLinComb(v, (new Matrix(a)).mul(dt), 1f, 1f);

        Matrix dMove = Matrix.getLinComb(v, newV, 1f, 1f).mul(0.5f * dt);
        geometryObject.move(dMove);
        geometryObject.rotate(av * dt);

        v = newV;

        v.mul(0.998f);
        av *= 0.998f;
    }

    @Override
    public Matrix getV() {
        return v;
    }

    @Override
    public float getAV() {
        return av;
    }

    @Override
    public float getInvM() {
        return invM;
    }

    @Override
    public float getInvI() {
        return invI;
    }

    @Override
    public void applyVelocityFix(Matrix dV, float dAV) {
        av += dAV;
        v.applyLinComb(dV, 1f, 1f);
    }

    @Override
    public void draw(IDrawContext IDrawContext) {
        geometryObject.draw(IDrawContext);
    }

    static public class PhysicsObjectBuilder {
        private float invM; // 1/mass
        private float invI; // 1/inertia_moment
        private Matrix v; // velocity
        private float av;
        private Matrix a; // acceleration
        GeometryObject geometryObject;

        public PhysicsObjectBuilder setInvM(float invM) {
            this.invM = invM;
            return this;
        }

        public PhysicsObjectBuilder setInvI(float invI) {
            this.invI = invI;
            return this;
        }

        public PhysicsObjectBuilder setV(Matrix v) {
            this.v = v;
            return this;
        }

        public PhysicsObjectBuilder setAV(float av) {
            this.av = av;
            return this;
        }

        public PhysicsObjectBuilder setA(Matrix a) {
            this.a = a;
            return this;
        }

        public PhysicsObjectBuilder setGeometryObject(GeometryObject geometryObject) {
            this.geometryObject = geometryObject;
            return this;
        }

        public PhysicsObject createPhysicsObject() {
            return new PhysicsObject(
                    invM,
                    invI,
                    v == null ? Matrix.createCoords(0f, 0f) : v,
                    av,
                    a == null ? Matrix.createCoords(0f, 0f) : a,
                    geometryObject
            );
        }
    }
}
