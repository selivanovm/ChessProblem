package chessproblem;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.function.Consumer;

public class BoardsSet {

    private int nodesCount = 0;

    private Node root = new Node();

    public void processSolutions(Consumer<SolutionInfo> nodeFn) {
        processSolutions(nodeFn, root, new BitSet(), 0);
    }

    private void processSolutions(Consumer<SolutionInfo> nodeFn, Node node, BitSet bitSet, int bitNumber) {
        nodeFn.accept(new SolutionInfo(node, bitSet));
        processNode(nodeFn, bitSet, bitNumber, node.left, false);
        processNode(nodeFn, bitSet, bitNumber, node.right, true);
    }

    private void processNode(Consumer<SolutionInfo> nodeFn, BitSet bitSet, int bitNumber, Node nodeLeft, boolean bitState) {
        if (nodeLeft != null) {
            //System.out.println("LEFT " + bitNumber);
            boolean prefBit = bitSet.get(bitNumber);
            bitSet.set(bitNumber, bitState);
            processSolutions(nodeFn, nodeLeft, bitSet, bitNumber + 1);
            bitSet.set(bitNumber, prefBit);
        }
    }

    synchronized public void addBoard(Board board) {
        //System.out.println("NEW BOARD | " + board.squattedSquares.length() + "\n" + board.getStringRepresentation());
        Node prevNode = root;
        Node node = null;
        for (int i = 0; i < board.squattedSquares.length(); i++) {
            if (board.squattedSquares.get(i)) {
                //System.out.println("SET RIGHT " + i);
                node = prevNode.right;
            } else {
                //System.out.println("SET LEFT " + i);
                node = prevNode.left;
            }
            if (node == null) {
                for (int j = i; j < board.squattedSquares.length(); j++) {
                    node = new Node();
                    if (board.squattedSquares.get(j)) {
                        //System.out.println("SET RIGHT " + j);
                        prevNode.right = node;
                    } else {
                        //System.out.println("SET LEFT " + j);
                        prevNode.left = node;
                    }
                    prevNode = node;
                }
                break;
            }
            prevNode = node;
        }
        List<int[]> pieces = node.pieces;
        if (pieces == null) {
            pieces = new ArrayList<>();
            node.pieces = pieces;
        }
        int idx = pieces.indexOf(board.pieces);
        if (idx < 0) {
            node.pieces.add(board.pieces.clone());
            nodesCount++;
        }
    }

    public int size() {
        return nodesCount;
    }

    public static class Node {
        Node left, right;
        List<int[]> pieces;

        public Node() {
        }

        public Node(Node left, Node right, List<int[]> pieces) {
            this.left = left;
            this.right = right;
            this.pieces = pieces;
        }
    }

    public static class SolutionInfo {
        BitSet bitSet;
        Node node;

        public SolutionInfo(Node node, BitSet bitSet) {
            this.node = node;
            this.bitSet = bitSet;
        }
    }
}
