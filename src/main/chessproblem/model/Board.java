package main.chessproblem.model;

import java.util.List;
import java.util.Optional;

public class Board {
    final int width;
    final int height;
    private final ISquareState[][] squareStates;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.squareStates = new ISquareState[this.width][this.height];
    }

    private Board(ISquareState[][] squareStates, int width, int height) {
        this.width = width;
        this.height = height;
        this.squareStates = squareStates;
    }

    /**
     * Put piece on board, check if it attacks some other pieces, if no then return board with a new state,
     * otherwise return nothing.
     * @param piece piece to put
     * @param x square to put on - x coordinate
     * @param y square to put on - y coordinate
     * @return new board's state, or nothing
     */
    public Optional<Board> putPiece(IPiece piece, int x, int y) {
        ISquareState[][] newBoardArray = new ISquareState[this.width][this.height];
        for (int i = 0; i < this.width; i++) {
            System.arraycopy(squareStates[i], 0, newBoardArray[i], 0, this.height);
        }

        List<SquareCoordinates> attackVectors = piece.getAttackedSquares(x, y, width, height);
        for (SquareCoordinates v : attackVectors) {
            int attackedSquareX = v.x;
            int attackedSquareY = v.y;

            ISquareState squareState = newBoardArray[attackedSquareX][attackedSquareY];
            boolean pieceUnderAttack = squareState != null && squareState != AttackedSquare.INSTANCE;
            if (pieceUnderAttack) {
                return Optional.empty();
            } else {
                newBoardArray[attackedSquareX][attackedSquareY] = AttackedSquare.INSTANCE;
            }
        }
        newBoardArray[x][y] = piece;

        return Optional.of(new Board(newBoardArray, x, y));
    }
}
