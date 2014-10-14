package chessproblem;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum PieceTypeEnum {
    // Order is important, in that order solver puts pieces on the board.
    // Pieces with higher number of guarding possibilities(like Queen, Rook, Bishop) should be put
    // in the first place, that decreases iterations count.
    Queen, Rook, Bishop, Knight, King;

    public char getChar() {
        if (this != Knight) {
            return this.name().charAt(0);
        } else {
            return 'N';
        }
    }

    public boolean isGuardLines() {
        return this == Queen || this == Rook;
    }

    public static Optional<PieceTypeEnum> getByChar(char c) {
        return Arrays.asList(PieceTypeEnum.values()).stream().filter((pieceType) -> pieceType.getChar() == c).findFirst();
    }
}
