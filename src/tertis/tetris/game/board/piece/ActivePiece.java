package tertis.tetris.game.board.piece;

import tertis.tetris.game.board.IntMatrix;
import tertis.tetris.game.board.Position;

public class ActivePiece {

	private Piece piece;
	private Position pos = new Position();
	private IntMatrix board;

	public ActivePiece(IntMatrix board) {
		this.board = board;
	}

	public Piece getPiece() {
		return piece;
	}

	public Position getPos() {
		return pos;
	}

	public boolean next(Piece piece) {
		this.piece = piece;
		int column = (board.width - piece.getShape().width) / 2;
		pos.setColumn(column);

		Position temp = new Position(0, column);
		int i = 0;
		for (; i <= piece.getShape().height; i++) {
			if (board.canPartlyContain(piece.getShape(), temp, i)) {
				pos.setRow(-i);
				break;
			}
		}
		
		return i == 0;
	}

	public boolean rotate() {
		return piece.rotate(board, pos);
	}

	public boolean down() {
		Position temp = new Position(pos.getRow() + 1, pos.getColumn());
		
		if (board.canContain(piece.getShape(), temp)) {
			pos.setRow(pos.getRow() + 1);
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
			column = pos.getColumn() - 1;
		} else {
			column = pos.getColumn() + 1;
		}
		Position temp = new Position(pos.getRow(), column);
		if (board.canContain(piece.getShape(), temp)) {
			pos.setColumn(column);
			return true;
		} else {
			return false;
		}
	}
}