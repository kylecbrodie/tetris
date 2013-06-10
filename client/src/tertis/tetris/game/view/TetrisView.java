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

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import tertis.tetris.game.model.TetrisModel;

@SuppressWarnings("serial")
public class TetrisView extends JPanel implements Runnable {

	private TetrisModel model;
	
	private GridPanel panel;
	private GridPanel previewPanel;
	private ScorePanel scorePanel;
	private QueuePanel queuePanel;
	private JButton connect;
	private boolean connected = false;
	private String name;

	public TetrisView(int height, int width) {
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
				try {
					if(model.isStopped()) {
						gameOver();
						return;
					}
					
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				
				ourTurn(true);
				boardChanged();
				queueChanged();
				previewChanged();
				scoreChanged();
			}
		}
	}
	
	public void setModel(TetrisModel model) {
		this.model = model;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void scoreChanged() {
		try {
			scorePanel.setScore(model.getScore());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void boardChanged() {
		try {
			panel.setModel(model.getViewBoard());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void queueChanged() {
		try {
			queuePanel.setQueue(model.getPlayerQueue());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void previewChanged() {
		try {
			previewPanel.setModel(model.getPreviewShape());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void gameOver() {
		try {
			JOptionPane.showMessageDialog(TetrisView.this, "GAME OVER.\nGobal lines cleared: " + model.getScore() + "\nGlobal Coordination Failed.",
					"GAME OVER", JOptionPane.INFORMATION_MESSAGE);
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private JButton createConnectButton() {
		connect = new JButton("Connect");
		connect.setPreferredSize(new Dimension(90, 30));
	
		connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (model == null) {
					return;
				}
				try {
					if (!connected) {
						connected = model.connect(name);
						//TODO notify the player their name isn't unique
						connect.setText("Disconnect");
					} else {
						model.disconnect(name);
						connect.setText("Connect");
						connected = false;
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

	private boolean ourTurn = false;
	public void ourTurn(boolean isOurTurn) {
		if(isOurTurn == ourTurn) return;
		
		if (isOurTurn) {
			setupKeyboard();
			ourTurn = true;
		} else {
			disableKeyboard();
			ourTurn = false;
		}
	}
}