package tertis.tetris.game.board;

import java.util.Arrays;

/**
 * A two dimensional integer array wrapper class.
 * For our uses, 0 represents unoccupied and all
 * other values represent occupied.
 */
public final class IntMatrix implements Cloneable {
	
	public final int height;
    public final int width;
    private final int data[][];
    
    /**
     * Constructs an IntMatrix with initial value.
     */
    public IntMatrix(int height, int width, int initialValue) {
        this.height = height;
        this.width = width;
        data = new int[height][width];
        for(int i = 0; i < height; i++) {
        	Arrays.fill(data[i], initialValue);
        }
    }
    
    /**
     * Copy constructor.
     */
    public IntMatrix(IntMatrix rhs) {
        this.height = rhs.height;
        this.width = rhs.width;
        data = rhs.data.clone();
    }
    
    /**
     * Contructs an IntMatrix with all elements unoccupied.
     */
    public IntMatrix(int height, int width) {
        this(height, width, 0);
    }
    
    /**
     * Return height.
     */
    public int getHeight() {
    	return height;
    }
    
    /**
     * Return width.
     */
    public int getWidth() {
    	return width;
    }

    /**
     * Set the value of an element.
     */
    public void set(int row, int col, int value) {
        data[row][col] = value;
    }
    
    /**
     * Return the value of an element.
     */
    public int get(int row, int col) {
        return data[row][col];
    }
    
    /**
     * Return true if the other IntMatrix can be placed in this one.
     */
    public boolean contains(IntMatrix other, Position pos) {
        return partlyContains(other, pos, 0);
    }
    
    /**
     * Return true if part of the other IntMatrix can be placed in this one.
     */
    public boolean partlyContains(IntMatrix other, Position pos, int begin) {
        if (pos.getRow() < 0 || pos.getColumn() < 0)
            return false;
        if (pos.getColumn() + other.getWidth() > this.width)
            return false;
        if (pos.getRow() + other.getHeight() - begin > this.height)
            return false;
        for (int i = begin; i < other.getHeight(); i++) {
            for (int j = 0; j < other.getWidth(); j++) {
                if (other.get(i, j) > 0 &&
                        this.get(i + pos.getRow() - begin,
                            j + pos.getColumn()) > 0)
                    return false;
            }
        }
        return true;
    }
    
    /**
     * Add the other IntMatrix to this one.
     */
    public void add(IntMatrix other, Position pos) {
        for (int i = 0; i < other.getHeight(); i++) {
            for (int j = 0; j < other.getWidth(); j++) {
                if (other.get(i, j) > 0)
                    this.set(pos.getRow() + i,
                        pos.getColumn() + j,
                        other.get(i, j));
            }
        }
    }
    
    /**
     * Return true if all the elements in the specified row are occupied.
     */
    public boolean isRowOccupied(int row) {
        for (int i = 0; i < width; i++) {
            if (data[row][i] == 0) {
            	return false;
            }
        }
        return true;
    }
    
    /**
     * Delete the specified row and move down the rows above.
     */
    public void deleteRow(int index) {
        for (int i = index; i > 0; i--) {
            for (int j = 0; j < width; j++) {
                data[i][j] = data[i-1][j];
            }
        }
        clearRow(0);
    }
    
    /**
     * Set all elements to unoccupied.
     */
    public void clear() {
        for (int i = 0; i < height; i++) {
            clearRow(i);
        }
    }
    
    private void clearRow(int index) {
        for (int j = 0; j < width; j++) {
            data[index][j] = 0;
        }
    }
    
    /**
     * Rotate the source IntMatrix clockwise and return the new created one.
     */
    public static IntMatrix transform(IntMatrix source) {
        IntMatrix target = new IntMatrix(source.getWidth(), source.getHeight());
        for (int i = 0; i < target.getHeight(); i++) {
            for (int j = 0; j < target.getWidth(); j++) {
                target.set(i, j, source.get(source.getHeight() - j - 1, i));
            }
        }
        return target;
    }
    
    /**
     * Clone a instance.
     */
    @Override
    public IntMatrix clone() {
        return new IntMatrix(this);
    }
    
    /**
     * Dump this IntMatrix for debug.
     */
    public final void dump() {
        System.out.println("<<------------->>");
        System.out.print("Height=");System.out.print(height);
        System.out.print(" Width=");System.out.print(width);
        System.out.println();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(data[x + y * width]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}