package game.engine.newengine;

import game.engine.gamefield.IDrawContext;
import game.engine.gamefield.IDrawable;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class OneHundredBalls implements IDrawable {

    private final int MAX_ACTIVE_BALLS_COUNT = 40;

    private NewGameObject container;
    private NewGameObject bottomBorder;

    private List<GlassPhysicsObject> glasses = new ArrayList<GlassPhysicsObject>();
    private List<NewGameObject> balls = new ArrayList<NewGameObject>();

    private Deque<NewGameObject> notUsedBalls;
    private Deque<NewGameObject> notUsedGlasses;

    private int activeBalls = 0;
    private int glassesCount = 0;

    public OneHundredBalls() {
        final IShape containerShape = new Container();
        containerShape.move(new Vec2(3.00f, 2.75f));
        container = new NewGameObject(containerShape, 0f);
//        container.setAngleVel(0.5f);

//        final IShape bottomBorderShape = new Segment(
//                new Vec2(0f, 7.40f),
//                new Vec2(7.00f, 7.40f)
//        );

        final IShape bottomBorderShape = new Circle(new Vec2(3f, 15.32f), 9f);

        bottomBorder = new NewGameObject(bottomBorderShape, 0f);

        for (int i = 0; i < 6; i++) {
            IShape glassShape = new GlassShape();
            glassShape.move(new Vec2(5.00f, Constants.up + 0.5f));
            GlassPhysicsObject glass = new GlassPhysicsObject(glassShape, 0f);
//            glass.setAngleVel(0.05f);
            glasses.add(glass);
        }

        float x = 1.57f;
        float y = 2.00f;
        for (int i = 0; i < 100; i++) {
            float currX = x + 0.12f * (i % 15);
            float currY = y + 0.12f * (i / 15);
            NewGameObject ball = new NewGameObject(new Circle(new Vec2(currX, currY), 0.045f), 1f).setAcceleration(new Vec2(0f, 10.0f));
            balls.add(ball);
        }

        notUsedBalls = new LinkedList<NewGameObject>(balls);
        notUsedGlasses = new LinkedList<NewGameObject>(glasses);

        PhysicsService.getInstance().addGameObject(container);
        PhysicsService.getInstance().addGameObjects(glasses);
        PhysicsService.getInstance().addGameObject(bottomBorder);

        this.new GameThread().start();
    }

    public void open() {
        ((Container) container.getShape()).open();
    }

    public void close() {

    }

    private void adjust() {

    }

    @Override
    public void draw(IDrawContext drawContext) {
        container.draw(drawContext);
        bottomBorder.draw(drawContext);
        for (NewGameObject glass : glasses) {
            glass.draw(drawContext);
        }
        for (NewGameObject ball : balls) {
            ball.draw(drawContext);
        }
    }

    private class GameThread extends Thread {

        @Override
        public void run() {
            while (true) {
                if (activeBalls < MAX_ACTIVE_BALLS_COUNT && notUsedBalls.size() > 0) {
                    NewGameObject ball = notUsedBalls.pollLast();
                    if (ball != null) {
                        PhysicsService.getInstance().addGameObject(ball);
                        activeBalls++;
                    }
                }

                try {
                    Thread.sleep(1000 / 30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
