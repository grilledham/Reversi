/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.ai;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import javafx.application.Platform;
import reversi.model.GameModel;
import reversi.model.Owner;

/**
 *
 * @author James
 */
public class ComplexAI extends AI {

    protected static final int WIN_VALUE = 1000000;
    protected static final int LOSE_VALUE = -1000000;

    protected static final int MAX_DEPTH = 7;
    //private static final int MAX_NODES = 3000000;

//    private int maxguaranteedDepth = 0;
    protected final int col;
    protected final int row;
    protected Owner turn;
//    private Node root;
    private Point nextMove;

//    private long lastSleep;
//    private Queue<Node> queue;
//    private Queue<Node> bottomNodes;
//    private Queue<Node> parents;
    private StackNode[] nodePool;
    private int stackNodeCount;

    private int maxMajorTick;
    private int majorTickCount;
    private int maxMinorTick;
    private int minorTickCount;

    private long then;
    private long now;

    public ComplexAI(GameModel gm) {
        super(gm);

        col = gm.getColumns();
        row = gm.getRows();

//        queue = new LinkedBlockingQueue<>();
//        bottomNodes = new LinkedBlockingQueue<>();
//        parents = new LinkedBlockingQueue<>();
    }

    /**
     * Must call {@code requestNextMove()} first
     *
     * @return the AI's next move
     */
    @Override
    public Point getMove() {
        return nextMove;
    }

    @Override
    public Point suggestMove() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void requestNextMove() {
        readyProperty().set(false);
        progressProperty().set(0);

        Thread thread = new Thread(() -> stackHelper());//-> nextMoveHelper());
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.setDaemon(true);

        thread.start();
    }

    protected void stackHelper() {
        turn = gm.turnProperty().get();
        stackNodeCount = 0;

        majorTickCount = 0;
        minorTickCount = 0;

        nodePool = new StackNode[MAX_DEPTH + 1];

        StackNode root = new StackNode(makeFirstLWB(), null, false, initScore(0));
        nodePool[0] = root;

        stackNodeCount++;

        System.out.println("starting");

        recurisveMinMax(0);

        nextMove = root.chosenMove;
        System.out.println("next move: " + nextMove + ", score: " + root.score);

        System.out.println("number of move(s) considered: " + stackNodeCount);

        nodePool = null;
        root = null;
        System.gc();

        System.out.println("done");

        Platform.runLater(() -> {
            progressProperty().set(1);
            readyProperty().set(true);
        });
    }

    private void recurisveMinMax(int depth) {

        if (depth == MAX_DEPTH) {

            scoreChild(nodePool[depth], nodePool[depth - 1]);
            return;
        }

        StackNode parent = nodePool[depth];
        List<Point> list = parent.lwb.getLegalMoves();

        startProgress(depth, list.size());

        depth++;

        if (list.isEmpty()) {
            LightWeightBoard lwb = new LightWeightBoard(col, row, parent.lwb.getTurn(), parent.lwb.getBaord());
            lwb.flipTurn();

            StackNode child = new StackNode(lwb, null, true, initScore(depth));
            nodePool[depth] = child;

            stackNodeCount++;

            if (parent.noMove) {
                scoreChild(child, parent);
            } else {
                recurisveMinMax(depth);
            }

            pickChild(depth, child, parent);
            countProgress(depth - 1);

        } else {

            for (Point p : list) {

                LightWeightBoard lwb = new LightWeightBoard(col, row, parent.lwb.getTurn(), parent.lwb.getBaord());
                lwb.takeTurn(p.x, p.y);

                StackNode child = new StackNode(lwb, p, false, initScore(depth));
                nodePool[depth] = child;

                stackNodeCount++;

                recurisveMinMax(depth);

                pickChild(depth, child, parent);

                countProgress(depth - 1);
            }
        }
    }

    protected void scoreChild(StackNode child, StackNode parent) {

        child.score = child.lwb.calculateScore(turn);

        if (child.noMove && parent.noMove) {
            if (child.score > 0) {
                child.score += WIN_VALUE;
            } else {
                child.score += LOSE_VALUE;
            }
        }
    }

    private void pickChild(int depth, StackNode child, StackNode parent) {
        if (depth % 2 == 0) {
            if (child.score < parent.score) {
                parent.score = child.score;
                parent.chosenMove = child.move;
            }
        } else {
            if (child.score > parent.score) {
                parent.score = child.score;
                parent.chosenMove = child.move;
            }
        }
    }

