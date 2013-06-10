package tertis.tetris.game.view;

import javax.swing.JPanel;

import tertis.tetris.game.server.PlayerQueue;

public class QueuePanel extends JPanel {
	
	private final PlayerQueue queue;
	
	public QueuePanel(PlayerQueue q) {
		queue = q;
	}
}