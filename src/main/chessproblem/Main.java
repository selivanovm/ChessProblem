package chessproblem;

import chessproblem.model.Board;
import chessproblem.model.IPiece;
import chessproblem.model.pieces.*;

import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        testCase1();
        testCase2();
        //testCase3();
        //testCase4();
        testCase5();
        //testCase6();
    }

    private static void testCase1() {
        List<IPiece> pieces = new PiecesListBuilder()
                .addPieces(King.class, 2)
                .addPieces(Rook.class, 1)
                .build();

        List<String> expectedResults = new LinkedList<>();
        expectedResults.add("K x K \nx x x \nx R x \n");
        expectedResults.add("K x x \nx x R \nK x x \n");
        expectedResults.add("x R x \nx x x \nK x K \n");
        expectedResults.add("x x K \nR x x \nx x K \n");

        checkSolutions(new Solver().solve(pieces, 3, 3), expectedResults);
    }

    private static void testCase2() {
        List<IPiece> pieces= new PiecesListBuilder()
                .addPieces(Knight.class, 4)
                .addPieces(Rook.class, 2)
                .build();

        List<String> expectedResult = new LinkedList<>();
        expectedResult.add("R x x x \nx N x N \nx x R x \nx N x N \n");
        expectedResult.add("N x N x \nx R x x \nN x N x \nx x x R \n");
        expectedResult.add("N x N x \nx x x R \nN x N x \nx R x x \n");
        expectedResult.add("x R x x \nN x N x \nx x x R \nN x N x \n");
        expectedResult.add("x x R x \nx N x N \nR x x x \nx N x N \n");
        expectedResult.add("x x x R \nN x N x \nx R x x \nN x N x \n");
        expectedResult.add("x N x N \nR x x x \nx N x N \nx x R x \n");
        expectedResult.add("x N x N \nx x R x \nx N x N \nR x x x \n");
        checkSolutions(new Solver().solve(pieces, 4, 4), expectedResult);
    }

    private static void testCase3() {
        List<IPiece> pieces = new PiecesListBuilder()
                .addPieces(King.class, 2)
                .addPieces(Queen.class, 2)
                .addPieces(Bishop.class, 2)
                .addPieces(Knight.class, 1)
                .build();

        Solver solver = new Solver();
        solver.solve(pieces, 7, 7);
    }

    private static void testCase4() {
        List<IPiece> pieces = new PiecesListBuilder()
                .addPieces(King.class, 2)
                .addPieces(Queen.class, 2)
                .addPieces(Bishop.class, 2)
                .addPieces(Knight.class, 1)
                .build();

        Solver solver = new Solver();
        solver.solve(pieces, 5, 5);
    }

    private static void testCase5() {
        List<IPiece> pieces = new PiecesListBuilder()
                .addPieces(Queen.class, 2)
                .addPieces(Knight.class, 1)
                .build();

        List<String> expectedResult = new LinkedList<>();
        expectedResult.add("N . x x \nx x x Q \n. x x x \nx x Q x \n");
        expectedResult.add("N x . x \n. x x x \nx x x Q \nx Q x x \n");
        expectedResult.add("x Q x x \nx x x Q \n. x x x \nN x . x \n");
        expectedResult.add("x Q x x \nx x x . \nQ x x x \nx x . N \n");
        expectedResult.add("x x Q x \nQ x x x \nx x x . \nx . x N \n");
        expectedResult.add("x x Q x \n. x x x \nx x x Q \nN . x x \n");
        expectedResult.add("x x . N \nQ x x x \nx x x . \nx Q x x \n");
        expectedResult.add("x . x N \nx x x . \nQ x x x \nx x Q x \n");
        checkSolutions(new Solver().solve(pieces, 4, 4), expectedResult);
    }

    private static void testCase6() {
        List<IPiece> pieces = new PiecesListBuilder()
                .addPieces(Queen.class, 8)
                .build();

        assert new Solver().solve(pieces, 8, 8).size() == 92;
    }

    private static void checkSolutions(List<Board> solutions, List<String> expectedResults) {
        assert solutions.stream().map(Board::toString).sorted().reduce((acc, s) -> acc + s)
                .equals(expectedResults.stream().sorted().reduce((acc, s) -> acc + s));
    }

}
