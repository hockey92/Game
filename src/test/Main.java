package test;

import game.engine.gamefield.GameField;
import game.engine.geometry.figures.ConvexPolygon;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String args[]) {
        List<ConvexPolygon> gameObjects = new ArrayList<ConvexPolygon>();
        gameObjects.add(new ConvexPolygon(300, 300));
        gameObjects.add(new ConvexPolygon(350, 300));
        gameObjects.add(new ConvexPolygon(300, 350));
        gameObjects.add(new ConvexPolygon(350, 350));

        float dCoord[][] = {{-1.0f, -1.0f}, {1.0f, -1.0f}, {-1.0f, 1.0f}, {1.0f, 1.0f}};

        SimpleGameContextImpl contextImp = new SimpleGameContextImpl();
        GameField gameField = new GameField(contextImp);
        gameField.setObjectsToDraw(gameObjects);

        Thread renderThread = new Thread(gameField);
        renderThread.start();

        while (true) {
            try {
                for (int i = 0; i < 4; i++) {
                    gameObjects.get(i).move(dCoord[i][0], dCoord[i][1]);
                }
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
