package chessproblem;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import java.util.Optional;

public class Main {

    public static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static final int TERMINAL_WIDTH = 120;

    public static void main(String[] args) {
        Options options = getOptions();
        CommandLineParser parser = new BasicParser();
        try {
            CommandLine commandLine = parser.parse(options, args);
            boolean allOptionsDefined = commandLine.hasOption("bw") && commandLine.hasOption("bh") && commandLine.hasOption("ps");
            if (allOptionsDefined) {
                Integer boardWidth = Util.parseInt(commandLine.getOptionValue("bw"));
                Integer boardHeight = Util.parseInt(commandLine.getOptionValue("bh"));

                String piecesSet = commandLine.getOptionValue("ps");
                String[] pieceDefinitionStrings = piecesSet.split("-");

                Solver solver = new Solver(boardWidth, boardHeight);
                initSolver(pieceDefinitionStrings, solver);
                try {
                    solver.solve();
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                    printUsage(options);
                }
            } else {
                printUsage(options);
            }
        } catch (ParseException e) {
            logger.error("Unable to parse options", e);
        }
    }

    private static Options getOptions() {
        Options options = new Options();
        options.addOption("bw", "board-width", true, "Board width: number in range 1-255");
        options.addOption("bh", "board-height", true, "Board height: number in range 1-255");
        options.addOption("ps", "pieces-set", true,
                "Pieces set in the following format: \"K1-Q1-R1-B2-N3\". " +
                        "Where character signifies the piece type and number is a number of pieces of this type. " +
                        "In the example we have: K1 - one king, Q1 - one queen, R1 - one rook, B2 - two bishops and N3 - three knights.");
        return options;
    }

    private static void initSolver(String[] pieceDefinitionStrings, Solver solver) {
        Arrays.asList(pieceDefinitionStrings).stream().forEach((pieceDef) -> {
            Optional<PieceTypeEnum> pieceTypeOptional = PieceTypeEnum.getByChar(pieceDef.charAt(0));
            if (pieceTypeOptional.isPresent()) {
                PieceTypeEnum pieceType = pieceTypeOptional.get();
                Integer pieceCount = Util.parseInt(pieceDef.substring(1));
                if (pieceCount == null) {
                    parameterValidationError("Invalid piece definition format: " + pieceDef);
                } else {
                    solver.addPieces(pieceType, pieceCount);
                }
            } else {
                parameterValidationError("Invalid piece definition format: " + pieceDef);
            }
        });
    }

    private static void parameterValidationError(String s) {
        logger.error(s);
        System.out.println(s);
        System.exit(1);
    }

    private static void printUsage(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(TERMINAL_WIDTH);
        formatter.printHelp("chess-problem", "Solves multiple unguard arrangements problem.", options, "", true);
    }

}
