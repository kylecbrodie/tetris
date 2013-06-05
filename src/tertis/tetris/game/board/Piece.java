package tertis.tetris.game.board;

import java.awt.Polygon;

public enum Piece {
	I,
	L,
	J,
	T,
	O,
	S,
	Z;
	public static Polygon getShape(Piece p) {
		switch(p) {
			case I:
				return new Polygon(new int[]{}, new int[]{}, numPoints);
			case L:
				return new Polygon(new int[]{}, new int[]{}, numPoints);
			case J:
				return new Polygon(new int[]{}, new int[]{}, numPoints);
			case T:
				return new Polygon(new int[]{}, new int[]{}, numPoints);
			case O:
				return new Polygon(new int[]{}, new int[]{}, numPoints);
			case S:
				return new Polygon(new int[]{}, new int[]{}, numPoints);
			case Z:
				return new Polygon(new int[]{}, new int[]{}, numPoints);
			default:
				return null;
		}
	}
}