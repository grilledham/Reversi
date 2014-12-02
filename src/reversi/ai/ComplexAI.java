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
    private int nodeCount;

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
        
        isStopped=false;

        thread.start();
    }
    @Override
    public int getNumberOfMovesChecked() {
        return nodeCount;
    }

    protected void nextMoveHelper() {
        turn = gm.turnProperty().get();

        nodeCount = 0;

        majorTickCount = 0;
        minorTickCount = 0;

        Node root = new Node(null, makeFirstLWB(), null, false, initScore(0));

        nodeCount++;

        recurisveMinMax(0, root);
        
        if(isStopped){
            return;
        }

        nextMove = root.chosenMove;

        Platform.runLater(() -> {
            progressProperty().set(1);
            readyProperty().set(true);
        });
    }

    private void recurisveMinMax(int depth, Node parent) {
        
        if(isStopped){
            return;
        }

        if (depth == MAX_DEPTH) {
            scoreChild(parent, parent.parent);
            return;
        }

        List<Point> list = parent.lwb.getLegalMoves();

        startProgress(depth, list.size());

        depth++;

        if (list.isEmpty()) {
            LightWeightBoard lwb = new LightWeightBoard(col, row, parent.lwb.getTurn(), parent.lwb.getBaord());
            lwb.flipTurn();

            Node child = new Node(parent, lwb, null, true, initScore(depth));

            nodeCount++;

            if (parent.noMove) {
                scoreChild(child, parent);
            } else {
                recurisveMinMax(depth, child);
            }

            pickChild(depth, child, parent);

            countProgress(depth - 1);

        } else {

            for (Point p : list) {

                LightWeightBoard lwb = new LightWeightBoard(col, row, parent.lwb.getTurn(), parent.lwb.getBaord());
                lwb.takeTurn(p.x, p.y);

                Node child = new Node(parent, lwb, p, false, initScore(depth));

                nodeCount++;

                recurisveMinMax(depth, child);

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

    
    

    

    protected static class Node {

        final Node parent;
        final boolean noMove;
        final LightWeightBoard lwb;
        final Point move;
        Point chosenMove;
        int score;

        public Node(Node parent, LightWeightBoard lwb, Point move, boolean noMove, int score) {
            this.parent = parent;
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
