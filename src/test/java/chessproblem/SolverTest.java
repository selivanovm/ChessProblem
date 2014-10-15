package chessproblem;

import org.junit.Test;

import java.util.*;

import static chessproblem.PieceTypeEnum.*;
import static org.junit.Assert.assertEquals;

public class SolverTest {

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithZeroPieces() throws Exception {
        new Solver(7, 7).solve();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithZeroSizeBoard() throws Exception {
        new Solver(7, 0).addPieces(King, 2).solve();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithTooBigBoard() throws Exception {
        new Solver(128, 10).addPieces(King, 2).solve();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNegativeBoardSide() throws Exception {
        new Solver(128, -10).addPieces(King, 2).solve();
    }

    @Test
    public void testSolveCase3x3R1K2() throws Exception {
        Result results = new Solver(3, 3)
                .addPieces(King, 2)
                .addPieces(Rook, 1)
                .solve();

        List<String> expectedResults =
                new ExpectedResultBuilder()
                        .newBoard()
                        .addBoardLine("K . K")
                        .addBoardLine(". . .")
                        .addBoardLine(". R .")
                        .newBoard()
                        .addBoardLine("K . .")
                        .addBoardLine(". . R")
                        .addBoardLine("K . .")
                        .newBoard()
                        .addBoardLine(". R .")
                        .addBoardLine(". . .")
                        .addBoardLine("K . K")
                        .newBoard()
                        .addBoardLine(". . K")
                        .addBoardLine("R . .")
                        .addBoardLine(". . K")
                        .newBoard()
                        .getStrings();

        checkSolutions(results.solutions, 3, 3, expectedResults);
    }

    @Test
    public void testSolveCase4x4N4R2() throws Exception {
        Result results = new Solver(4, 4)
                .addPieces(Knight, 4)
                .addPieces(Rook, 2).solve();

        List<String> expectedResults =
                new ExpectedResultBuilder()
                        .newBoard()
                        .addBoardLine("R . . .")
                        .addBoardLine(". N . N")
                        .addBoardLine(". . R .")
                        .addBoardLine(". N . N")
                        .newBoard()
                        .addBoardLine("N . N .")
                        .addBoardLine(". R . .")
                        .addBoardLine("N . N .")
                        .addBoardLine(". . . R")
                        .newBoard()
                        .addBoardLine("N . N .")
                        .addBoardLine(". . . R")
                        .addBoardLine("N . N .")
                        .addBoardLine(". R . .")
                        .newBoard()
                        .addBoardLine(". R . .")
                        .addBoardLine("N . N .")
                        .addBoardLine(". . . R")
                        .addBoardLine("N . N .")
                        .newBoard()
                        .addBoardLine(". . R .")
                        .addBoardLine(". N . N")
                        .addBoardLine("R . . .")
                        .addBoardLine(". N . N")
                        .newBoard()
                        .addBoardLine(". . . R")
                        .addBoardLine("N . N .")
                        .addBoardLine(". R . .")
                        .addBoardLine("N . N .")
                        .newBoard()
                        .addBoardLine(". N . N")
                        .addBoardLine("R . . .")
                        .addBoardLine(". N . N")
                        .addBoardLine(". . R .")
                        .newBoard()
                        .addBoardLine(". N . N")
                        .addBoardLine(". . R .")
                        .addBoardLine(". N . N")
                        .addBoardLine("R . . .")
                        .getStrings();


        checkSolutions(results.solutions, 4, 4, expectedResults);
    }

    @Test
    public void testSolveCase4x4Q2N1() throws Exception {
        List<String> expectedResults =
                new ExpectedResultBuilder()
                        .newBoard()
                        .addBoardLine("N . . .")
                        .addBoardLine(". . . Q")
                        .addBoardLine(". . . .")
                        .addBoardLine(". . Q .")
                        .newBoard()
                        .addBoardLine("N . . .")
                        .addBoardLine(". . . .")
                        .addBoardLine(". . . Q")
                        .addBoardLine(". Q . .")
                        .newBoard()
                        .addBoardLine(". Q . .")
                        .addBoardLine(". . . Q")
                        .addBoardLine(". . . .")
                        .addBoardLine("N . . .")
                        .newBoard()
                        .addBoardLine(". Q . .")
                        .addBoardLine(". . . .")
                        .addBoardLine("Q . . .")
                        .addBoardLine(". . . N")
                        .newBoard()
                        .addBoardLine(". . Q .")
                        .addBoardLine("Q . . .")
                        .addBoardLine(". . . .")
                        .addBoardLine(". . . N")
                        .newBoard()
                        .addBoardLine(". . Q .")
                        .addBoardLine(". . . .")
                        .addBoardLine(". . . Q")
                        .addBoardLine("N . . .")
                        .newBoard()
                        .addBoardLine(". . . N")
                        .addBoardLine("Q . . .")
                        .addBoardLine(". . . .")
                        .addBoardLine(". Q . .")
                        .newBoard()
                        .addBoardLine(". . . N")
                        .addBoardLine(". . . .")
                        .addBoardLine("Q . . .")
                        .addBoardLine(". . Q .")
                        .getStrings();

        Result result = new Solver(4, 4)
                .addPieces(Queen, 2)
                .addPieces(Knight, 1).solve();
        checkSolutions(result.solutions, 4, 4, expectedResults);
    }

    @Test
    public void testSolve8Queens() throws Exception {
        assertEquals("8 Queens problem has 92 solutions", 92, new Solver(8, 8).addPieces(Queen, 8).solve().solutions.size());
    }

    @Test
    public void testSolve7x7K2Q2B2N1() throws Exception {
        Result results = new Solver(7, 7)
                .addPieces(King, 2)
                .addPieces(Queen, 2)
                .addPieces(Bishop, 2)
                .addPieces(Knight, 1)
                .solve();
        assertEquals("Incorrect solution size", 3063828, results.solutions.size());
    }


    @Test
    public void testSolve8x8K2Q3B4N1() throws Exception {
        Result results = new Solver(8, 8)
                .addPieces(King, 2)
                .addPieces(Queen, 3)
                .addPieces(Bishop, 4)
                .addPieces(Knight, 1)
                .solve();
        assertEquals("Incorrect solution size", 8112968, results.solutions.size());
    }

    private static List<String> getBoardsRepresentation(BoardsSet solutions, int width, int height) {
        List<String> boardsList = new ArrayList<>();
        List<Integer> piecesList = new ArrayList<>();
        solutions.processSolutions((n) -> {
            if (n.node.pieces != null) {
                for (int[] pieces : n.node.pieces) {
                    piecesList.clear();
                    Collections.addAll(piecesList, Arrays.stream(pieces).boxed().toArray(Integer[]::new));
                    piecesList.sort((p1, p2) -> Util.getFirstShortFromInt(p1) - Util.getFirstShortFromInt(p2));
                    StringBuilder sb = new StringBuilder();
                    int pieceNum = 0;
                    for (int i = 0; i < width; i++) {
                        for (int j = 0; j < height; j++) {
                            int pos = Util.calcArrayPosition(i, j, height);
                            boolean squattedSquare = n.bitSet.get(pos);
                            if (squattedSquare) {
                                PieceTypeEnum pieceType = PieceTypeEnum.values()[Util.getSecondShortFromInt(piecesList.get(pieceNum++))];
                                sb.append(pieceType.getChar()).append(' ');
                            } else {
                                sb.append(". ");
                            }
                        }
                        sb.append("\n");
                    }
                    sb.append("\n");
                    boardsList.add(sb.toString());
                }
            }
        });
        return boardsList;
    }

    private static void checkSolutions(BoardsSet solutions, int width, int height, List<String> expectedResults) {
        List<String> boardsList = getBoardsRepresentation(solutions, width, height);
        assertEquals("Boards representations should be equal",
                expectedResults.stream().sorted().reduce((acc, s) -> acc + s).get(),
                boardsList.stream().sorted().reduce((acc, s) -> acc + s).get());
    }

    private static class ExpectedResultBuilder {
        private final List<String> boardRepresentationList = new LinkedList<>();
        private String boardRepresentation = "";

        void addBoard(String boardRepresentation) {
            if (!boardRepresentation.isEmpty()) {
                this.boardRepresentationList.add(boardRepresentation + "\n");
            }
        }

        ExpectedResultBuilder addBoardLine(String boardLine) {
            boardRepresentation += boardLine + " \n";
            return this;
        }

        ExpectedResultBuilder newBoard() {
            addBoard(boardRepresentation);
            this.boardRepresentation = "";
            return this;
        }

        List<String> getStrings() {
            addBoard(boardRepresentation);
            return boardRepresentationList;
        }
    }

}