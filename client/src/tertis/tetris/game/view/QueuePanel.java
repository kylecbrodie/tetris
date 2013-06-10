package tertis.tetris.game.view;

import java.awt.Graphics;

import javax.swing.JPanel;

import tertis.tetris.game.server.PlayerQueue;

public class QueuePanel extends JPanel {

	private static final long serialVersionUID = 4200542161324510129L;

	private PlayerQueue queue;

	public void setQueue(PlayerQueue q) {
		queue = q;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(queue == null) return;
		
		int y = getY();
		for (String s : queue) {
			g.drawString(s, getX(), y);
			y += 20;
		}
	}
}