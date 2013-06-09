package tertis.tetris.game.model;

import tertis.tetris.game.board.IntMatrix;
import tertis.tetris.game.board.Position;
import tertis.tetris.game.board.piece.ActiveCube;
import tertis.tetris.game.board.piece.CubeShape;
import tertis.tetris.game.view.TetrisView;

/**
 * The model of tetris game. It's the MODEL of M-VC pattern.
 */
public class TetrisModel implements Runnable {
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