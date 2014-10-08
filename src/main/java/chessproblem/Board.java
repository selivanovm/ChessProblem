package chessproblem;

import java.util.Arrays;
import java.util.BitSet;

public class Board {
    public final byte width;
    public final byte height;

    // Keeps only squares that have pieces
    private final BitSet squattedSquares;
    // Keeps both squatted squares and guarded ones
    private final BitSet guardedSquares;
    // Keeps pieces' positions in the order that pieces were put on the board
    private final short[] piecesPositions;
    // Keeps pieces' types in the order that pieces were put on the board
    private final PieceTypeEnum[] piecesTypes;
    private final int verticalGuardedLines;
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
        this.piecesPositions = new short[piecesNumber];
        for (int i = 0; i < piecesNumber; i++) {
            this.piecesPositions[i] = -1;
        }
        this.piecesTypes = new PieceTypeEnum[piecesNumber];
        this.maxPiecePosition = new short[PieceTypeEnum.values().length];
    }

    /**
     * This constructor is used when new piece is added on the board.
     */
    private Board(Board oldBoard, BitSet newSquattedSquares, BitSet newGuardedSquares, int newVGLines,
                  short[] newPiecesPositions, PieceTypeEnum[] newPiecesTypes, int newPiecesOnBoard, short[] newMaxPiecePosition) {
        this.width = oldBoard.width;
        this.height = oldBoard.height;
        this.squattedSquares = newSquattedSquares;
        this.guardedSquares = newGuardedSquares;
        this.verticalGuardedLines = newVGLines;
        this.piecesOnBoard = newPiecesOnBoard;
        this.piecesPositions = newPiecesPositions;
        this.piecesTypes = newPiecesTypes;
        this.maxPiecePosition = newMaxPiecePosition;
    }

    /**
     * Put piece on board, check if it attacks some other pieces, if it doesn't then return board with a new state,
     * otherwise return null.
     */
    public Board putPiece(PieceTypeEnum piece, int x, int y, PieceGuardedSquaresCache pieceGuardedSquaresCache) {
        int piecePosition = Util.calcArrayPosition(x, y, height);
        boolean squareState = guardedSquares.get(piecePosition);
        boolean alreadyCheckedThisPosition = maxPiecePosition[piece.ordinal()] > piecePosition;
        if (squareState || alreadyCheckedThisPosition) {
            return null;
        } else {
            BitSet squaresGuardedByPiece = pieceGuardedSquaresCache.getGuardedSquares(piece, x, y);

            BitSet newSquattedSquares = (BitSet) squattedSquares.clone();
            newSquattedSquares.and(squaresGuardedByPiece);
            boolean existentPieceUnderAttack = newSquattedSquares.cardinality() != 0;
            if (existentPieceUnderAttack) {
                return null;
            } else {
                newSquattedSquares = (BitSet) squattedSquares.clone();
                newSquattedSquares.set(piecePosition);

                BitSet newGuardedSquares = (BitSet) guardedSquares.clone();
                newGuardedSquares.or(squaresGuardedByPiece);
                newGuardedSquares.set(piecePosition);

                short[] newPiecesPositions = piecesPositions.clone();
                newPiecesPositions[piecesOnBoard] = (short) piecePosition;

                PieceTypeEnum[] newPiecesTypes = piecesTypes.clone();
                newPiecesTypes[piecesOnBoard] = piece;

                short[] newMaxPiecePosition = maxPiecePosition.clone();
                newMaxPiecePosition[piece.ordinal()] = (short) piecePosition;

                // Mark guarded vertical line if needed
                int newVGLines = this.verticalGuardedLines;
                if (piece.isGuardLines()) {
                    newVGLines = setVerticalGuardedLine(x);
                }
                return new Board(this, newSquattedSquares, newGuardedSquares, newVGLines,
                        newPiecesPositions, newPiecesTypes, piecesOnBoard + 1, newMaxPiecePosition);
            }
        }
    }


    String getStringRepresentation() {
        if (representation == null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int pos = Util.calcArrayPosition(i, j, height);
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

    /**
     * Used only for putting solution into result map.
     * On incomplete solution will raise NPE, since piecesTypes contains nulls
     */
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

    /**
     * Used only for putting solution into result map.
     */
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
