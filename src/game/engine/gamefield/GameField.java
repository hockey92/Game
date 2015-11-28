package game.engine.gamefield;

import java.util.List;

public class GameField implements Runnable {
    private List<? extends IDrawable> objectsToDraw = null;
    IDrawContext IDrawContext = null;

    public GameField(IDrawContext IDrawContext) {
        this.IDrawContext = IDrawContext;
    }

    public void setObjectsToDraw(List<? extends IDrawable> objectsToDraw) {
        this.objectsToDraw = objectsToDraw;
    }

    public void render() {
        IDrawContext.startRendering();
        if (objectsToDraw != null) {
            for (IDrawable objectToDraw : objectsToDraw) {
                objectToDraw.draw(IDrawContext);
            }
        }
        IDrawContext.endRendering();
    }

    @Override
    public void run() {
        while (true) {
            render();
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
