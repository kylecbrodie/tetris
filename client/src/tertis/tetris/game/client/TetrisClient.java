package tertis.tetris.game.client;

import java.awt.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.*;

import tertis.tetris.game.model.TetrisModel;
import tertis.tetris.game.view.SimpleView;

@SuppressWarnings("serial")
public class TetrisClient extends JFrame {

	public TetrisClient(String title) {
		super(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		SimpleView view = new SimpleView(20, 10);
		
		if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            Registry registry = LocateRegistry.getRegistry();
            TetrisModel m = (TetrisModel) registry.lookup("tetrisModel");
            view.setModel(m);
        } catch (Exception e) {
            System.err.println("TetrisClient exception:");
            e.printStackTrace();
        }
		
		getContentPane().add(view, BorderLayout.CENTER);
	}

	public static void main(String[] args) {
		TetrisClient app = new TetrisClient("Tetris");
		app.pack();
		// center the window
		Dimension dm = app.getToolkit().getScreenSize();
		app.setLocation((int) (dm.getWidth() / 2 - app.getWidth() / 2), (int) (dm.getHeight() / 2 - app.getHeight() / 2));
		app.setVisible(true);
	}
}