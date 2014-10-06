package chessproblem.model;

import chessproblem.util.BitUtil;

import java.util.Arrays;
import java.util.BitSet;

public class Board {
    public final byte width;
    public final byte height;

    private final BitSet squattedSquares;
    private final BitSet guardedSquares;
    private final short[] piecesPositions;
    private final PieceTypeEnum[] piecesTypes;

    private final int verticalGuardedLines;

    private String representation;
    private int piecesOnBoard = 0;

    /**
     * This constructor is used to create pristine version of board, without any pieces on it.
     * @param width
     * @param height
     * @param piecesNumber
     */
    public Board(byte width, byte height, int piecesNumber) {
        this.width = width;
        this.height = height;
        this.squattedSquares = new BitSet(this.width * this.height);
        this.guardedSquares = new BitSet(this.width * this.height);
        this.verticalGuardedLines = 0;
        this.piecesPositions = new short[piecesNumber];
        for (int i = 0; i < piecesNumber; i++) {
            this.piecesPositions[i] = -1;
        }
        this.piecesTypes = new PieceTypeEnum[piecesNumber];
    }

    /**
     * This constructor is used when new piece is added on the board.
     */
    private Board(Board oldBoard, BitSet newSquattedSquares, BitSet newGuardedSquares, int newVGLines,
                  short[] newPiecesPositions, PieceTypeEnum[] newPiecesTypes, int newPiecesOnBoard) {
        this.width = oldBoard.width;
        this.height = oldBoard.height;
        this.squattedSquares = newSquattedSquares;
        this.guardedSquares = newGuardedSquares;
        this.verticalGuardedLines = newVGLines;
        this.piecesOnBoard = newPiecesOnBoard;
        this.piecesPositions = newPiecesPositions;
        this.piecesTypes = newPiecesTypes;
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
        boolean squareState = guardedSquares.get(getBoardPosition(x, y));
        if (squareState) {
            return null;
        } else {
            BitSet squaresGuardedByPiece = piece.getGuardedSquares(x, y, this);

            BitSet newSquattedSquares = (BitSet) squattedSquares.clone();
            newSquattedSquares.and(squaresGuardedByPiece);
            boolean existentPieceUnderAttack = newSquattedSquares.cardinality() != 0;
            if (existentPieceUnderAttack) {
                return null;
            } else {
                newSquattedSquares = (BitSet) squattedSquares.clone();
                newSquattedSquares.set(getBoardPosition(x, y));

                BitSet newGuardedSquares = (BitSet) guardedSquares.clone();
                newGuardedSquares.or(squaresGuardedByPiece);
                newGuardedSquares.set(getBoardPosition(x, y));

                short[] newPiecesPositions = piecesPositions.clone();
                newPiecesPositions[piecesOnBoard] = getBoardPosition(x, y);

                PieceTypeEnum[] newPiecesTypes = piecesTypes.clone();
                newPiecesTypes[piecesOnBoard] = piece.getPieceType();

                // Mark guarded vertical line if needed
                int newVGLines = this.verticalGuardedLines;
                if (piece.isGuardsLines()) {
                    newVGLines = setVerticalGuardedLine(x);
                }
                return new Board(this, newSquattedSquares, newGuardedSquares, newVGLines,
                        newPiecesPositions, newPiecesTypes, piecesOnBoard + 1);
            }
        }
    }


    public String getStringRepresentation() {
        if (representation == null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int pos = getBoardPosition(i, j);
                    boolean squattedSquare = guardedSquares.get(pos);
                    if (squattedSquare) {
                        int pieceNum = getPieceNumByPosition(pos);
                        if (pieceNum > -1) {
                            PieceTypeEnum pieceType = piecesTypes[pieceNum];
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

    private int getPieceNumByPosition(int pos) {
        int pieceNum = -1;
        for (int p = 0; p < piecesPositions.length; p++) {
            if (piecesPositions[p] == pos) {
                pieceNum = p;
                break;
            }
        }
        return pieceNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board = (Board) o;

        if (!squattedSquares.equals(board.squattedSquares)) return false;
        if (piecesPositions.length != board.piecesPositions.length) return false;
        if (piecesTypes.length != board.piecesTypes.length) return false;

        return comparePieceTypesAndPositions(board);
    }

    private boolean comparePieceTypesAndPositions(Board board) {
        boolean positionAndTypesAreEqual = true;
        for (int i = 0; i < piecesOnBoard; i++) {
            short pos = piecesPositions[i];
            int posIndex = board.searchPositionIndex(pos);
            if (!(posIndex > -1 && piecesTypes[i] == board.piecesTypes[posIndex])) {
                positionAndTypesAreEqual = false;
                break;
            }
        }
        return positionAndTypesAreEqual;
    }

    private int searchPositionIndex(short position) {
        for (int i = 0; i < piecesOnBoard; i++) {
            if (this.piecesPositions[i] == position) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int hashCode() {
        int result = squattedSquares.hashCode();

        short[] piecesPositionsCopy = piecesPositions.clone();
        Arrays.sort(piecesPositionsCopy);
        result = 31 * result + Arrays.hashCode(piecesPositionsCopy);

        PieceTypeEnum[] piecesTypesCopy = piecesTypes.clone();
        Arrays.sort(piecesTypesCopy);
        result = 31 * result + Arrays.hashCode(piecesTypesCopy);

        return result;
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

    private short getBoardPosition(int x, int y) {
        return (short) (x * height + y);
    }

    public int getPiecesOnBoard() {
        return piecesOnBoard;
    }

    public boolean isSquareSpotted(int x, int y) {
        return squattedSquares.get(getBoardPosition(x, y));
    }
}
