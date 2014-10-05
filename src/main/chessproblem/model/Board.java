package chessproblem.model;

import chessproblem.util.BitUtil;

import java.util.List;

public class Board {
    public final int width;
    public final int height;

    private final ISquareState[][] squareStates;

    private final int verticalGuardedLines;
    private final int horizontalGuardedLines;
    private final int guardedDiagonals;
    private final int guardedBackDiagonals;

    private String representation;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.squareStates = new ISquareState[this.width][this.height];
        this.verticalGuardedLines = 0;
        this.horizontalGuardedLines = 0;
        this.guardedDiagonals = 0;
        this.guardedBackDiagonals = 0;
    }

    private Board(Board oldBoard, ISquareState[][] squareStates, int newHGLines, int newVGLines,
                  int newGDiagonals, int newGBDiagonals) {
        this.width = oldBoard.width;
        this.height = oldBoard.height;
        this.squareStates = squareStates;

        this.horizontalGuardedLines = newHGLines;
        this.verticalGuardedLines = newVGLines;
        this.guardedDiagonals = newGDiagonals;
        this.guardedBackDiagonals = newGBDiagonals;
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
            List<SquareCoordinates> attackVectors = piece.getAttackedSquares(x, y, this);
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

            int newHGLines = this.horizontalGuardedLines;
            int newVGLines = this.verticalGuardedLines;
            int newGDiagonals = this.guardedDiagonals;
            int newGBDiagonals = this.guardedBackDiagonals;
            if (piece.isGuardsLines()) {
                newHGLines = setHorizontalLineGuarded(y);
                newVGLines = setVerticalGuardedLine(x);
            }
            if (piece.isGuardsDiagonals()) {
                newGDiagonals = setDiagonalGuarded(x, y);
                newGBDiagonals = setBackDiagonalGuarded(x, y);
            }

            return new Board(this, newBoardArray, newHGLines, newVGLines, newGDiagonals, newGBDiagonals);
        }
    }

    public String getStringRepresentation() {
        if (representation == null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    ISquareState iSquareState = squareStates[i][j];
                    sb.append(iSquareState == null ? ". " : iSquareState.toString() + " ");
                }
                sb.append("\n");
            }
            representation = sb.toString();
        }
        return representation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board = (Board) o;

        return width == board.width && getStringRepresentation().equals(board.getStringRepresentation());
    }

    @Override
    public int hashCode() {
        return getStringRepresentation().hashCode();
    }

    @Override
    public String toString() {
        return getStringRepresentation();
    }

    public boolean isVerticalLineGuarded(int x) {
        return BitUtil.isBitSet(verticalGuardedLines, x);
    }

    public int setVerticalGuardedLine(int x) {
        return BitUtil.setBit(verticalGuardedLines, x);
    }

    public boolean isHorizontalLineGuarded(int y) {
        return BitUtil.isBitSet(horizontalGuardedLines, y);
    }

    public int setHorizontalLineGuarded(int y) {
        return BitUtil.setBit(horizontalGuardedLines, y);
    }

    public boolean isDiagonalGuarded(int x, int y) {
        return BitUtil.isBitSet(guardedDiagonals, getDiagonalNumber(x, y));
    }

    public int setDiagonalGuarded(int x, int y) {
        return BitUtil.setBit(guardedDiagonals, getDiagonalNumber(x, y));
    }

    public boolean isBackDiagonalGuarded(int x, int y) {
        return BitUtil.isBitSet(guardedBackDiagonals, getBackDiagonalNumber(x, y));
    }

    public int setBackDiagonalGuarded(int x, int y) {
        return BitUtil.setBit(guardedBackDiagonals, getBackDiagonalNumber(x, y));
    }

    private int getDiagonalNumber(int x, int y) {
        return x - y + this.height;
    }

    private int getBackDiagonalNumber(int x, int y) {
        return x + y;
    }
}
