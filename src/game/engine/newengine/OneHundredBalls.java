package game.engine.newengine;

import game.engine.gamefield.IDrawContext;
import game.engine.gamefield.IDrawable;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class OneHundredBalls implements IDrawable {

    private NewGameObject container;
    private List<NewGameObject> glasses = new ArrayList<NewGameObject>();
    private List<NewGameObject> balls = new ArrayList<NewGameObject>();
    private Deque<NewGameObject> notUsedBalls;

    private final int BALLS_COUNT = 20;
    private int activeBalls = 0;

    public OneHundredBalls() {
        final IShape containerShape = new Container();
        containerShape.move(new Vec2(400, 250));
        container = new NewGameObject(containerShape, 0f);

        float x = 310;
        float y = 50;
        for (int i = 0; i < 100; i++) {
            float currX = x + 20 * (i % 10);
            float currY = y + 20 * (i / 10);
            NewGameObject ball = new NewGameObject(new Circle(new Vec2(currX, currY), 8), 1f).setAcceleration(new Vec2(0f, 0.4f));
            balls.add(ball);
        }
        notUsedBalls = new LinkedList<NewGameObject>(balls);
        PhysicsService.getInstance().addGameObject(container);
//        PhysicsService.getInstance().setNewGameObjects((List) balls);

        this.new GameThread().start();
    }

    public void open() {

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

                try {
                    Thread.sleep(1000 / 30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
