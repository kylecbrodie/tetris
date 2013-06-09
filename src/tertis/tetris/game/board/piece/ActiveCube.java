package tertis.tetris.game.board.piece;

import tertis.tetris.game.board.IntMatrix;
import tertis.tetris.game.board.Position;

public class ActiveCube {
    
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