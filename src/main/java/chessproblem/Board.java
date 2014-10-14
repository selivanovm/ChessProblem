package chessproblem;

import java.util.BitSet;

public class Board {
    public final byte width;
    public final byte height;

    // Keeps only squares that have pieces
    final BitSet squattedSquares;
    // Keeps both squatted squares and guarded ones
    private final BitSet guardedSquares;
    // Keeps array of combined position and piece type. First half of bytes for positions, last - for types.
    final int[] pieces;
    private int verticalGuardedLines;
    private int piecesOnBoard = 0;
    private final short[] maxPiecePosition;
    private String representation;


    /**
     * This constructor is used to create pristine version of board, without any pieces on it.
     */
    public Board(byte width, byte height, int piecesNumber) {
        this.width = width;
        this.height = height;
        this.squattedSquares = new BitSet(this.width * this.height);
        this.guardedSquares = new BitSet(this.width * this.height);
        this.verticalGuardedLines = 0;
        this.pieces = new int[piecesNumber];
        for (int i = 0; i < piecesNumber; i++) {
            pieces[i] = -1;
        }
        this.maxPiecePosition = new short[PieceTypeEnum.values().length];
    }

    private Board(byte width, byte height, BitSet squattedSquares, BitSet guardedSquares,
                  int[] pieces, int verticalGuardedLines,
                  int piecesOnBoard, short[] maxPiecePosition) {
        this.width = width;
        this.height = height;
        this.squattedSquares = squattedSquares;
        this.guardedSquares = guardedSquares;
        this.pieces = pieces;
        this.verticalGuardedLines = verticalGuardedLines;
        this.piecesOnBoard = piecesOnBoard;
        this.maxPiecePosition = maxPiecePosition;
    }

    /**
     * Put piece on board, check if it attacks some other pieces, if it doesn't then return board with a new state,
     * otherwise return null.
     */
    public Board putPiece(Board tmpBoard, PieceTypeEnum piece, int x, int y, PieceGuardedSquaresCache pieceGuardedSquaresCache) {
        int piecePosition = Util.calcArrayPosition(x, y, height);
        boolean squareState = guardedSquares.get(piecePosition);
        boolean alreadyCheckedThisPosition = maxPiecePosition[piece.ordinal()] > piecePosition;
        if (squareState || alreadyCheckedThisPosition) {
            return null;
        } else {
            BitSet squaresGuardedByPiece = pieceGuardedSquaresCache.getGuardedSquares(piece, x, y);

            tmpBoard.squattedSquares.and(squaresGuardedByPiece);
            boolean existentPieceUnderAttack = tmpBoard.squattedSquares.cardinality() != 0;
            if (existentPieceUnderAttack) {
                return null;
            } else {
                tmpBoard.squattedSquares.and(squattedSquares);
                tmpBoard.squattedSquares.or(squattedSquares);
                tmpBoard.squattedSquares.set(piecePosition);

                tmpBoard.guardedSquares.or(squaresGuardedByPiece);
                tmpBoard.guardedSquares.set(piecePosition);

                int positionAndType = Util.packShortsToInt((short) piecePosition, (short) piece.ordinal());
                tmpBoard.pieces[piecesOnBoard] = positionAndType;
                tmpBoard.maxPiecePosition[piece.ordinal()] = (short) piecePosition;

                // Mark guarded vertical line if needed
                if (piece.isGuardLines()) {
                    tmpBoard.verticalGuardedLines = setVerticalGuardedLine(x);
                }
                tmpBoard.piecesOnBoard++;
                return tmpBoard;
            }
        }
    }

    public void resetTo(Board board) {
        squattedSquares.and(board.squattedSquares);
        squattedSquares.or(board.squattedSquares);

        guardedSquares.and(board.guardedSquares);
        guardedSquares.or(board.guardedSquares);

        System.arraycopy(board.pieces, 0, pieces, 0, pieces.length);

        verticalGuardedLines = board.verticalGuardedLines;
        piecesOnBoard = board.piecesOnBoard;

        System.arraycopy(board.maxPiecePosition, 0, maxPiecePosition, 0, maxPiecePosition.length);
    }

    public Board getCopy() {
        return new Board(width, height, (BitSet) squattedSquares.clone(), (BitSet) guardedSquares.clone(),
                pieces.clone(), verticalGuardedLines, piecesOnBoard, maxPiecePosition.clone());
    }

    String getStringRepresentation() {
        if (representation == null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int pos = Util.calcArrayPosition(i, j, height);
                    boolean squattedSquare = guardedSquares.get(pos);
                    if (squattedSquare) {
                        PieceTypeEnum pieceType = getPieceTypeByPosition(pos);
                        if (pieceType != null) {
                            sb.append(pieceType.getChar()).append(' ');
                        } else {
                            sb.append("x ");
                        }
                    } else {
                        sb.append(". ");
                    }
                }
                sb.append("\n");
            }
            representation = sb.toString();
        }
        return representation;
    }

    private PieceTypeEnum getPieceTypeByPosition(int pos) {
        PieceTypeEnum pieceType = null;
        for (int p = 0; p < pieces.length; p++) {
            if (Util.getFirstShortFromInt(pieces[p]) == pos) {
                pieceType = PieceTypeEnum.values()[Util.getSecondShortFromInt(p)];
                break;
            }
        }
        return pieceType;
    }

    @Override
    public String toString() {
        return getStringRepresentation();
    }

    public boolean isVerticalLineGuarded(int x) {
        return Util.isBitSet(verticalGuardedLines, x);
    }

    int setVerticalGuardedLine(int x) {
        return Util.setBit(verticalGuardedLines, x);
    }

    public boolean isEmpty() {
        return piecesOnBoard == 0;
    }

}
