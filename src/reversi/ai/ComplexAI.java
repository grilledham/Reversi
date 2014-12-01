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

    protected final int col;
    protected final int row;
    protected Owner turn;

    private Point nextMove;
    private Node[] nodePool;
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

        Thread thread = new Thread(() -> nextMoveHelper());
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.setDaemon(true);

        thread.start();
    }

    protected void nextMoveHelper() {
        turn = gm.turnProperty().get();
        stackNodeCount = 0;

        majorTickCount = 0;
        minorTickCount = 0;

        nodePool = new Node[MAX_DEPTH + 1];

        Node root = new Node(makeFirstLWB(), null, false, initScore(0));
        nodePool[0] = root;

        stackNodeCount++;

        //System.out.println("starting");
        recurisveMinMax(0);

        nextMove = root.chosenMove;
        //System.out.println("next move: " + nextMove + ", score: " + root.score);

        //System.out.println("number of move(s) considered: " + stackNodeCount);
        nodePool = null;
        root = null;
        System.gc();

        //System.out.println("done");
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

        Node parent = nodePool[depth];
        List<Point> list = parent.lwb.getLegalMoves();

        startProgress(depth, list.size());

        depth++;

        if (list.isEmpty()) {
            LightWeightBoard lwb = new LightWeightBoard(col, row, parent.lwb.getTurn(), parent.lwb.getBaord());
            lwb.flipTurn();

            Node child = new Node(lwb, null, true, initScore(depth));
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

                Node child = new Node(lwb, p, false, initScore(depth));
                nodePool[depth] = child;

                stackNodeCount++;

                recurisveMinMax(depth);

                pickChild(depth, child, parent);

                countProgress(depth - 1);
            }
        }
    }

    protected void scoreChild(Node child, Node parent) {

        child.score = child.lwb.calculateScore(turn);

        if (child.noMove && parent.noMove) {
            if (child.score > 0) {
                child.score += WIN_VALUE;
            } else {
                child.score += LOSE_VALUE;
            }
        }
    }

    private void pickChild(int depth, Node child, Node parent) {
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

    private LightWeightBoard makeFirstLWB() {

        Owner[][] board = new Owner[col][row];

        for (int x = 0; x < col; x++) {
            for (int y = 0; y < row; y++) {
                board[x][y] = gm.boardProperty()[x][y].get();
            }
        }

        return new LightWeightBoard(col, row, gm.turnProperty().get(), board);
    }

    @Override
    public int getNumberOfMovesChecked() {
        return stackNodeCount;
    }

    protected static class Node {

        final boolean noMove;        
        final LightWeightBoard lwb;        
        final Point move;
        Point chosenMove;
        int score;              

        public Node(LightWeightBoard lwb, Point move, boolean noMove, int score) {
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
            
            return "score: " + score + m + ", noMove: " + noMove + cm;
        }
    }
}
