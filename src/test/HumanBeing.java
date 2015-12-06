package test;

import game.engine.geometry.GeometryObject;
import game.engine.geometry.HardJoin;
import game.engine.geometry.PolarCoords;
import game.engine.geometry.figures.ShapeFactory;
import game.engine.myutils.MathUtils;

public class HumanBeing extends GeometryObject {

    public HumanBeing() {
        shape = ShapeFactory.createRectangle(12f, 40f, 100f, 100f, 0f);
        HardJoin join = new HardJoin(this, new PolarCoords(MathUtils.PI * 0.5f, 40f * 0.5f));
        addChild(join);
        join.addChild(createObjectsChain(2, 4f, 20f), new PolarCoords(MathUtils.PI * 1.5f, 20f * 0.5f));
    }

    private GeometryObject createObjectsChain(int objectsCount, float w, float h) {
        if (objectsCount < 1) {
            throw new RuntimeException("You should create at least one link in chain.");
        }

        GeometryObject first;
        GeometryObject prev = first = new GeometryObject(ShapeFactory.createRectangle(w, h));
        for (int i = 0; i < objectsCount - 1; i++) {
            GeometryObject next = new GeometryObject(ShapeFactory.createRectangle(w, h));
            HardJoin join = new HardJoin(prev, new PolarCoords(MathUtils.PI * 0.5f, h * 0.5f));
            join.addChild(next, new PolarCoords(MathUtils.PI * 1.5f, h * 0.5f));
            prev.addChild(join);
            prev = next;
        }
        return first;
    }
}
