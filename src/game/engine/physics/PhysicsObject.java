package game.engine.physics;

import game.engine.geometry.GeometryObject;
import game.engine.gamefield.DrawContext;
import game.engine.gamefield.Drawable;
import game.engine.myutils.Matrix;

public class PhysicsObject implements Drawable {
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

    public GeometryObject getGeometryObject() {
        return geometryObject;
    }

    public void update(float dt) {
        v.applyLinearCombination((new Matrix(a)).mul(dt), 1f, 1f);
        Matrix dMove = (new Matrix(v)).mul(dt);
        geometryObject.move(dMove);
        geometryObject.rotate(av * dt);
    }

    public Matrix getV() {
        return v;
    }

    public float getAV() {
        return av;
    }

    public float getInvM() {
        return invM;
    }

    public float getInvI() {
        return invI;
    }

    public void applyImpulse(Matrix impulse) {

    }

    @Override
    public void draw(DrawContext drawContext) {
        geometryObject.draw(drawContext);
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
