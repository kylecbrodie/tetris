//Java source code in tetris.java

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * View interface of the tetris game. It is the VIEW in M-VC pattern.
 */
interface TetrisView {
    /**
     * Set the tetris model of the view to establish the two-way association.
     *  This method will be invoked by TetirsModel.setView().
     */
    void setModel(TetrisModel model);
    /**
     * Notify the view that the main map is changed.
     */
    void mapChanged();
    /**
     * Notify the view that the score is changed.
     */
    void scoreChanged();
    /**
     * Notify the view that the preview cube is changed.
     */
    void previewChanged();
    /**
     * Notify the view that there are rows will be deleted in the map.
     */
    void rowsToDelete(int row[], int count);
    /**
     * Notify the view that the game is over.
     */
    void gameOver();
}

/**
 * The model of tetris game. It's the MODEL of M-VC pattern.
 */
class TetrisModel implements Runnable {
    /**
     * Contructor
     */
    public TetrisModel(int height, int width) {
        _map = new IntMatrix(height, width);
        _viewMap = new IntMatrix(height, width);
        _cube = new ActiveCube(_map);        
    }
    /**
     * Set the view of this model.
     */
    public void setView(TetrisView view) {
        _view = view;
        _view.setModel(this);
    }
    /**
     * Start the game.
     */
    public void start() {
        _stopped = false;
        _map.clear();
        _cube.next(getNextCube());
        update();
        _score = 0;
        _view.scoreChanged();
        Thread t = new Thread(this);
        t.start();
        
    }
    /**
     * Stop the game.
     */
    public synchronized void stop() {
        _stopped = true;
        resume();
        _map.clear();
        update();
    }
    /**
     * Return true if the game is stopped.
     */
    public synchronized boolean isStopped() { return _stopped; }
    /**
     * Pause the game.
     */
    public synchronized void pause() {
        _paused = true;
    }
    /**
     * Continue the game when paused.
     */
    public synchronized void resume() {
        _paused = false;
        notify();        
    }
    /**
     * Return true if the game is paused.
     */
    public synchronized boolean isPaused() { return _paused; }
    /**
     * Move the cube to left.
     */
    public void left() {
        if (isStoppedOrPaused()) return;
        if (_cube.left()) update();
    }
    /**
     * Move the cube to right.
     */
    public void right() {
        if (isStoppedOrPaused()) return;
        if (_cube.right()) update();
    }
    /**
     * Rotate the cube.
     */
    public void rotate() {
        if (isStoppedOrPaused()) return;
        if (_cube.rotate()) update();
    }
    /**
     * Go down the cube.
     */
    public void down() {
        if (isStoppedOrPaused()) return;
        if (_cube.down()) update();
    }
    /**
     * Return the main map.
     */
    public IntMatrix getViewMap() { return _viewMap; }
    /**
     * Return current score.
     */
    public int getScore() { return _score; }
    /**
     * Return the shape of the next cube.
     */
    public IntMatrix getPreviewShape() {
        return _nextShape.getShape();
    }
    /**
     * Main loop.
     */
    public void run() {
        while(!_stopped) {
            maybePause();
            try { Thread.sleep(500); }
                catch(InterruptedException e) {}
            maybePause();
            synchronized(this) {
                if (_cube.down()) {
                    update();    
                } else {
                    accept();
                    _cube.next(getNextCube());
                    update();
                    if (_cube.getPos().getRow() < 0) {
                        _stopped = true;
                        _paused = false;
                        _view.gameOver();
                        break;
                    }
                }
            }
        }
    }

    private void update() {
        for (int i = 0; i < _map.getHeight(); i++)
            for (int j = 0; j < _map.getWidth(); j++)
                _viewMap.set(i, j, _map.get(i, j));
        if (_stopped)  {
            _view.mapChanged();
            return;
        }

        IntMatrix shape = _cube.getShape();
        Position pos = _cube.getPos();
        int start = 0;
        if (pos.getRow() < 0) start = Math.abs(pos.getRow());
        for (int i = start; i < shape.getHeight(); i++)
            for (int j = 0; j < shape.getWidth(); j++) {
                if (shape.get(i, j) > 0)
                    _viewMap.set(i + pos.getRow(),
                        j + pos.getColumn(), shape.get(i, j));
            }
        _view.mapChanged();
    }

    private synchronized void maybePause() {
        try {
            while (_paused)
                wait();
        } catch (InterruptedException e) {}
    }

    private void accept() {
        _map.add(_cube.getShape(), _cube.getPos());
        int count = 0;
        int[] todelete = new int[_map.getHeight()];
        for (int i = 0; i < _map.getHeight(); i++) {
            if (_map.isRowOccupied(i)) {
                count++;
                todelete[count-1] = i;
                _map.deleteRow(i);
            }
        }
        if (count > 0) {
            _view.rowsToDelete(todelete, count);
            _score += count;
            _view.scoreChanged();
        }
    }

