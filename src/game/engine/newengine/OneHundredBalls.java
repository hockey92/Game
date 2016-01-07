package game.engine.newengine;

import game.engine.gamefield.IDrawContext;
import game.engine.gamefield.IDrawable;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class OneHundredBalls implements IDrawable {

    private NewGameObject container;
    private List<NewGameObject> glasses;
    private Deque<NewGameObject> balls = new LinkedList<NewGameObject>();

    public OneHundredBalls() {
        float x = 0;
        float y = 0;
        for (int i = 0; i < 100; i++) {
            float currX = x + 20 * (i % 10);
            float currY = y + 20 * (i / 10);
            NewGameObject ball = new NewGameObject(new Circle(new Vec2(currX, currY), 8), 1f).setAcceleration(new Vec2(0f, 0.4f));
            balls.add(ball);
        }

        PhysicsService.getInstance().addGameObject(container);
    }

    public void open() {

    }

    public void close() {

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
}
