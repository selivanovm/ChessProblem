package chessproblem;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class BoardsSet {

    private final AtomicInteger nodesCount = new AtomicInteger();

    private final Node root = new Node();

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
            boolean prefBit = bitSet.get(bitNumber);
            bitSet.set(bitNumber, bitState);
            processSolutions(nodeFn, nodeLeft, bitSet, bitNumber + 1);
            bitSet.set(bitNumber, prefBit);
        }
    }

    synchronized public void addBoard(Board board) {
        Node prevNode = root;
        Node node = null;
        for (int i = 0; i < board.squattedSquares.length(); i++) {
            if (board.squattedSquares.get(i)) {
                node = prevNode.right;
            } else {
                node = prevNode.left;
            }
            if (node == null) {
                for (int j = i; j < board.squattedSquares.length(); j++) {
                    node = new Node();
                    if (board.squattedSquares.get(j)) {
                        prevNode.right = node;
                    } else {
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
            nodesCount.incrementAndGet();
        }
    }

    public int size() {
        return nodesCount.get();
    }

    public static class Node {
        Node left, right;
        List<int[]> pieces;

        public Node() {
        }

    }

    public static class SolutionInfo {
        final BitSet bitSet;
        final Node node;

        public SolutionInfo(Node node, BitSet bitSet) {
            this.node = node;
            this.bitSet = bitSet;
        }
    }
}
