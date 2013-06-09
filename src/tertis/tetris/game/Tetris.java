package tertis.tetris.game;
import java.awt.*;
import javax.swing.*;

import tertis.tetris.game.model.TetrisModel;
import tertis.tetris.game.view.SimpleView;

@SuppressWarnings("serial")
public class Tetris extends JFrame {
    
    public Tetris(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        SimpleView view = new SimpleView(20,10);
        TetrisModel russia = new TetrisModel(20, 10);
        russia.setView(view);
        getContentPane().add(view, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        Tetris app = new Tetris("Tetris");
        app.pack();
        //center the window
        Dimension dm = app.getToolkit().getScreenSize();
        app.setLocation((int)(dm.getWidth()/2 - app.getWidth()/2),
            (int)(dm.getHeight()/2 - app.getHeight()/2));
        app.setVisible(true);
    }
}