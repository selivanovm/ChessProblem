package chessproblem;

import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {

    private final PieceGuardedSquaresCache pieceGuardedSquaresCache = new PieceGuardedSquaresCache(2, 2);

    @Test
    public void testPutPiece() throws Exception {
        int piecesNumber = 2;
        Board emptyBoard = new Board((byte) 2, (byte) 2, piecesNumber);
        assertTrue("Board should be empty", emptyBoard.isEmpty());

        Board nonEmptyBoard = emptyBoard.getCopy();
        emptyBoard.putPiece(nonEmptyBoard, PieceTypeEnum.Rook, 1, 1, pieceGuardedSquaresCache);
        assertFalse("Board shouldn't be empty", nonEmptyBoard.isEmpty());

        int pieceTypeAndPosition = nonEmptyBoard.pieces[0];
        assertTrue("First pieceTypeAndPosition should be set", pieceTypeAndPosition > -1);
        assertTrue("Second pieceTypeAndPosition shouldn't be set", nonEmptyBoard.pieces[1] == -1);

        short piecePosition = Util.getFirstShortFromInt(pieceTypeAndPosition);
        assertEquals("Piece position should be equal to 3", 3, piecePosition);

        int pieceTypeIndex = Util.getSecondShortFromInt(pieceTypeAndPosition);
        assertEquals("The pieceTypeAndPosition should be a Rook", PieceTypeEnum.Rook.ordinal(), pieceTypeIndex);
    }

    @Test
    public void testResetToEmptyBoard() throws Exception {
        Board emptyBoard = new Board((byte) 2, (byte) 2, 2);
        assertTrue("Board should be empty", emptyBoard.isEmpty());

        Board nonEmptyBoard = emptyBoard.getCopy();
        emptyBoard.putPiece(nonEmptyBoard, PieceTypeEnum.Bishop, 0, 0, pieceGuardedSquaresCache);
        assertFalse("Board shouldn't be empty after we put piece on it", nonEmptyBoard.isEmpty());

        nonEmptyBoard.resetTo(emptyBoard);
        assertTrue("Board should be empty after reset to empty board", nonEmptyBoard.isEmpty());
    }

    @Test
    public void testResetTo() throws Exception {
        Board board1 = new Board((byte) 2, (byte) 2, 2);
        assertTrue("Board should be empty", board1.isEmpty());

        Board board2 = board1.getCopy();
        board1.putPiece(board2, PieceTypeEnum.Bishop, 0, 0, pieceGuardedSquaresCache);
        assertFalse("Board shouldn't be empty after we put piece on it", board2.isEmpty());

        assertNotEquals("Empty board shouldn't be equal to non empty board", board2, board1);

        board2.resetTo(board1);
        assertEquals("board1 should be equal to board2 after reset to board2", board1, board2);
    }

    @Test
    public void testGetCopy() throws Exception {
        Board board1 = new Board((byte) 2, (byte) 2, 2);
        assertTrue("Board should be empty", board1.isEmpty());

        Board board2 = board1.getCopy();
        board1.putPiece(board2, PieceTypeEnum.Bishop, 0, 0, pieceGuardedSquaresCache);
        assertFalse("Board shouldn't be empty after we put piece on it", board2.isEmpty());

        assertEquals("Board should be equal to it's copy", board2, board2.getCopy());
    }

    @Test
    public void testIsVerticalLineGuarded() throws Exception {
        Board board1 = new Board((byte) 2, (byte) 2, 2);
        assertTrue("Board should be empty", board1.isEmpty());
        assertFalse("Empty board shouldn't have guarded lines", board1.isVerticalLineGuarded(0));
        assertFalse("Empty board shouldn't have guarded lines", board1.isVerticalLineGuarded(1));

        Board board2 = board1.getCopy();
        board1.putPiece(board2, PieceTypeEnum.Rook, 1, 0, pieceGuardedSquaresCache);
        assertFalse("Board shouldn't have first column guarded", board2.isVerticalLineGuarded(0));
        assertTrue("Board should have second column guarded", board2.isVerticalLineGuarded(1));
    }

    @Test
    public void testIsEmpty() throws Exception {
        Board board = new Board((byte) 1, (byte) 2, 3);
        assertTrue("New board should be empty", board.isEmpty());
    }
}