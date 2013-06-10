package tertis.tetris.game.model;

import tertis.tetris.game.board.IntMatrix;
import tertis.tetris.game.board.Position;
import tertis.tetris.game.board.piece.ActivePiece;
import tertis.tetris.game.board.piece.Piece;
import tertis.tetris.game.server.PlayerQueue;

/**
 * The model of the Tetris game. It's the MODEL of the M-VC pattern.
 */
public class ServerTetrisModel implements TetrisModel, Runnable {

	private Piece nextPiece = new Piece();
	private ActivePiece piece;

	private IntMatrix board;
	private IntMatrix viewBoard;
	
	private PlayerQueue queue = new PlayerQueue(); 

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
	 * Start the game.
	 */
	public void start() {
		stopped = false;
		board.clear();
		piece.next(getNextPiece());
		update();
		score = 0;
		Thread t = new Thread(this);
		t.start();
	}
	

	/**
	 * Main loop.
	 */
	public void run() {
		while (!stopped) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
			synchronized (this) {
				if (piece.down()) {
					update();
				} else {
					accept();
					piece.next(getNextPiece());
					update();
					if (piece.getPos().getRow() < 0) {
						stopped = true;
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
	}
	
	private void accept() {
		board.add(piece.getPiece().getShape(), piece.getPos());
		
		int count = 0;
		
		for (int i = 0; i < board.getHeight(); i++) {
			if (board.isRowOccupied(i)) {
				count++;
				board.deleteRow(i);
			}
		}
		if (count > 0) {
			score += count;
		}
		queue.reQueue();
	}

	/**
	 * Stop the game.
	 */
	public synchronized void stop() {
		stopped = true;
		board.clear();
		update();
	}
	
	/**
	 * Return true if the game is stopped.
	 */
	@Override
	public synchronized boolean isStopped() {
		return stopped;
	}
	
	@Override
	public boolean isMyTurn(String player) {
		return queue.isMyTurn(player);
	}
	
	/**
	 * Return the main board.
	 */
	@Override
	public IntMatrix getViewBoard() {
		return viewBoard;
	}
	
	@Override
	public PlayerQueue getPlayerQueue() {
		return queue;
	}

	/**
	 * Return current score.
	 */
	@Override
	public int getScore() {
		return score;
	}

	/**
	 * Return the shape of the next piece.
	 */
	@Override
	public IntMatrix getPreviewShape() {
		return nextPiece.getShape();
	}
	
	private Piece getNextPiece() {
		Piece tmp = nextPiece;
		nextPiece = new Piece();
		return tmp;
	}

	/**
	 * Move the cube to left.
	 */
	@Override
	public void left() {
		if (isStopped())
			return;
		if (piece.left())
			update();
	}

	/**
	 * Move the cube to right.
	 */
	@Override
	public void right() {
		if (isStopped())
			return;
		if (piece.right())
			update();
	}

	/**
	 * Rotate the cube.
	 */
	@Override
	public void rotate() {
		if (isStopped())
			return;
		if (piece.rotate())
			update();
	}

	/**
	 * Go down the cube.
	 */
	@Override
	public void down() {
		if (isStopped())
			return;
		if (piece.down())
			update();
	}

	@Override
	public boolean connect(String player) {
		return queue.add(player);
	}

	@Override
	public void disconnect(String player) {
		queue.remove(player);
	}
}