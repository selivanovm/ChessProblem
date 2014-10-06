package chessproblem;

import chessproblem.model.Board;
import static chessproblem.model.PieceTypeEnum.*;

import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        runTests();
        runPerfTest();
    }

    private static void runPerfTest() {
        System.out.println("Start Perf Test");
        long start = System.currentTimeMillis();
        List<Board> solutions = null;
        for (int i = 0; i < 100; i++) {
            List<Board> newSolutions = perfTestCase();

            if (solutions != null) {
                checkSolution2(solutions, newSolutions);
            }
            solutions = newSolutions;
        }
        int durationSec = (int) (System.currentTimeMillis() - start) / 1000;
        System.out.printf("Performance test time: %d sec.\n", durationSec);
    }

    private static void runTests() {
        testCase1();
        testCase2();
        testCase3();
        testCase4();
        testCase5();
    }

    private static void testCase1() {
        System.out.println("Start Test #1");

        List<Board> results = new Solver(3, 3)
                .addPieces(King, 2)
                .addPieces(Rook, 1)
                .solve();

        List<String> expectedResults = new LinkedList<>();
        expectedResults.add("K x K \nx x x \nx R x \n");
        expectedResults.add("K x x \nx x R \nK x x \n");
        expectedResults.add("x R x \nx x x \nK x K \n");
        expectedResults.add("x x K \nR x x \nx x K \n");

        printResults(results);
        checkSolutions(results, expectedResults);
    }

    private static void printResults(List<Board> results) {
        System.out.println("Results Count = " + results.size());
        for (Board b : results) {
            System.out.print(b);
            System.out.println("===");
        }
    }

    private static void testCase2() {
        System.out.println("Start Test #2");

        List<Board> results = new Solver(4, 4)
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


        expectedResult.forEach(System.out::println);
        printResults(results);
        checkSolutions(results, expectedResult);
    }

    private static void testCase3() {
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
        checkSolutions(new Solver(4, 4)
                .addPieces(Queen, 2)
                .addPieces(Knight, 1).solve(), expectedResult);
    }

    private static void testCase4() {
        System.out.println("Start Test #4");
        List<Board> results = new Solver(8, 8).addPieces(Queen, 8).solve();
        assert results.size() == 92;
    }

    private static void testCase5() {
        System.out.println("Start Task Case");
        long start = System.currentTimeMillis();
        List<Board> results = new Solver(7, 7)
                .addPieces(King, 2)
                .addPieces(Queen, 2)
                .addPieces(Bishop, 2)
                .addPieces(Knight, 1)
                .solve();
        int durationSec = (int) (System.currentTimeMillis() - start) / 1000;
        System.out.printf("Task time: %d sec. Result: %d\n", durationSec, results.size());
        assert results.size() == 3063828;
    }

    private static List<Board> perfTestCase() {
        return new Solver(6, 6)
                .addPieces(King, 2)
                .addPieces(Queen, 4)
                .addPieces(Bishop, 2)
                .addPieces(Knight, 1)
                .solve();
    }

    private static void checkSolutions(List<Board> solutions, List<String> expectedResults) {
        assert solutions.stream().map(Board::toString).sorted().reduce((acc, s) -> acc + s)
                .equals(expectedResults.stream().sorted().reduce((acc, s) -> acc + s));
    }

    private static void checkSolution2(List<Board> solutions1, List<Board> solutions2) {
        assert solutions1.stream().map(Board::toString).sorted().reduce((acc, s) -> acc + s)
                .equals(solutions2.stream().map(Board::toString).sorted().reduce((acc, s) -> acc + s));
    }

}
