package game.engine.gamefield;

import game.engine.myutils.Matrix;

public interface DrawContext {
    void drawLine(float x1, float y1, float x2, float y2);

    void drawPolygon(Matrix[] coords);

    void drawCircle(float x, float y, float r);

    void startRendering();

    void drawRect(float x1, float y1, float x2, float y2);

    void endRendering();
}
