package tertis.tetris.game.board;

/**
 * A helper class to hold a position in an IntMatrix.
 */
public class Position implements Cloneable, Comparable<Position> {
    
	public int row;
    public int col;
	
	/**
     * Constructs a Position.
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }
    /**
     * Copy constructor.
     */
    public Position(Position rhs) {
        this.row = rhs.row;
        this.col = rhs.col;
    }
    /**
     * Constructs a Position with row and column as zero.
     */
    public Position() {
    	row = 0;
    	col = 0;
    }
    
    /**
     * Return current row.
     */
    public int getRow() {
    	return row;
    }
    
    /**
     * Return current column.
     */
    public int getColumn() {
    	return col;
    }
    
    /**
     * Set row.
     */
    public void setRow(int row) {
    	this.row = row;
    }
    
    /**
     * Set column.
     */
    public void setColumn(int col) {
    	this.col = col;
    }
    
    /**
     * Set this equals to another position.
     */
    public void setPosition(Position pos) {
        row = pos.row;
        col = pos.row;
    }
    
    @Override
    public Position clone() {
		return new Position(row, col);
    }
    
    @Override
    public boolean equals(Object o) {
    	if(o instanceof Position) {
    		Position p = (Position) o;
    		return row == p.row && col == p.col;
    	}
    	return false;
    }
    
	@Override
	public int compareTo(Position o) {
		if(o instanceof Position) {
			Position p = (Position) o;
			if(row == p.row) {
				return col - p.col;
			}
			return row - p.row;
		}
		return 0;
	}
}