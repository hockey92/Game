package test;

import game.engine.gamefield.GameField;
import game.engine.gamefield.IDrawContext;
import game.engine.newengine.*;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OneHundredBallsTest {
    public static void main(String args[]) throws Exception {

        IDrawContext context = new SimpleGameContextImpl();
        final GameField gameField = new GameField(context);

        final OneHundredBalls oneHundredBalls = new OneHundredBalls();

        gameField.addObjectsToDraw(oneHundredBalls);

        ((JFrame) context).addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                oneHundredBalls.open();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                oneHundredBalls.open();
            }
        });

        Thread renderThread = new Thread(gameField);
        renderThread.start();
    }
}
