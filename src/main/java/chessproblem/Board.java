package chessproblem;

import java.util.Arrays;
import java.util.BitSet;

public class Board {
    public static final int MAX_BOARD_SIDE_SIZE = 127;

    public final byte width;
    public final byte height;

    // Keeps only squares that have pieces
    final BitSet squattedSquares;
    // Keeps both squatted squares and guarded ones
    private final BitSet guardedSquares;
    // Keeps array of combined position and piece type. First half of bytes for positions, last - for types.
    final int[] pieces;
    private int piecesOnBoard = 0;
    private final short[] maxPiecePosition;


    /**
     * This constructor is used to create pristine version of board, without any pieces on it.
     */
    public Board(byte width, byte height, int piecesNumber) {
        this.width = width;
        this.height = height;
        this.squattedSquares = new BitSet(this.width * this.height);
        this.guardedSquares = new BitSet(this.width * this.height);
        this.pieces = new int[piecesNumber];
        for (int i = 0; i < piecesNumber; i++) {
            pieces[i] = -1;
        }
        this.maxPiecePosition = new short[PieceTypeEnum.values().length];
    }

    private Board(byte width, byte height, BitSet squattedSquares, BitSet guardedSquares,
                  int[] pieces, int piecesOnBoard, short[] maxPiecePosition) {
        this.width = width;
        this.height = height;
        this.squattedSquares = squattedSquares;
        this.guardedSquares = guardedSquares;
        this.pieces = pieces;
        this.piecesOnBoard = piecesOnBoard;
        this.maxPiecePosition = maxPiecePosition;
    }

    /**
     * Put piece on board, check if it attacks some other pieces, if it doesn't then return board with a new state,
     * otherwise return null.
     *
     * @param tmpBoard board that will represent original board and new piece on it
     * @param piece piece to be put on board
     * @param x column number of new piece
     * @param y row number where of new piece
     * @param pieceGuardedSquaresCache cache that keeps guarded squares bit masks for each of piece type and every square
     *                                 on the board
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

        piecesOnBoard = board.piecesOnBoard;

        System.arraycopy(board.maxPiecePosition, 0, maxPiecePosition, 0, maxPiecePosition.length);
    }

    public Board getCopy() {
        return new Board(width, height, (BitSet) squattedSquares.clone(), (BitSet) guardedSquares.clone(),
                pieces.clone(), piecesOnBoard, maxPiecePosition.clone());
    }

    public boolean isEmpty() {
        return piecesOnBoard == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board = (Board) o;

        if (height != board.height) return false;
        if (piecesOnBoard != board.piecesOnBoard) return false;
        if (width != board.width) return false;
        if (!guardedSquares.equals(board.guardedSquares)) return false;
        if (!Arrays.equals(maxPiecePosition, board.maxPiecePosition)) return false;
        if (!Arrays.equals(pieces, board.pieces)) return false;
        if (!squattedSquares.equals(board.squattedSquares)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) width;
        result = 31 * result + (int) height;
        result = 31 * result + squattedSquares.hashCode();
        result = 31 * result + guardedSquares.hashCode();
        result = 31 * result + Arrays.hashCode(pieces);
        result = 31 * result + piecesOnBoard;
        result = 31 * result + Arrays.hashCode(maxPiecePosition);
        return result;
    }
}
