package tertis.tetris.game.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.rmi.RemoteException;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tertis.tetris.game.server.PlayerQueue;

@SuppressWarnings("serial")
public class QueuePanel extends JPanel {
	
	//private final PlayerQueue queue;
	
	public QueuePanel() 
	{
		
	}
	
	public void repaint(PlayerQueue q)
	{
		this.removeAll();
		this.updateUI();
		JLabel temp;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		for(int x = 0; x < q.size(); x++)
		{
			try {
				temp = new JLabel(q.get(x).getName());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}