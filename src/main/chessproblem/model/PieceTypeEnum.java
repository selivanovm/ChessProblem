package chessproblem.model;

public enum PieceTypeEnum {
    Bishop, King, Knight, Queen, Rook;

    public char getChar() {
        if (this != Knight) {
            return this.name().charAt(0);
        } else {
            return 'N';
        }
    }
}
