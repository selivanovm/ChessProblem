package chessproblem.model;

public class AttackedSquare implements ISquareState {
    public static final AttackedSquare INSTANCE = new AttackedSquare();

    @Override
    public String toString() {
        return "x";
    }

}
