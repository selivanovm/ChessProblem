package chessproblem;

import java.util.*;
import java.util.function.Supplier;

import static chessproblem.PieceTypeEnum.*;

public class Main {

    public static void main(String[] args) {
        runTests();
    }

    private static void runTests() {
        testCase1();
        testCase2();
        testCase3();
        testCase4();

        for (int i = 0; i < 100; i++) {
            testCase5();
        }

        testCase6();
    }

    private static void testCase1() {
        runCase("Test #1", false, () -> {
            System.out.println("Start Test #1");

            Result results = new Solver(3, 3)
                    .addPieces(King, 2)
                    .addPieces(Rook, 1)
                    .solve();

            List<String> expectedResults = new LinkedList<>();
            expectedResults.add("K . K \n. . . \n. R . \n");
            expectedResults.add("K . . \n. . R \nK . . \n");
            expectedResults.add(". R . \n. . . \nK . K \n");
            expectedResults.add(". . K \nR . . \n. . K \n");

            printResults(getBoardsRepresentation(results.solutions, 3, 3));

            checkSolutions(results.solutions, 3, 3, expectedResults);
            return results;
        }, 3, 3);
    }

    private static void testCase2() {
        runCase("Test #2", false, () -> {
            Result results = new Solver(4, 4)
                    .addPieces(Knight, 4)
                    .addPieces(Rook, 2).solve();

            List<String> expectedResult = new LinkedList<>();
            expectedResult.add("R . . . \n. N . N \n. . R . \n. N . N \n");
            expectedResult.add("N . N . \n. R . . \nN . N . \n. . . R \n");
            expectedResult.add("N . N . \n. . . R \nN . N . \n. R . . \n");
            expectedResult.add(". R . . \nN . N . \n. . . R \nN . N . \n");
            expectedResult.add(". . R . \n. N . N \nR . . . \n. N . N \n");
            expectedResult.add(". . . R \nN . N . \n. R . . \nN . N . \n");
            expectedResult.add(". N . N \nR . . . \n. N . N \n. . R . \n");
            expectedResult.add(". N . N \n. . R . \n. N . N \nR . . . \n");

            checkSolutions(results.solutions, 4, 4, expectedResult);
            return results;
        }, 4, 4);
    }

    private static void testCase3() {
        runCase("Test #3", false, () -> {
            System.out.println("Start Test #3");

            List<String> expectedResult = new LinkedList<>();
            expectedResult.add("N . . . \n. . . Q \n. . . . \n. . Q . \n");
            expectedResult.add("N . . . \n. . . . \n. . . Q \n. Q . . \n");
            expectedResult.add(". Q . . \n. . . Q \n. . . . \nN . . . \n");
            expectedResult.add(". Q . . \n. . . . \nQ . . . \n. . . N \n");
            expectedResult.add(". . Q . \nQ . . . \n. . . . \n. . . N \n");
            expectedResult.add(". . Q . \n. . . . \n. . . Q \nN . . . \n");
            expectedResult.add(". . . N \nQ . . . \n. . . . \n. Q . . \n");
            expectedResult.add(". . . N \n. . . . \nQ . . . \n. . Q . \n");

            Result result = new Solver(4, 4)
                    .addPieces(Queen, 2)
                    .addPieces(Knight, 1).solve();
            checkSolutions(result.solutions, 4, 4, expectedResult);

            return result;
        }, 4, 4);
    }

    private static void testCase4() {
        runCase("Test #4. 8 Queens", false, () -> {
            Result results = new Solver(8, 8).addPieces(Queen, 8).solve();
            assert results.solutions.size() == 92;
            return results;
        }, 8, 8);
    }

    private static void testCase5() {
        runCase("Test #5", false, () -> {
            Result results = new Solver(7, 7)
                    .addPieces(King, 2)
                    .addPieces(Queen, 2)
                    .addPieces(Bishop, 2)
                    .addPieces(Knight, 1)
                    .solve();
            assert results.solutions.size() == 3063828;
            return results;
        }, 7, 7);
    }

    private static Result testCase6() {
        return runCase("Test #6", false, () -> {
            Result results = new Solver(8, 8)
                    .addPieces(King, 2)
                    .addPieces(Queen, 3)
                    .addPieces(Bishop, 4)
                    .addPieces(Knight, 1)
                    .solve();
            assert results.solutions.size() == 8112968;
            return results;
        }, 8, 8);
    }

    private static void checkSolutions(BoardsSet solutions, int width, int height, List<String> expectedResults) {
        List<String> boardsList = getBoardsRepresentation(solutions, width, height);
        assert boardsList.stream().sorted().reduce((acc, s) -> acc + s)
                .equals(expectedResults.stream().sorted().reduce((acc, s) -> acc + s));
    }

    private static List<String> getBoardsRepresentation(BoardsSet solutions, int width, int height) {
        List<String> boardsList = new ArrayList<>();
        List<Integer> piecesList = new ArrayList<>();
        solutions.processSolutions((n) -> {
            if (n.node.pieces != null) {
                System.out.println("!!! " + n.bitSet.toString());
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
                                //System.out.println(">>> pos = " + pos + " >> " + Arrays.asList(pieces));
                                PieceTypeEnum pieceType = PieceTypeEnum.values()[Util.getSecondShortFromInt(piecesList.get(pieceNum++))];
                                sb.append(pieceType.getChar()).append(' ');
                            } else {
                                sb.append(". ");
                            }
                        }
                        sb.append("\n");
                    }
                    boardsList.add(sb.toString());
                }
            }
        });
        return boardsList;
    }

    private static Result runCase(String caseName, boolean showResult, Supplier<Result> run, int width, int height) {
        System.out.println(caseName);
        long start = System.currentTimeMillis();
        Result result = run.get();
        int durationSec = (int) (System.currentTimeMillis() - start) / 1000;
        System.out.printf("Finished '%s'. Results count: %d, Time: %d sec. Loops count: %d\n",
                caseName, result.solutions.size(), durationSec, result.loopsCount);

        if (showResult) {
            printResults(getBoardsRepresentation(result.solutions, width, height));
        }
        return result;
    }

    private static void printResults(List<String> results) {
        System.out.println("Results Count = " + results.size());
        for (String b : results) {
            System.out.print(b);
            System.out.println("===");
        }
    }

}
