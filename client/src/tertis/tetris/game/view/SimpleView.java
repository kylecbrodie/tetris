package tertis.tetris.game.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

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

@SuppressWarnings("serial")
public class SimpleView extends JPanel implements TetrisView, Runnable {

	private TetrisModel model;
	
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
		box.add(queuePanel);

		JPanel box2 = new JPanel();
		box2.setLayout(new BoxLayout(box2, BoxLayout.Y_AXIS));

		scorePanel.setPreferredSize(new Dimension(80, 30));

		box2.add(scorePanel);
		box2.add(preview);

		JPanel all = new JPanel();
		all.setLayout(new BorderLayout());
		all.add(box, BorderLayout.NORTH);
		all.add(box2, BorderLayout.SOUTH);

		this.add(all, BorderLayout.EAST);
	}

	public void start() {
		Thread t = new Thread(this);
		t.start();
	}
	
	public void run() {
		while(true) {
			if(model != null) {
				scoreChanged();
				boardChanged();
				queueChanged();
				previewChanged();
				try {
					Thread.sleep(16);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void setModel(TetrisModel model) {
		this.model = model;
	}

	@Override
	public void scoreChanged() {
		try {
			scorePanel.setScore(model.getScore());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void boardChanged() {
		try {
			panel.setModel(model.getViewBoard());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void queueChanged() {
		try {
			queuePanel.repaint(model.getPlayerQueue()); //TODO make better
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void previewChanged() {
		try {
			previewPanel.setModel(model.getPreviewShape());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void gameOver() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					JOptionPane.showMessageDialog(SimpleView.this, "GAME OVER.\nYour score is " + model.getScore() + ".", "GAME OVER",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (HeadlessException e) {
					e.printStackTrace();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				connect.setText("Connect");
			}
		});
	}

	@Override
	public void rowsToDelete(final int row[], final int count) {
		panel.blink(row, count);
	}

	private JButton createConnectButton() {
		connect = new JButton("Connect");
		connect.setPreferredSize(new Dimension(90, 30));
		
		//Convoluted way to have bi-directional communication
		TetrisView tmp = null;
		try {
			tmp = (TetrisView) UnicastRemoteObject.exportObject(this, 8080);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		final TetrisView view = tmp;
		connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (model == null) {
					return;
				}
				try {
					if (!model.isStopped()) {
						System.out.println("Server not stopped!");
						model.connect(view);
						connect.setText("Disconnect");
					} else {
						model.disconnect(view);
						connect.setText("Connect");
					}
				} catch (RemoteException e1) {
					e1.printStackTrace();
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
				try {
					model.left();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});

		action.put("right", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (model == null)
					return;
				try {
					model.right();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});
		action.put("up", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (model == null)
					return;
				try {
					model.rotate();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});
		action.put("down", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (model == null)
					return;
				try {
					model.down();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});
		action.put("space", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (model == null) {
					return;
				}
				try {
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
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	public void disableKeyboard() {
		InputMap input = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), null);
		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), null);
		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), null);
		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), null);
		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), null);
		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), null);
	}

	@Override
	public void yourTurn(boolean isYourTurn) {
		if (isYourTurn) {
			setupKeyboard();
		} else {
			disableKeyboard();
		}
	}

	public String getName() {
		return name;
	}
}