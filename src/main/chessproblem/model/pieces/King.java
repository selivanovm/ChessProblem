package chessproblem.model.pieces;

import chessproblem.model.Board;
import chessproblem.model.SquareCoordinates;
import chessproblem.model.SquareStateEnum;

import java.util.LinkedList;
import java.util.List;

public class King extends AbstractPiece {

    public King() {
        super(false, false, SquareStateEnum.King);
    }

    @Override
    public List<SquareCoordinates> getAttackedSquares(int x, int y, Board board) {
        List<SquareCoordinates> result = new LinkedList<>();
        for (int i = -1; i < 2; i++) {
            addSquare(result, x + 1, y + i, board.width, board.height);
            addSquare(result, x - 1, y + i, board.width, board.height);
        }
        addSquare(result, x, y - 1, board.width, board.height);
        addSquare(result, x, y + 1, board.width, board.height);
        return result;
    }

    @Override
    public String toString() {
        return "K";
    }
}
