package main.chessproblem;

import main.chessproblem.model.Board;
import main.chessproblem.model.IPiece;
import main.chessproblem.model.pieces.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Solver {

    public void solve(List<IPiece> pieces, int width, int height) {
        System.out.println("Complexity is : " + (pieces.size() * Math.pow(width * height, pieces.size())));
        Board board = new Board(width, height);

        List<Class> piecesClasses = new LinkedList<Class>() {{ add(Bishop.class); add(King.class); add(Knight.class); add(Queen.class); add(Rook.class); }};
        List<IPiece> startPieces = new LinkedList<>();
        for(Class pc : piecesClasses) {
            IPiece p = takePieceOfType(pieces, pc);
            if (p != null) {
                startPieces.add(p);
            }
        }


        List<Board> results = new LinkedList<>();
        int result = 0;
        for (IPiece p : startPieces) {
            Queue<IPiece> pieceQueue = new LinkedBlockingQueue<>();
            pieceQueue.addAll(pieces);
            pieceQueue.add(p);
            System.out.println("PiecesNumber : " + pieces.size());
            System.out.println("Starting with : " + p.toString());
            result += loop(width, height, board, pieceQueue, 0, results);
        }
        System.out.println("Loop count is : " + result);
        System.out.println("Solution size: " + results.size());

        for(Board b : results) {
            b.print();
            System.out.println("=========");
        }
    }

    private int loop(int width, int height, Board board, Queue<IPiece> pieceQueue, int counter, List<Board> results) {
        while (!pieceQueue.isEmpty()) {
            IPiece piece = pieceQueue.poll();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    Board newBoard = board.putPiece(piece, x, y);
                    if (newBoard != null) {
                        if (pieceQueue.isEmpty()) {
                            results.add(newBoard);
                        } else {
                            Queue<IPiece> newPieceQueue = new LinkedBlockingQueue<>();
                            newPieceQueue.addAll(pieceQueue);
                            counter = loop(width, height, newBoard, newPieceQueue, counter, results);
                        }
                    }
                }
            }
        }
        if (counter % 1000_000 == 0) {
            System.out.println(counter);
        }
        return counter + 1;
    }

    private IPiece takePieceOfType(List<IPiece> pieces, Class<IPiece> pieceClass) {
        IPiece result = null;
        for(IPiece p : pieces) {
            if (p.getClass() == pieceClass) {
                result = p;
                break;
            }
        }
        if (result != null) {
            pieces.remove(result);
        }
        return result;
    }
}