    private synchronized boolean isStoppedOrPaused() {
        return (_stopped || _paused);
    }

    private CubeShape getNextCube() {
        CubeShape tmp = _nextShape;
        _nextShape = new CubeShape();
        _view.previewChanged();
        return tmp;
    }
 
    private CubeShape _nextShape = new CubeShape();
    
    private IntMatrix _map;
    private IntMatrix _viewMap;
    private ActiveCube _cube;
    private TetrisView _view;
    private volatile boolean _paused = false;
    private volatile boolean _stopped = true;
    private volatile int _score = 0;
}

class CubeShape {
    
    public CubeShape() {
        _shape = _random.nextInt(_shapeCount);
        _direction = _random.nextInt(4);
    }
    
    public boolean rotate(IntMatrix map, Position pos) {
        int next = (_direction + 1) % 4;
        IntMatrix currShape = getShape();
        int tryCount = currShape.getHeight() - currShape.getWidth() + 1;
        if (tryCount <= 0) tryCount = 1;
        Position temp = new Position(pos);
        for (int i = 0; i < tryCount; i++) {
            if (map.contains(_shapes[_shape][next], temp)) {
                _direction = next;
                pos.setColumn(temp.getColumn());
                return true;
            }
            temp.setColumn(temp.getColumn() - 1);
        }
        return false;
    }
    
    public IntMatrix getShape() {
        return _shapes[_shape][_direction];
    }

    private int _direction;
    private int _shape;
    
    private static int _shapeCount = 7;
    
    private static java.util.Random _random = new java.util.Random();
    
    private static IntMatrix _shapes[][] = new IntMatrix[_shapeCount][4];
    static {
        IntMatrix line = new IntMatrix(1, 4, 1);
        _shapes[0] = buildSeriesShape(line);

        IntMatrix square = new IntMatrix(2, 2, 1);
        _shapes[1] = buildSeriesShape(square);
        
        IntMatrix leftL = new IntMatrix(3, 2, 1);
        leftL.set(0, 1, 0);
        leftL.set(1, 1, 0);
        _shapes[2] = buildSeriesShape(leftL);

        IntMatrix rightL =  new IntMatrix(3, 2, 1);
        rightL.set(0, 0, 0);
        rightL.set(1, 0, 0);
        _shapes[3] = buildSeriesShape(rightL);

        IntMatrix leftS = new IntMatrix(3, 2, 1);
        leftS.set(0, 1, 0);
        leftS.set(2, 0, 0);
        _shapes[4] = buildSeriesShape(leftS);

        IntMatrix rightS = new IntMatrix(3, 2, 1);
        rightS.set(0, 0, 0);
        rightS.set(2, 1, 0);
        _shapes[5] = buildSeriesShape(rightS);

        IntMatrix tshape = new IntMatrix(3, 2, 1);
        tshape.set(0, 1, 0);
        tshape.set(2, 1, 0);
        _shapes[6] = buildSeriesShape(tshape);
    }
    private static IntMatrix[] buildSeriesShape(IntMatrix initial) {
        IntMatrix[] shapes = new IntMatrix[4];
        shapes[0] = new IntMatrix(initial);
        shapes[1] = IntMatrix.transform(shapes[0]);
        shapes[2] = IntMatrix.transform(shapes[1]);
        shapes[3] = IntMatrix.transform(shapes[2]);
        return shapes;
    }
}

class ActiveCube {
    
    public ActiveCube(IntMatrix map) {
        _map = map;
    }

    public IntMatrix getShape() { return _shape.getShape(); }

    public Position getPos() { return _pos; }

    public boolean next(CubeShape shape) {
        _shape = shape;
        int column = (_map.getWidth()-shape.getShape().getWidth()) / 2;
        _pos.setColumn(column);
        Position temp = new Position(0, column);
        int i = 0;
        for (; i <= shape.getShape().getHeight(); i++) {
            if (_map.partlyContains(shape.getShape(), temp, i)) {
                    _pos.setRow(-i);
                    break;
            }
        }
        if (i == 0) return true;
        else return false;
    }

    public boolean rotate() {
        return _shape.rotate(_map, _pos);
    }

    public boolean down() {
        Position temp = new Position(_pos.getRow() + 1, _pos.getColumn());
        if (_map.contains(_shape.getShape(), temp)) {
            _pos.setRow(_pos.getRow() + 1);
            return true;
        } else {
            return false;
        }
    }

    public boolean left() {
        return goLeftOrRight(true);
    }

    public boolean right() {
        return goLeftOrRight(false);
    }

    private boolean goLeftOrRight(boolean isLeft) {
        int column = 0;
        if (isLeft) {
            column = _pos.getColumn() - 1;
        } else {
            column = _pos.getColumn() + 1;
        }
        Position temp = new Position(_pos.getRow(), column);
        if (_map.contains(_shape.getShape(), temp)) {
            _pos.setColumn(column);
            return true;
        } else {
            return false;
        }
    }
    
