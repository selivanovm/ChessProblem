package chessproblem;

import java.util.List;

public class Result {
    public final List<Board> boards;
    public final int loopsCount;

    public Result(List<Board> boards, int loopsCount) {
        this.boards = boards;
        this.loopsCount = loopsCount;
    }
}
