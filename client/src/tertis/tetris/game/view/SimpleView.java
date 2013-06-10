package tertis.tetris.game.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import tertis.tetris.game.model.TetrisModel;
import tertis.tetris.game.server.PlayerQueue;

@SuppressWarnings("serial")
public class SimpleView extends JPanel implements TetrisView {

	private SafeModel model;
	private GridPanel panel;
	private GridPanel previewPanel;
	private ScorePanel scorePanel;
	private QueuePanel queuePanel;
	private JButton connect;
	private String name;

	public SimpleView(int height, int width, String s) {
		name = s;
		panel = new GridPanel(height, width, true, Color.WHITE);
		previewPanel = new GridPanel(4, 4, false, Color.BLACK);

		scorePanel = new ScorePanel();
		
		queuePanel = new QueuePanel();

		this.setLayout(new BorderLayout());

		this.add(panel, BorderLayout.CENTER);

		JPanel control = new JPanel();
		control.setLayout(new GridLayout(2, 1, 1, 10));

		control.add(createConnectButton());

		JPanel box = new JPanel();
		box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
		box.add(control);

		JPanel preview = new JPanel();
		previewPanel.setPreferredSize(new Dimension(90, 90));
		preview.add(previewPanel);

		JPanel box2 = new JPanel();
		box2.setLayout(new BoxLayout(box2, BoxLayout.Y_AXIS));

		scorePanel.setPreferredSize(new Dimension(80, 30));

		box2.add(scorePanel);
		box2.add(preview);
		box.add(queuePanel);
		

		JPanel all = new JPanel();
		all.setLayout(new BorderLayout());
		all.add(box, BorderLayout.NORTH);
		all.add(box2, BorderLayout.SOUTH);

		this.add(all, BorderLayout.EAST);

		setupKeyboard();
	}

	@Override
	public void setModel(TetrisModel model) {
		this.model = new SafeModel(model);
	}

	public void scoreChanged() {
		scorePanel.setScore(model.getScore());
	}

	public void boardChanged() {
		panel.setModel(model.getViewBoard());
	}

	public void previewChanged() {
		previewPanel.setModel(model.getPreviewShape());
	}
	
	public void queueChanged()
	{
		queuePanel.repaint(model.getPlayerQueue());
	}

	public void gameOver() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JOptionPane.showMessageDialog(SimpleView.this, "GAME OVER.\nYour score is " + model.getScore() + ".", "GAME OVER",
						JOptionPane.INFORMATION_MESSAGE);
				connect.setText("Connect");
			}
		});
	}

	public void rowsToDelete(final int row[], final int count) {
		panel.blink(row, count);
	}

	private JButton createConnectButton() {
		connect = new JButton("Connect");
		connect.setPreferredSize(new Dimension(90, 30));
		final TetrisView view = this;
		connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (model == null)
					return;
				if (true /*TODO determine if the server is available (network connection, etc)*/) {
					model.connect(view);
					connect.setText("Disconnect");
				} else {
					model.disconnect(view);
					connect.setText("Connect");
				}
			}
		});
		return connect;
	}

	private void setupKeyboard() {
		InputMap input = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up");
		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down");
		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "space");
		ActionMap action = this.getActionMap();
		action.put("left", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (model == null)
					return;
				model.left();
			}
		});

		action.put("right", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (model == null)
					return;
				model.right();
			}
		});
		action.put("up", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (model == null)
					return;
				model.rotate();
			}
		});
		action.put("down", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (model == null)
					return;
				model.down();
			}
		});
		action.put("space", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (model == null)
					return;
				model.down();
				model.down();
				model.down();
				model.down();
				model.down();
				model.down();
				model.down();
				model.down();
				model.down();
				model.down();
				model.down();
				model.down();
				model.down();
				model.down();
				model.down();
				model.down();
			}

		});

	}


	@Override
	public void yourTurn() throws RemoteException {
		// TODO send input to server and other stuff
	}
	
	public String getName()
	{
		return name;
	}
	
	
	//TODO remove if unnecessary
//	private void pauseOrResume() {
//		if (model == null)
//			return;
//		if (model.isStopped())
//			return;
//		if (model.isPaused()) {
//			model.resume();
//			pause.setText("Pause");
//		} else {
//			model.pause();
//			pause.setText("Continue");
//		}
//	}
}