package chessproblem.model;

import java.util.List;

public class Board {
    public final int width;
    public final int height;

    private final ISquareState[][] squareStates;
    private final String stringRepresentation;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.squareStates = new ISquareState[this.width][this.height];
        this.stringRepresentation = getStringRepresentation();
    }

    private Board(ISquareState[][] squareStates, int width, int height) {
        this.width = width;
        this.height = height;
        this.squareStates = squareStates;
        this.stringRepresentation = getStringRepresentation();
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
        ISquareState squareState = squareStates[x][y];
        if (squareState != null) {
            return null;
        } else {
            List<SquareCoordinates> attackVectors = piece.getAttackedSquares(x, y, width, height);
            for (SquareCoordinates v : attackVectors) {
                int attackedSquareX = v.x;
                int attackedSquareY = v.y;

                squareState = squareStates[attackedSquareX][attackedSquareY];
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

            newBoardArray[x][y] = piece;
            return new Board(newBoardArray, width, height);
        }
    }

    public String getBoardHashString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                ISquareState iSquareState = squareStates[i][j];
                sb.append(iSquareState == null ? "." : iSquareState.toString());
            }
        }
        return sb.toString();
    }

    private String getStringRepresentation() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                ISquareState iSquareState = squareStates[i][j];
                sb.append(iSquareState == null ? ". " : iSquareState.toString() + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board = (Board) o;

        return width == board.width && stringRepresentation.equals(board.stringRepresentation);
    }

    @Override
    public int hashCode() {
        return stringRepresentation.hashCode();
    }

    @Override
    public String toString() {
        return stringRepresentation;
    }

}
