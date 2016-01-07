package game.engine.newengine;

import game.engine.gamefield.IDrawContext;
import game.engine.gamefield.IDrawable;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class OneHundredBalls implements IDrawable {

    private NewGameObject container;
    private List<GlassPhysicsObject> glasses = new ArrayList<GlassPhysicsObject>();
    private List<NewGameObject> balls = new ArrayList<NewGameObject>();

    private Deque<NewGameObject> notUsedBalls;
    private Deque<NewGameObject> notUsedGlasses;

    private final int BALLS_COUNT = 20;
    private int activeBalls = 0;
    private int glassesCount = 0;

    public OneHundredBalls() {
        final IShape containerShape = new Container();
        containerShape.move(new Vec2(300, 350));
        container = new NewGameObject(containerShape, 0f);

        for (int i = 0; i < 10; i++) {
            IShape glassShape = new GlassShape();
            glassShape.move(new Vec2(500f + 200f * i, NewEngineConstants.down));
            glasses.add(new GlassPhysicsObject(glassShape, 0f));
        }

        float x = 210;
        float y = 275;
        for (int i = 0; i < 100; i++) {
            float currX = x + 12 * (i % 16);
            float currY = y + 12 * (i / 16);
            NewGameObject ball = new NewGameObject(new Circle(new Vec2(currX, currY), 4.99f), 1f).setAcceleration(new Vec2(0f, 0.8f));
            balls.add(ball);
        }

        notUsedBalls = new LinkedList<NewGameObject>(balls);
        notUsedGlasses = new LinkedList<NewGameObject>(glasses);

        PhysicsService.getInstance().addGameObject(container);
        PhysicsService.getInstance().addGameObjects(glasses);

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
                if (activeBalls < BALLS_COUNT && notUsedBalls.size() > 0) {
                    NewGameObject ball = notUsedBalls.pollLast();
                    if (ball != null) {
                        PhysicsService.getInstance().addGameObject(ball);
                        activeBalls++;
                    }
                }

                for (GlassPhysicsObject glass : glasses) {
                    glass.checkDirection();
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
