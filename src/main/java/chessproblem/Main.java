package chessproblem;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import static chessproblem.PieceTypeEnum.*;

public class Main {

    public static void main(String[] args) {
        runTests();
    }

    private static void runTests() {
        //testCase1();
        //testCase2();
        //testCase3();
        //testCase4();
        testCase5();
        //testCase6();
    }

    private static void testCase1() {
        runCase("Test #1", false, () -> {
            System.out.println("Start Test #1");

            Result results = new Solver(3, 3)
                    .addPieces(King, 2)
                    .addPieces(Rook, 1)
                    .solve();

            List<String> expectedResults = new LinkedList<>();
            expectedResults.add("K x K \nx x x \nx R x \n");
            expectedResults.add("K x x \nx x R \nK x x \n");
            expectedResults.add("x R x \nx x x \nK x K \n");
            expectedResults.add("x x K \nR x x \nx x K \n");

            checkSolutions(results.boards, expectedResults);
            return results;
        });
    }

    private static void testCase2() {
        runCase("Test #2", false, () -> {
            Result results = new Solver(4, 4)
                    .addPieces(Knight, 4)
                    .addPieces(Rook, 2).solve();

            List<String> expectedResult = new LinkedList<>();
            expectedResult.add("R x x x \nx N x N \nx x R x \nx N x N \n");
            expectedResult.add("N x N x \nx R x x \nN x N x \nx x x R \n");
            expectedResult.add("N x N x \nx x x R \nN x N x \nx R x x \n");
            expectedResult.add("x R x x \nN x N x \nx x x R \nN x N x \n");
            expectedResult.add("x x R x \nx N x N \nR x x x \nx N x N \n");
            expectedResult.add("x x x R \nN x N x \nx R x x \nN x N x \n");
            expectedResult.add("x N x N \nR x x x \nx N x N \nx x R x \n");
            expectedResult.add("x N x N \nx x R x \nx N x N \nR x x x \n");

            checkSolutions(results.boards, expectedResult);
            return results;
        });
    }

    private static void testCase3() {
        runCase("Test #3", false, () -> {
            System.out.println("Start Test #3");

            List<String> expectedResult = new LinkedList<>();
            expectedResult.add("N . x x \nx x x Q \n. x x x \nx x Q x \n");
            expectedResult.add("N x . x \n. x x x \nx x x Q \nx Q x x \n");
            expectedResult.add("x Q x x \nx x x Q \n. x x x \nN x . x \n");
            expectedResult.add("x Q x x \nx x x . \nQ x x x \nx x . N \n");
            expectedResult.add("x x Q x \nQ x x x \nx x x . \nx . x N \n");
            expectedResult.add("x x Q x \n. x x x \nx x x Q \nN . x x \n");
            expectedResult.add("x x . N \nQ x x x \nx x x . \nx Q x x \n");
            expectedResult.add("x . x N \nx x x . \nQ x x x \nx x Q x \n");

            Result result = new Solver(4, 4)
                    .addPieces(Queen, 2)
                    .addPieces(Knight, 1).solve();
            checkSolutions(result.boards, expectedResult);

            return result;
        });
    }

    private static void testCase4() {
        runCase("Test #4. 8 Queens", false, () -> {
            Result results = new Solver(8, 8).addPieces(Queen, 8).solve();
            assert results.boards.size() == 92;
            return results;
        });
    }

    private static void testCase5() {
        runCase("Test #5", false, () -> {
            Result results = new Solver(7, 7)
                    .addPieces(King, 2)
                    .addPieces(Queen, 2)
                    .addPieces(Bishop, 2)
                    .addPieces(Knight, 1)
                    .solve();
            assert results.boards.size() == 3063828;
            return results;
        });
    }

    private static Result testCase6() {
        return runCase("Test #6", false, () ->
                new Solver(8, 8)
                        .addPieces(King, 2)
                        .addPieces(Queen, 3)
                        .addPieces(Bishop, 4)
                        .addPieces(Knight, 1)
                        .solve());
    }

    private static void checkSolutions(List<Board> solutions, List<String> expectedResults) {
        assert solutions.stream().map(Board::toString).sorted().reduce((acc, s) -> acc + s)
                .equals(expectedResults.stream().sorted().reduce((acc, s) -> acc + s));
    }

    private static Result runCase(String caseName, boolean showResult, Supplier<Result> run) {
        System.out.println(caseName);
        long start = System.currentTimeMillis();
        Result result = run.get();
        int durationSec = (int) (System.currentTimeMillis() - start) / 1000;
        System.out.printf("Finished '%s'. Results count: %d, Time: %d sec. Loops count: %d\n",
                caseName, result.boards.size(), durationSec, result.loopsCount);

        if (showResult) {
            printResults(result.boards);
        }
        return result;
    }

    private static void printResults(List<Board> results) {
        System.out.println("Results Count = " + results.size());
        for (Board b : results) {
            System.out.print(b);
            System.out.println("===");
        }
    }

}
