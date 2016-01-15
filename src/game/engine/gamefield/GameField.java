package game.engine.gamefield;

import java.util.ArrayList;
import java.util.List;

public class GameField implements Runnable {
    final private List<IDrawable> objectsToDraw = new ArrayList<IDrawable>();
    IDrawContext drawContext = null;

    public GameField(IDrawContext drawContext) {
        this.drawContext = drawContext;
    }

    public void addObjectsToDraw(List<? extends IDrawable> drawables) {
        synchronized (objectsToDraw) {
            objectsToDraw.addAll(drawables);
        }
    }

    public void addObjectsToDraw(IDrawable drawable) {
        synchronized (objectsToDraw) {
            objectsToDraw.add(drawable);
        }
    }

    public void render() {
        drawContext.startRendering();
        synchronized (objectsToDraw) {
            for (IDrawable objectToDraw : objectsToDraw) {
                objectToDraw.draw(drawContext);
            }
        }
        drawContext.endRendering();
    }

    @Override
    public void run() {
        while (true) {
            render();
            try {
                Thread.sleep(1000 / 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