    private CubeShape _shape;
    private Position _pos = new Position();;
    private IntMatrix _map;
}

class SimpleView extends JPanel implements TetrisView {
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
};

class GridPanel extends JPanel {

    public GridPanel(int rols, int cols, boolean hasBorder, Color borderColor) {
        setLayout(new GridLayout(rols, cols));
        _grids = new Grid[rols][cols];
        for (int i = 0; i < rols; i++)
            for (int j = 0; j < cols; j++) {
                _grids[i][j] = new Grid(hasBorder, borderColor);
                add(_grids[i][j]);
            }
    }

    public int getRows() { return _grids.length; }
    
    public int getCols() { return _grids[0].length; }

    public void setModel(IntMatrix model) {
        reset();
        int colBegin = 0;
        if (model.getWidth() < getCols()) {
            colBegin = (getCols() - model.getWidth()) / 2;
        }
        int rowBegin = 0;
        if (model.getHeight() < getRows()) {
            rowBegin = (getRows() - model.getHeight()) / 2;
        }
        for (int i = 0; i < model.getHeight(); i++)
            for (int j = 0; j < model.getWidth(); j++) {
                _grids[i + rowBegin][j + colBegin].set(model.get(i, j));
            }
        repaint();
    }

    public void reset() {
        for (int i = 0; i < getRows(); i++)
            for (int j = 0; j < getCols(); j++)
                _grids[i][j].set(0);
    }

    public void blink(int row[], int count) {
        try {
            setRowsColor(row, 1, Color.RED);
            repaint();
            Thread.sleep(150);
            setRowsColor(row, 1, Color.GRAY);
//             repaint();
//             Thread.sleep(50);
//             setRowsColor(row, 1, Color.YELLOW);
//             repaint();
//             Thread.sleep(50);
//             setRowsColor(row, 1, Color.GREEN);
//             repaint();
//             Thread.sleep(50);
//             setRowsColor(row, 1, Color.CYAN);
//             repaint();
//             Thread.sleep(50);
//             setRowsColor(row, 1, Color.BLUE);
//             repaint();
//             Thread.sleep(50);
//             setRowsColor(row, 1, Color.MAGENTA);
            repaint();
        } catch(InterruptedException e) {}
    }

    private void setRowsColor(int row[], int count, Color color) {
        for (int i = 0; i < count; i++)
            for (int j = 0; j < getCols(); j++) {
                _grids[row[i]][j].setColor(color);
            }
    }

    static class Grid extends JComponent {

        public Grid(boolean hasBorder, Color borderColor) {
            if (hasBorder) setBorder(new LineBorder(borderColor));
        }
    
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            int w = this.getWidth();
            int h = this.getHeight();
            
            g.setColor(_color);
            if (_on > 0)
                g.fillRect(0,0,w,h);
            else
                g.clearRect(0,0,w,h);
        }
        
        public Dimension getPreferredSize() {
            return new Dimension(40, 40);
        }

        public void set(int value) {
            _on = value;
        }    

        public int get() { return _on; }

        public void setColor(Color color) {
            _color = color;
        }
    
        private int _on = 0;
        
        private Color _color = Color.GRAY;
    }

    private Grid[][] _grids;
}

class ScorePanel extends JPanel {
    
    public ScorePanel() {
        _format = new java.text.DecimalFormat("#####");
        _format.setMaximumIntegerDigits(_numberCount);
        _format.setMinimumIntegerDigits(_numberCount);        
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        String str = _format.format(_score);
        int w = this.getWidth();
        int h = this.getHeight();
        g.clearRect(0, 0, w, h);
        g.setColor(Color.RED);
        g.setFont(new Font("Courier", Font.BOLD, 25));
        FontMetrics fm = g.getFontMetrics();
        int fw = fm.stringWidth(str);
        int fh = fm.getAscent();
        g.drawString(str, w/2 - fw/2, h/2 + fh/4);
    }

    public void setScore(int score) {
        _score = score;
        repaint();
    }

    private int _score = 0;
    private static final int _numberCount = 5;
    private java.text.DecimalFormat _format;
}

public class Tetris extends JFrame {
    
    public Tetris(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        SimpleView view = new SimpleView(20,10);
        TetrisModel russia = new TetrisModel(20, 10);
        russia.setView(view);
        getContentPane().add(view, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        Tetris app = new Tetris("Tetris");
        app.pack();
        //center the window
        Dimension dm = app.getToolkit().getScreenSize();
        app.setLocation((int)(dm.getWidth()/2 - app.getWidth()/2),
            (int)(dm.getHeight()/2 - app.getHeight()/2));
        app.show();
    }
}