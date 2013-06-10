package tertis.tetris.game.view;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tertis.tetris.game.server.PlayerQueue;

public class QueuePanel extends JPanel {

	private static final long serialVersionUID = 4200542161324510129L;

	private PlayerQueue queue;

	public void setQueue(PlayerQueue q) {
		queue = q;
	}

	public void repaint(PlayerQueue q) {
		this.removeAll();
		this.updateUI();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		for (String s : queue) {
			this.add(new JLabel(s));
		}
	}
}