    private int initScore(int depth) {
        if (depth % 2 == 0) {
            return Integer.MIN_VALUE;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    private void startProgress(int depth, int size) {
        if (depth == 0) {
            maxMajorTick = size;
            majorTickCount = 0;
        } else if (depth == 1) {
            if (size == 0) {
                maxMinorTick = 1;
            } else {
                maxMinorTick = size;
            }
            minorTickCount = 0;
        }
    }

    private void countProgress(int depth) {
        if (depth == 0) {
            majorTickCount++;
        } else if (depth == 1) {
            minorTickCount++;
        }

        updateProgress();
    }

    private void updateProgress() {

        //prevents flooding the gui thread with update requests
        now = System.currentTimeMillis();
        if (now - then > 20) {
            then = now;

            double value = (majorTickCount + (minorTickCount / (double) maxMinorTick)) / (double) maxMajorTick;

            Platform.runLater(() -> {
                progressProperty().set(value);
            });
        }
    }
    
//    private void nextMoveHelper() {
//        turn = gm.turnProperty().get();
//
//        System.out.println("building");
//
//        buildTree();
//
//        System.out.println("scoreing");
//
//        scoreBottomNodes();
//
//        System.out.println("minMaxing");
//
//        minMaxTree();
//
//        System.out.println("clearing up");
//
//        root = null;
//        queue = new LinkedBlockingQueue<>();
//        bottomNodes = new LinkedBlockingQueue<>();
//        parents = new LinkedBlockingQueue<>();
//        System.gc();
//
//        System.out.println("maxguaranteedDepth: " + maxguaranteedDepth);
//        System.out.println("done");
//
//        Platform.runLater(() -> {
//            ready.set(true);
//        });
//    }
//
//    private void delay() {
//        long now = System.currentTimeMillis();
//
//        if (now - lastSleep > 16) {
//            lastSleep = now;
//            Thread.yield();
//        }
//    }
//
//    private void buildTree() {
//        int nodeCount = 0;
//        int depth = 0;
//        int noMoveCount = 0;
//
//        root = new Node();
//        root.depth = 0;
//        root.lwb = makeFirstLWB();
//        root.id = nodeCount++;
//
//        queue.add(root);
//
//        do {
//            Node parent = queue.poll();
//            depth = parent.depth + 1;
//            //System.out.println("depth: "+depth+", NodeCount: "+ ++nodeCount);
//
//            if (depth > MAX_DEPTH) {
//                //System.out.println("bottom: " + parent);
//
//                bottomNodes.add(parent);
//                continue;
////                while (!queue.isEmpty()) {
////                    parent = queue.poll();
////                    bottomNodes.add(parent);
////                }
////                return;
//            }
//            List< Point> list = parent.lwb.getLegalMoves();
//
//            if (list.isEmpty()) {
//                noMoveCount++;
//                //need logic for no moves
//                LightWeightBoard lwb = new LightWeightBoard(col, row, parent.lwb.getTurn(), parent.lwb.getBaord());
//                lwb.flipTurn();
//                Node child = new Node(lwb, parent, depth, null);
//                child.id = nodeCount++;
//                child.noMove = true;
//                if (parent.children == null) {
//                    parent.children = new LinkedList<>();
//                }
//                parent.children.add(child);
//
////                if (parent.noMove) {
////                    bottomNodes.add(child);
////                } else {}
//                queue.add(child);
//
//                continue;
//            }
//
//            for (Point p : list) {
//
//                LightWeightBoard lwb = new LightWeightBoard(col, row, parent.lwb.getTurn(), parent.lwb.getBaord());
//                lwb.takeTurn(p.x, p.y);
//                Node child = new Node(lwb, parent, depth, p);
//                child.id = nodeCount++;
//                if (parent.children == null) {
//                    parent.children = new LinkedList<>();
//                }
//                parent.children.add(child);
//                queue.add(child);
//                //System.out.println(child);
//            }
//            parent.lwb = null;
//
//            // delay();
//        } while (!queue.isEmpty());//and all thread finished
//
//        maxguaranteedDepth = depth - 1;
//
//        System.out.println(
//                "noMoveCount: " + noMoveCount);
//    }
//
//    private void scoreBottomNodes() {
//        while (!bottomNodes.isEmpty()) {
//            Node node = bottomNodes.poll();
//            node.score = node.lwb.calculateScore(turn);
//
//            if (node.noMove && node.parent.noMove) {
//                if (node.score > 0) {
//                    node.score = 1000;
//                } else if (node.score < 0) {
//                    node.score = -1000;
//                }
//            }
//
//            if (node.parent.added == false) {
//                node.parent.added = true;
//                parents.add(node.parent);
//                //System.out.println("parent: " + node.parent);
//            }
//            //System.out.println(node);
//        }
//    }
//
//    private void minMaxTree() {
//        while (!parents.isEmpty()) {
//            Node node;
//            Node parent = parents.poll();
//
//            if (parent.depth % 2 == 0) {
//                node = getMaxChild(parent);
//            } else {
//                node = getMinChild(parent);
//            }
//            parent.score = node.score;
//
////            System.out.println("parent: " + parent);
////            for (Node n : parent.children) {
////                System.out.println(n);
////            }
//            if (parent.parent != null && parent.parent.added == false) {
//                parent.parent.added = true;
//                parents.add(parent.parent);
//            }
//            if (parent.parent == null) {
//                nextMove = node.move;
//                System.out.println("nextMove: " + nextMove);
////                System.out.println("parent: " + parent);
////                for (Node n : parent.children) {
////                    System.out.println(n);
////                }
//            }
//        }
//
//    }
//
//    private Node getMaxChild(Node parent) {
//        Node maxNode = parent.children.get(0);
//
//        for (int i = 1; i < parent.children.size(); i++) {
//            Node n = parent.children.get(i);
//            if (n.score > maxNode.score) {
//                maxNode = n;
//            }
//        }
//
//        return maxNode;
//    }
//
//    private Node getMinChild(Node parent) {
//        Node minNode = parent.children.get(0);
//
//        for (int i = 1; i < parent.children.size(); i++) {
//            Node n = parent.children.get(i);
//            if (n.score < minNode.score) {
//                minNode = n;
//            }
//        }
//
//        return minNode;
//    }

    private LightWeightBoard makeFirstLWB() {

        Owner[][] board = new Owner[col][row];

        for (int x = 0; x < col; x++) {
            for (int y = 0; y < row; y++) {
                board[x][y] = gm.boardProperty()[x][y].get();
            }
        }

        return new LightWeightBoard(col, row, gm.turnProperty().get(), board);

    }

//    private static class Node {
//
//        boolean noMove;
//        int id;
//        LightWeightBoard lwb;
//        boolean added;
//        int score;
//        int depth;
//        Node parent;
//        List<Node> children;// = new LinkedList<>();
//        Point move;
//
//        public Node() {
//        }
//
//        public Node(LightWeightBoard lwb, Node parent, int depth, Point move) {
//            this.lwb = lwb;
//            this.parent = parent;
//            this.depth = depth;
//            this.move = move;
//        }
//
//        @Override
//        public String toString() {
//            String m;
//
//            if (move == null) {
//                m = ", move: null";
//            } else {
//                m = ", move: (" + move.x + "," + move.y + ")";
//            }
//
//            return "depth: " + depth + ", added: " + added + ", score: " + score + m + ", noMove: " + noMove + ", id: " + id;
//        }
//    }
    protected static class StackNode {

        final boolean noMove;
        //int id;
        final LightWeightBoard lwb;
        //StackNode parent;
        final Point move;
        Point chosenMove;
        int score;
        //int depth;        

        public StackNode(LightWeightBoard lwb, Point move, boolean noMove, int score) {
            this.lwb = lwb;
            this.move = move;
            this.noMove = noMove;
            this.score = score;
        }

        @Override
        public String toString() {
            String m;

            if (move == null) {
                m = ", move: null";
            } else {
                m = ", move: (" + move.x + "," + move.y + ")";
            }

            String cm;

            if (chosenMove == null) {
                cm = ", chosenMove: null";
            } else {
                cm = ", chosenMove: (" + chosenMove.x + "," + chosenMove.y + ")";
            }

            //return "depth: " + depth + ", score: " + score + m + ", noMove: " + noMove + cm + ", id: " + id;
            return "score: " + score + m + ", noMove: " + noMove + cm;
        }

    }
}
