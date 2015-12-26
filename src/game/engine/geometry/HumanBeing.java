package game.engine.geometry;

import game.engine.geometry.figures.ShapeFactory;
import game.engine.myutils.CoordsUtils;
import game.engine.myutils.MathUtils;

public class HumanBeing extends GeometryObject {

    private float w = 60f / 4f;
    private float h = 120f / 4f;

    private float w1 = 20f / 4f;
    private float h1 = 80f / 4f;

    private float dh = 16f;

    private HardJoin[] joints = new HardJoin[4];

    public HumanBeing() {
        shape = ShapeFactory.createRectangle(w, h, 300f, 300f, 0f);
        HardJoin join1 = new HardJoin(this, CoordsUtils.convertToPolarCoords((w / 2 - w1 / 2), h / 2f));
        HardJoin join2 = new HardJoin(this, CoordsUtils.convertToPolarCoords(-(w / 2 - w1 / 2), h / 2f));
        HardJoin join3 = new HardJoin(this, CoordsUtils.convertToPolarCoords(w / 2, -h / 2 + h / dh));
        HardJoin join4 = new HardJoin(this, CoordsUtils.convertToPolarCoords(-w / 2, -h / 2 + h / dh));
        addChild(join1);
        addChild(join2);
        addChild(join3);
        addChild(join4);
        join1.addChild(
                createObjectsChain(2, w1, h1),
                new PolarCoords(MathUtils.PI * 1.5f, h1 / 2f)
        );
        join2.addChild(
                createObjectsChain(2, w1, h1),
                new PolarCoords(MathUtils.PI * 1.5f, h1 / 2f)
        );
        join3.addChild(
                createObjectsChain(2, w1, h1),
                new PolarCoords(MathUtils.PI * 1.5f, h1 / 2f)
        );
        join4.addChild(
                createObjectsChain(2, w1, h1),
                new PolarCoords(MathUtils.PI * 1.5f, h1 / 2f)
        );
        float angle1 = (float) Math.atan((w / 2f - w1 / 2f) / (h / 2));
        float angle2 = (float) Math.atan((h / 2 - h / dh) / (w / 2)) + MathUtils.PI / 2f;
        join1.shiftAngleBetweenParentAndChild(0, angle1);
        join2.shiftAngleBetweenParentAndChild(0, -angle1);
        join3.shiftAngleBetweenParentAndChild(0, angle2);
        join4.shiftAngleBetweenParentAndChild(0, -angle2);

        join1.shiftAngleBetweenParentAndChild(0, 0.2f);
        ((HardJoin) join1.getChild(0).getChild(0)).shiftAngleBetweenParentAndChild(0, -0.4f);
        join2.shiftAngleBetweenParentAndChild(0, 0.2f);
        ((HardJoin) join2.getChild(0).getChild(0)).shiftAngleBetweenParentAndChild(0, -0.4f);

        join3.shiftAngleBetweenParentAndChild(0, -0.2f);
        ((HardJoin) join3.getChild(0).getChild(0)).shiftAngleBetweenParentAndChild(0, 0.4f);
        join4.shiftAngleBetweenParentAndChild(0, -0.2f);
        ((HardJoin) join4.getChild(0).getChild(0)).shiftAngleBetweenParentAndChild(0, 0.4f);

        HardJoin join5 = new HardJoin(this, CoordsUtils.convertToPolarCoords(0, -h / 2f));
        join5.addChild(
                new GeometryObject(ShapeFactory.createRectangle(w / 1.5f, w / 1.5f)),
                CoordsUtils.convertToPolarCoords(0f, w / 2.5f)
        );
        addChild(join5);
    }

    private GeometryObject createObjectsChain(int objectsCount, float w, float h) {
        if (objectsCount < 1) {
            throw new RuntimeException("You should create at least one link in a chain.");
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
