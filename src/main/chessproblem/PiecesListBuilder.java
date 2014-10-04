package chessproblem;

import chessproblem.model.IPiece;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

class PiecesListBuilder {
    private final List<IPiece> pieces = new LinkedList<>();

    PiecesListBuilder addPieces(Class pieceType, int amount) {
        checkForRepeatedPieces(pieceType);
        try {
            for (int i = 0; i < amount; i++) {
                pieces.add((IPiece) pieceType.newInstance());
            }
        } catch (Exception e) {
            throw new RuntimeException("Can't create piece of type " + pieceType.getName(), e);
        }
        return this;
    }

    List<IPiece> build() {
        return Collections.unmodifiableList(this.pieces);
    }
    private void checkForRepeatedPieces(Class<IPiece> pieceType) {
        Stream<IPiece> existentPiecesOfSameType = pieces.stream().filter((p -> p.getClass() == pieceType));
        if (existentPiecesOfSameType.iterator().hasNext()) {
            throw new RuntimeException("Pieces of type '" + pieceType.getName() + "' are already added.");
        }
    }

}
