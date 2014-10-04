package main.chessproblem.model;

import java.util.List;

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
     * Put piece on board, check if it attacks some other pieces, if it doesn't then return board with a new state,
     * otherwise return null.
     * @param piece piece to put
     * @param x square to put on - x coordinate
     * @param y square to put on - y coordinate
     * @return new board's state, or nothing
     */
    public Board putPiece(IPiece piece, int x, int y) {
        List<SquareCoordinates> attackVectors = piece.getAttackedSquares(x, y, width, height);
        for (SquareCoordinates v : attackVectors) {
            int attackedSquareX = v.x;
            int attackedSquareY = v.y;
/*
            System.out.println(x + " <> " + y);
            System.out.println(attackedSquareX + " <> " + attackedSquareY);
*/
            ISquareState squareState = squareStates[attackedSquareX][attackedSquareY];
            boolean squareIsSquatted = squareState != null && squareState != AttackedSquare.INSTANCE;
            if (squareIsSquatted) {
                return null;
            }
        }

        ISquareState[][] newBoardArray = new ISquareState[this.width][this.height];
        for (int i = 0; i < this.width; i++) {
            System.arraycopy(squareStates[i], 0, newBoardArray[i], 0, this.height);
        }
        for (SquareCoordinates v : attackVectors) {
            newBoardArray[v.x][v.y] = AttackedSquare.INSTANCE;
        }
        //System.out.printf("bw = %d, bh = %d\n", newBoardArray.length, newBoardArray[0].length);
        newBoardArray[x][y] = piece;
        return new Board(newBoardArray, width, height);
    }

    public void print() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                ISquareState iSquareState = squareStates[i][j];
                System.out.print(iSquareState == null ? ". " : iSquareState.toString() + " ");
            }
            System.out.println();
        }
    }
}
