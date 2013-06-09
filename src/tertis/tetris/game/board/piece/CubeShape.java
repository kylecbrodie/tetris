package tertis.tetris.game.board.piece;

import tertis.tetris.game.board.IntMatrix;
import tertis.tetris.game.board.Position;

public class CubeShape {
    
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