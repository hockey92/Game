package game.engine.newengine;

import game.engine.geometry.collision.Line;

public class CollisionFactory {
    public static Collision createCollision(IShape shape1, IShape shape2) {
        if (shape1 instanceof Circle && shape2 instanceof Circle) {
            return createCollision((Circle) shape1, (Circle) shape2);
        } else if (shape1 instanceof Circle && shape2 instanceof Segment) {
            return createCollision((Circle) shape1, (Segment) shape2);
        } else if (shape1 instanceof Segment && shape2 instanceof Circle) {
            Collision c = createCollision((Circle) shape2, (Segment) shape1);
            if (c != null) {
                c.getN().mul(-1f);
                return c;
            }
        }
        return null;
    }

    public static Vec2 createDistance(Vec2 p, Segment s) {
        try {
            Line line = new Line(s.getCoord(0), s.getCoord(1));
            Vec2 lineNormal = new Vec2(line.getNormal());
            Line pLine = new Line(p, p.plusEq(lineNormal));
            Vec2 mutualPoint = new Vec2(Line.getMutualPoint(line, pLine));
            if (pointBelongsToSegment(s, mutualPoint)) {
                return mutualPoint.minusEq(p);
            } else {
                Vec2 a = null;
                Float aLen = null;
                for (int i = 0; i < 2; i++) {
                    Vec2 d = s.getCoord(i).minusEq(p);
                    float len = d.len();
                    if (a == null || len < aLen) {
                        a = d;
                        aLen = len;
                    }
                }
                return a;
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }

    private static Collision createCollision(Circle c1, Circle c2) {
        Vec2 v = c2.getCenterCoords().minusEq(c1.getCenterCoords());
        float d = v.len();
        float r = c1.getR() + c2.getR();
//        if (r >= d) {
        Vec2 n = v.mulEq(1 / d);
        Vec2 r1 = n.mulEq(c1.getR());
        Vec2 r2 = n.mulEq(-c2.getR());
        return new Collision(r1, r2, n, r - d);
//        }
//        return null;
    }

    private static Collision createCollision(Circle c, Segment s) {
        try {
            Line line = new Line(s.getCoord(0), s.getCoord(1));
            Vec2 lineNormal = new Vec2(line.getNormal());
            Line pLine = new Line(
                    c.getCenterCoords(),
                    c.getCenterCoords().plusEq(lineNormal)
            );

            Vec2 mutualPoint = new Vec2(Line.getMutualPoint(line, pLine));
            if (pointBelongsToSegment(s, mutualPoint)) {
                Vec2 v1 = mutualPoint.minusEq(c.getCenterCoords());
                float len = v1.len();
//            if (len > c1.getR()) {
//                return null;
//            }
                Vec2 n = v1.mulEq(1 / len);
//                Vec2 r1 = n.mulEq(c.getR());
//                Vec2 r2 = mutualPoint.minusEq(s.getCenter());
                float penetration = c.getR() - len;
                return new Collision(null, null, n, penetration);
            } else {
                Vec2[] d = new Vec2[2];
                Vec2 a = null;
                Float aLen = null;
                for (int i = 0; i < 2; i++) {
                    d[i] = s.getCoord(i).minusEq(c.getCenterCoords());
                    float len = d[i].len();
                    if (a == null || len < aLen) {
                        a = d[i];
                        aLen = len;
                    }
                }
//            if (aLen > c1.getR()) {
//                return null;
//            }
                Vec2 n = a.mulEq(1 / aLen);
                float penetration = c.getR() - aLen;
//                Vec2 r1 = n.mulEq(c.getR());
//                Vec2 r2 = a.minusEq(s.getCenter());
                return new Collision(null, null, n, penetration);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }

    private static boolean pointBelongsToSegment(Segment s, Vec2 p) {
        float d = 0.001f;
        for (int i = 0; i < 2; i++) {
            float point1 = s.getCoord(0).get(i);
            float point2 = s.getCoord(1).get(i);
            float point = p.get(i);

            if (!(point1 - d <= point && point <= point2 + d) && !(point2 - d <= point && point <= point1 + d)) {
                return false;
            }
        }
        return true;
    }
}
