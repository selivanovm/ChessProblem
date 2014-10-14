package chessproblem;

public class Result {
    public final BoardsSet solutions;
    public final int loopsCount;

    public Result(BoardsSet solutions, int loopsCount) {
        this.solutions = solutions;
        this.loopsCount = loopsCount;
    }
}
