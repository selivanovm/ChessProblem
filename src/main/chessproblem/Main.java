package main.chessproblem;

import main.chessproblem.model.IPiece;
import main.chessproblem.model.pieces.Bishop;
import main.chessproblem.model.pieces.King;
import main.chessproblem.model.pieces.Rook;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        /*
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        System.out.println("YO");
        List<IPiece> example1Pieces = new ArrayList<IPiece>() {{
            add(new King()); add(new King()); add(new Rook());
        }};
        List<IPiece> pieces = new ArrayList<IPiece>() {{
            add(new Bishop()); add(new Bishop()); add(new Bishop());
        }};
        List<IPiece> sevenPieces = new ArrayList<IPiece>() {{
            add(new Bishop()); add(new Bishop()); add(new Bishop());
            add(new Bishop()); add(new Bishop()); add(new Bishop()); add(new Bishop());
        }};

        Solver solver = new Solver();
        solver.solve(new ArrayList<IPiece>() {{add (new Bishop()); add(new Bishop()); }}, 2, 2);
        //solver.solve(sevenPieces, 3, 3);
        //solver.solve(sevenPieces, 7, 7);
    }

}
