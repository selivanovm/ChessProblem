package chessproblem;

import chessproblem.model.PieceTypeEnum;

import java.util.BitSet;

public class PieceGuardedSquaresCache {

    private int boardWidth;
    private int boardHeight;
    private final BitSet[][] guardedSquaresMasks;

    public PieceGuardedSquaresCache(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.guardedSquaresMasks = new BitSet[PieceTypeEnum.values().length][boardWidth * boardHeight];
        for (PieceTypeEnum pieceType : PieceTypeEnum.values()) {
            for (int x = 0; x < boardWidth; x++) {
                for (int y = 0; y < boardHeight; y++) {
                    guardedSquaresMasks[pieceType.ordinal()][Util.calcArrayPosition(x, y, boardHeight)] = calculateGuardedSquares(pieceType, x, y);
                }
            }
        }
    }

    public BitSet getGuardedSquares(PieceTypeEnum piece, int x, int y) {
        return this.guardedSquaresMasks[piece.ordinal()][Util.calcArrayPosition(x, y, boardHeight)];
    }

    private BitSet calculateGuardedSquares(PieceTypeEnum pieceType, int x, int y) {
        BitSet guardedSquares = new BitSet(boardWidth * boardHeight);
        switch (pieceType) {
            case King:
                for (byte i = -1; i < 2; i++) {
                    addSquare(x + 1, y + i, boardWidth, boardHeight, guardedSquares);
                    addSquare(x - 1, y + i, boardWidth, boardHeight, guardedSquares);
                }
                addSquare(x, y - 1, boardWidth, boardHeight, guardedSquares);
                addSquare(x, y + 1, boardWidth, boardHeight, guardedSquares);
                break;
            
            case Queen:
                addFullCross(x, y, boardWidth, boardHeight, guardedSquares);
                addFullDiagonalCross(x, y, boardWidth, boardHeight, guardedSquares);
                break;

            case Rook:
                addFullCross(x, y, boardWidth, boardHeight, guardedSquares);
                break;

            case Bishop:
                addFullDiagonalCross(x, y, boardWidth, boardHeight, guardedSquares);
                break;

            case Knight:
                addSquare(x + 2, y + 1, boardWidth, boardHeight, guardedSquares);
                addSquare(x - 2, y + 1, boardWidth, boardHeight, guardedSquares);

                addSquare(x + 2, y - 1, boardWidth, boardHeight, guardedSquares);
                addSquare(x - 2, y - 1, boardWidth, boardHeight, guardedSquares);

                addSquare(x + 1, y + 2, boardWidth, boardHeight, guardedSquares);
                addSquare(x + 1, y - 2, boardWidth, boardHeight, guardedSquares);

                addSquare(x - 1, y + 2, boardWidth, boardHeight, guardedSquares);
                addSquare(x - 1, y - 2, boardWidth, boardHeight, guardedSquares);
                break;
        }
        return guardedSquares;
    }

    private void addSquare(int x, int y, int boardWidth, int boardHeight, BitSet guardedSquares) {
        if (x >= 0 && x < boardWidth && y >= 0 && y < boardHeight) {
            addSquareNoChecks(x, y, boardHeight, guardedSquares);
        }
    }

    private void addSquareNoChecks(int x, int y, int boardHeight, BitSet guardedSquares) {
        guardedSquares.set(Util.calcArrayPosition(x, y, boardHeight));
    }

    private void addFullCross(int x, int y, int width, int height, BitSet guardedSquares) {
        for (int i = 0; i < width; i++) {
            if (i != x) {
                addSquareNoChecks(i, y, height, guardedSquares);
            }
        }
        for (int j = 0; j < height; j++) {
            if (j != y) {
                addSquareNoChecks(x, j, height, guardedSquares);
            }
        }
    }

    private void addFullDiagonalCross(int x, int y, int boardWidth, int boardHeight, BitSet guardedSquares) {
        int diagonalX = Math.max(0, x - y);
        int diagonalY = Math.max(0, y - x);

        int backDiagonalX = Math.max(0, x - (boardHeight - y - 1));
        int backDiagonalY = Math.min(boardHeight - 1, y + x);

        while (diagonalX < boardWidth && diagonalY < boardHeight) {
            addSquareNoChecks(diagonalX++, diagonalY++, boardHeight, guardedSquares);
        }

        while (backDiagonalX < boardWidth && backDiagonalY >= 0) {
            addSquareNoChecks(backDiagonalX++, backDiagonalY--, boardHeight, guardedSquares);
        }
    }
    
}
