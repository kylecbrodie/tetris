package tertis.tetris.game.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

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
public class SimpleView extends JPanel implements TetrisView {
	
    public SimpleView(int height, int width) {
        _panel = new GridPanel(height, width, true, Color.WHITE);
        _previewPanel = new GridPanel(4, 4, false, Color.BLACK);
        
        _scorePanel = new ScorePanel();
        
        this.setLayout(new BorderLayout());

        this.add(_panel, BorderLayout.CENTER);
        
        JPanel control = new JPanel();
        control.setLayout(new GridLayout(2, 1, 1, 10));
        
        control.add(createStartButton());
        control.add(createPauseButton());

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.add(control);

        JPanel preview = new JPanel();
        _previewPanel.setPreferredSize(new Dimension(90, 90));
        preview.add(_previewPanel);

        JPanel box2 = new JPanel();
        box2.setLayout(new BoxLayout(box2, BoxLayout.Y_AXIS));

        _scorePanel.setPreferredSize(new Dimension(80, 30));

        box2.add(_scorePanel);
        box2.add(preview);
        
        JPanel all = new JPanel();
        all.setLayout(new BorderLayout());
        all.add(box, BorderLayout.NORTH);
        all.add(box2, BorderLayout.SOUTH);
        
        this.add(all, BorderLayout.EAST);

        setupKeyboard();
        
    }

    @Override
    public void setModel(TetrisModel model) {
        _model = model;
        _model.start();
    }

    public void scoreChanged() { _scorePanel.setScore(_model.getScore()); }

    public void mapChanged() { _panel.setModel(_model.getViewMap()); }

    public void previewChanged() { _previewPanel.setModel(_model.getPreviewShape()); }

    public void gameOver() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
        JOptionPane.showMessageDialog(SimpleView.this,
            "GAME OVER.\nYour score is " + _model.getScore() + ".",
            "GAME OVER",
            JOptionPane.INFORMATION_MESSAGE);
        _start.setText("Start");
        _pause.setText("Pause");
            }
        });
    }

    public void rowsToDelete(final int row[], final int count) {
        _panel.blink(row, count);
    }

    private JButton createStartButton() {
        _start = new JButton("Stop");
        _start.setPreferredSize(new Dimension(90, 30));
        _start.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (_model == null) return;
                if (_model.isStopped()) {
                    _model.start();
                    _start.setText("Stop");
                } else {
                    _model.stop();
                    _start.setText("Start");
                }
                _pause.setText("Pause");
            }
        });
        return _start;
    }

    private JButton createPauseButton() {
        _pause = new JButton("Pause");
        _pause.setPreferredSize(new Dimension(90, 30));
        _pause.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pauseOrResume();
            }
        });
        return _pause;
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
        action.put("left",
            new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    if (_model == null) return;
                    _model.left();
                }
            });

        action.put("right",
            new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    if (_model == null) return;
                    _model.right();
                }
            });
        action.put("up",
            new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    if (_model == null) return;
                    _model.rotate();
                }
            });
        action.put("down",
            new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    if (_model == null) return;                    
                    _model.down();
                }
            });
        action.put("escape",
            new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    _pause.doClick();
                }
            });
            action.put("space",
            new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    if (_model == null) return;
                    _model.down();
                    _model.down();
                    _model.down();
                    _model.down();
                    _model.down();
                    _model.down();
                    _model.down();
                    _model.down();
                    _model.down();
                    _model.down();
                    _model.down();
                    _model.down();
                    _model.down();
                    _model.down();
                    _model.down();
                    _model.down();
                }
                
            
            });

    }
    private void pauseOrResume() {
        if (_model == null) return;
        if (_model.isStopped()) return;
        if (_model.isPaused()) {
            _model.resume();
            _pause.setText("Pause");
        } else {
            _model.pause();
            _pause.setText("Continue");
        }
    }

    private TetrisModel _model;
    private GridPanel _panel;
    private GridPanel _previewPanel;
    private ScorePanel _scorePanel;
    private JButton _start, _pause;
}