package tertis.tetris.game.client;

import java.awt.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.*;

import tertis.tetris.game.model.TetrisModel;
import tertis.tetris.game.view.TetrisView;

@SuppressWarnings("serial")
public class TetrisClient extends JFrame {

	public TetrisClient(String title) {
		super(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		TetrisView view = new TetrisView(20, 10);
		
		try {
        	System.out.println("Connecting to server...");
            Registry registry = LocateRegistry.getRegistry("ec2-50-112-190-58.us-west-2.compute.amazonaws.com", 1099);
            System.out.println("Getting tetris model...");
            TetrisModel m = (TetrisModel) registry.lookup("tetrisModel");
        
            //Set name
            String name = "";
            while(name == "") {
            	
            	name = (String)JOptionPane.showInputDialog("Enter Your Name Mortal:");
            	if(name == "") continue;
            	
            	if(!m.isNameAvailable(name)) {
            		JOptionPane.showMessageDialog(null, "That name is taken peasant.");
            		name = "";
            	}
            }
            view.setName(name);
            
            System.out.println("Setting tetris model in view...");
            view.setModel(m);
		} catch (Exception e) {
            System.err.println("TetrisClient exception:");
            e.printStackTrace();
        }
		System.out.println("Done!");
		
		getContentPane().add(view, BorderLayout.CENTER);
		view.start();
	}

	public static void main(String[] args) {
		TetrisClient app = new TetrisClient("TETRIS AGAINST HUMANITY");
		app.pack();
		// center the window
		Dimension dm = app.getToolkit().getScreenSize();
		app.setLocation((int) (dm.getWidth() / 2 - app.getWidth() / 2), (int) (dm.getHeight() / 2 - app.getHeight() / 2));
		app.setVisible(true);
	}
}