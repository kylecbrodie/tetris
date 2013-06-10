package tertis.tetris.game.board.piece;

import java.util.Random;

import tertis.tetris.game.board.IntMatrix;
import tertis.tetris.game.board.Position;

public class Piece {

	private int rotation;
	private int piece;

	private static int TOTAL_PIECES = 7;
	private static Random r = new Random();
	private static IntMatrix PIECES[][] = new IntMatrix[TOTAL_PIECES][4];

	static {
		IntMatrix line = new IntMatrix(1, 4, 1);
		PIECES[0] = buildSeriesShape(line);

		IntMatrix square = new IntMatrix(2, 2, 1);
		PIECES[1] = buildSeriesShape(square);

		IntMatrix leftL = new IntMatrix(3, 2, 1);
		leftL.set(0, 1, 0);
		leftL.set(1, 1, 0);
		PIECES[2] = buildSeriesShape(leftL);

		IntMatrix rightL = new IntMatrix(3, 2, 1);
		rightL.set(0, 0, 0);
		rightL.set(1, 0, 0);
		PIECES[3] = buildSeriesShape(rightL);

		IntMatrix leftS = new IntMatrix(3, 2, 1);
		leftS.set(0, 1, 0);
		leftS.set(2, 0, 0);
		PIECES[4] = buildSeriesShape(leftS);

		IntMatrix rightS = new IntMatrix(3, 2, 1);
		rightS.set(0, 0, 0);
		rightS.set(2, 1, 0);
		PIECES[5] = buildSeriesShape(rightS);

		IntMatrix tshape = new IntMatrix(3, 2, 1);
		tshape.set(0, 1, 0);
		tshape.set(2, 1, 0);
		PIECES[6] = buildSeriesShape(tshape);
	}

	private static IntMatrix[] buildSeriesShape(IntMatrix initial) {
		IntMatrix[] rotations = new IntMatrix[4];
		rotations[0] = initial;
		rotations[1] = IntMatrix.transform(rotations[0]);
		rotations[2] = IntMatrix.transform(rotations[1]);
		rotations[3] = IntMatrix.transform(rotations[2]);
		return rotations;
	}

	public Piece() {
		piece = r.nextInt(TOTAL_PIECES);
		rotation = r.nextInt(4);
	}

	/**
	 * Attempts to rotate this piece inside the board at position pos.
	 * 
	 * @param board
	 *            matrix representing the game board
	 * @param pos
	 *            current position of this shape
	 * @return if it successfully rotated this piece
	 */
	public boolean rotate(IntMatrix board, Position pos) {
		int next = (rotation + 1) % 4;

		IntMatrix currShape = getShape();

		int tryCount = currShape.getHeight() - currShape.getWidth() + 1;

		if (tryCount <= 0)
			tryCount = 1;

		Position temp = new Position(pos);
		for (int i = 0; i < tryCount; i++) {
			if (board.canContain(PIECES[piece][next], temp)) {
				rotation = next;
				pos.col = temp.col;
				return true;
			}
			temp.col--;
		}
		return false;
	}

	public IntMatrix getShape() {
		return PIECES[piece][rotation];
	}
}