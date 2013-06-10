package tertis.tetris.game.model;

import java.rmi.RemoteException;

import tertis.tetris.game.board.IntMatrix;
import tertis.tetris.game.board.Position;
import tertis.tetris.game.board.piece.ActivePiece;
import tertis.tetris.game.board.piece.Piece;
import tertis.tetris.game.server.PlayerQueue;
import tertis.tetris.game.view.TetrisView;

/**
 * The model of the Tetris game. It's the MODEL of the M-VC pattern.
 */
public class ServerTetrisModel implements TetrisModel, Runnable {

	private Piece nextPiece = new Piece();
	private ActivePiece piece;

	private IntMatrix board;
	private IntMatrix viewBoard;

	private SafeView view;

	private volatile boolean paused = false;
	private volatile boolean stopped = true;

	private volatile int score = 0;

	/**
	 * Constructor
	 */
	public ServerTetrisModel(int height, int width) {
		board = new IntMatrix(height, width);
		viewBoard = new IntMatrix(height, width);
		piece = new ActivePiece(board);
	}

	/**
	 * Set the view of this model.
	 */
	public void setView(TetrisView view) {
		this.view = new SafeView(view);
		this.view.setModel(this);
	}

	/**
	 * Start the game.
	 */
	public void start() {
		stopped = false;
		board.clear();
		piece.next(getNextPiece());
		update();
		score = 0;
		view.scoreChanged();
		Thread t = new Thread(this);
		t.start();
	}
	

	/**
	 * Main loop.
	 */
	public void run() {
		while (!stopped) {
			maybePause();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
			maybePause();
			synchronized (this) {
				if (piece.down()) {
					update();
				} else {
					accept();
					piece.next(getNextPiece());
					update();
					if (piece.getPos().getRow() < 0) {
						stopped = true;
						paused = false;
						view.gameOver();
						break;
					}
				}
			}
		}
	}

	private void update() {
		for (int i = 0; i < board.getHeight(); i++) {
			for (int j = 0; j < board.getWidth(); j++) {
				viewBoard.set(i, j, board.get(i, j));
			}
		}

		if (stopped) {
			//view.boardChanged();
			return;
		}

		IntMatrix shape = piece.getPiece().getShape();
		Position pos = piece.getPos();
		int start = 0;
		
		if (pos.getRow() < 0) {
			start = Math.abs(pos.getRow());
		}
		
		for (int i = start; i < shape.getHeight(); i++) {
			for (int j = 0; j < shape.getWidth(); j++) {
				if (shape.get(i, j) > 0) {
					viewBoard.set(i + pos.getRow(), j + pos.getColumn(), shape.get(i, j));
				}
			}
		}
		view.boardChanged();
	}
	
	private void accept() {
		board.add(piece.getPiece().getShape(), piece.getPos());
		int count = 0;
		int[] todelete = new int[board.getHeight()];
		for (int i = 0; i < board.getHeight(); i++) {
			if (board.isRowOccupied(i)) {
				count++;
				todelete[count - 1] = i;
				board.deleteRow(i);
			}
		}
		if (count > 0) {
			view.rowsToDelete(todelete, count);
			score += count;
			view.scoreChanged();
		}
	}

	/**
	 * Stop the game.
	 */
	public synchronized void stop() {
		stopped = true;
		resume();
		board.clear();
		update();
	}

	/**
	 * Pause the game.
	 */
	public synchronized void pause() {
		paused = true;
	}
	
	private synchronized void maybePause() {
		try {
			while (paused)
				wait();
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Continue the game when paused.
	 */
	public synchronized void resume() {
		paused = false;
		notify();
	}
	
	/**
	 * Return true if the game is stopped.
	 */
	public synchronized boolean isStopped() {
		return stopped;
	}

	/**
	 * Return true if the game is paused.
	 */
	public synchronized boolean isPaused() {
		return paused;
	}
	
	private synchronized boolean isStoppedOrPaused() {
		return stopped || paused;
	}
	
	/**
	 * Return the main board.
	 */
	public IntMatrix getViewBoard() {
		return viewBoard;
	}

	/**
	 * Return current score.
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Return the shape of the next piece.
	 */
	public IntMatrix getPreviewShape() {
		return nextPiece.getShape();
	}
	
	private Piece getNextPiece() {
		Piece tmp = nextPiece;
		nextPiece = new Piece();
		view.previewChanged();
		return tmp;
	}

	/**
	 * Move the cube to left.
	 */
	public void left() {
		if (isStoppedOrPaused())
			return;
		if (piece.left())
			update();
	}

	/**
	 * Move the cube to right.
	 */
	public void right() {
		if (isStoppedOrPaused())
			return;
		if (piece.right())
			update();
	}

	/**
	 * Rotate the cube.
	 */
	public void rotate() {
		if (isStoppedOrPaused())
			return;
		if (piece.rotate())
			update();
	}

	/**
	 * Go down the cube.
	 */
	public void down() {
		if (isStoppedOrPaused())
			return;
		if (piece.down())
			update();
	}

	@Override
	public void connect(TetrisView player) {
		//TODO add the player to the queue
	}

	@Override
	public void disconnect(TetrisView player) {
		//TODO remove the player from queue
	}

	@Override
	public PlayerQueue getPlayerQueue() throws RemoteException {
		//TODO return the player queue
		return null;
	}
}