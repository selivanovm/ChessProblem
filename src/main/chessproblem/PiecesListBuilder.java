package chessproblem;

import chessproblem.model.GuardedSquaresBuffer;
import chessproblem.model.IPiece;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class PiecesListBuilder {
    private final List<IPiece> pieces = new LinkedList<>();
    private final int boardWidth;
    private final int boardHeight;

    public PiecesListBuilder(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
    }

    PiecesListBuilder addPieces(Class pieceType, int amount) {
        checkForRepeatedPieces(pieceType);
        try {
            for (int i = 0; i < amount; i++) {
                pieces.add((IPiece) (pieceType.getConstructor(GuardedSquaresBuffer.class)
                        .newInstance(new GuardedSquaresBuffer(this.boardWidth, this.boardHeight))));
            }
        } catch (Exception e) {
            throw new RuntimeException("Can't create piece of type " + pieceType.getName(), e);
        }
        return this;
    }

    List<IPiece> build() {
        return Collections.unmodifiableList(
                this.pieces.stream().sorted((p1, p2) -> p2.getCheckPriority() - p1.getCheckPriority()).collect(Collectors.toList())
        );
    }
    private void checkForRepeatedPieces(Class<IPiece> pieceType) {
        Stream<IPiece> existentPiecesOfSameType = pieces.stream().filter((p -> p.getClass() == pieceType));
        if (existentPiecesOfSameType.iterator().hasNext()) {
            throw new RuntimeException("Pieces of type '" + pieceType.getName() + "' are already added.");
        }
    }

}
