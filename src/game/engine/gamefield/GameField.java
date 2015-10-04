package game.engine.gamefield;

import java.util.List;

public class GameField implements Runnable {
    private List<? extends Drawable> objectsToDraw = null;
    DrawContext drawContext = null;

    public GameField(DrawContext drawContext) {
        this.drawContext = drawContext;
    }

    public void setObjectsToDraw(List<? extends Drawable> objectsToDraw) {
        this.objectsToDraw = objectsToDraw;
    }

    public void render() {
        drawContext.startRendering();
        if (objectsToDraw != null) {
            for (Drawable objectToDraw : objectsToDraw) {
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
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
