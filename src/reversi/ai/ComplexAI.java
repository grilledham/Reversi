/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.ai;

import java.awt.Point;
import java.util.List;
import javafx.application.Platform;
import reversi.model.BoardModel;
import reversi.model.GameModel;
import reversi.model.Owner;

/**
 *
 * @author James
 */
public class ComplexAI extends AI {

    protected static final int WIN_VALUE = 1000000;
    protected static final int LOSE_VALUE = -1000000;
    protected static final int MAX_DEPTH = 9;    

    protected final int col;
    protected final int row;
    protected Owner turn;

    private Point nextMove;
    private Point suggestMove;

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
        if (suggestMove == null) {
            List<Point> list = gm.getBoard().getLegalMoves();
            if (list.isEmpty()) {
                return null;
            } else {
                return list.get(0);
            }
        } else {
            return suggestMove;
        }
    }

    @Override
    public void requestNextMove() {
        readyProperty().set(false);
        progressProperty().set(0);

        Thread thread = new Thread(() -> nextMoveHelper());
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.setDaemon(true);

        isStopped = false;

        thread.start();
    }

    protected void nextMoveHelper() {
        long start = System.currentTimeMillis();
        //nodeCount = 0;

        turn = gm.turnProperty().get();

        majorTickCount = 0;
        minorTickCount = 0;

        Node root = new Node(null, gm.getBoard(), null, false, Integer.MIN_VALUE, Integer.MAX_VALUE);

        recurisveMinMax(0, root);

        if (isStopped) {
            return;
        }

        nextMove = root.chosenChild.move;
        suggestMove = root.chosenChild.chosenChild.move;

        long end = System.currentTimeMillis();

        System.out.println("Time: " + (end - start));

        Platform.runLater(() -> {
            progressProperty().set(1);
            readyProperty().set(true);
        });
    }

    private void recurisveMinMax(int depth, Node parent) {

        if (isStopped) {
            return;
        }

        if (depth == MAX_DEPTH) {
            scoreChild(depth, parent, parent.parent);
            return;
        }

        List<Point> list = parent.bm.getLegalMoves();

        startProgress(depth, list.size());

        depth++;

        if (list.isEmpty()) {

            BoardModel bm = new BoardModel(parent.bm);
            bm.flipTurn();

            Node child = new Node(parent, bm, null, true, parent.alpha, parent.beta);

            if (parent.noMove) {
                scoreChild(depth, child, parent);
            } else {
                recurisveMinMax(depth, child);
            }

            pickChild(depth, child, parent);

            countProgress(depth - 1);

        } else {

            for (Point p : list) {

                BoardModel bm = new BoardModel(parent.bm);
                bm.takeTurn(p.x, p.y);

                Node child = new Node(parent, bm, p, false, parent.alpha, parent.beta);

                recurisveMinMax(depth, child);

                pickChild(depth, child, parent);

                countProgress(depth - 1);

                if (parent.alpha >= parent.beta) {                    
                    break;
                }
                
            }
        }
    }

    protected void scoreChild(int depth, Node child, Node parent) {

        int score = child.bm.calculateScoreDifference(turn);

        if (child.noMove && parent.noMove) {
            if (score > 0) {
                score += WIN_VALUE;
            } else {
                score += LOSE_VALUE;
            }
        }

        if (depth % 2 == 0) {
            child.alpha = score;
        } else {
            child.beta = score;
        }
    }

    private void pickChild(int depth, Node child, Node parent) {

        if (depth % 2 == 0) {
            if (child.alpha < parent.beta) {
                parent.beta = child.alpha;
                parent.chosenChild = child;
            }
        } else {
            if (child.beta > parent.alpha) {
                parent.alpha = child.beta;
                parent.chosenChild = child;
            }
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

    protected static class Node {

        final Node parent;
        final boolean noMove;
        final BoardModel bm;
        final Point move;
        Node chosenChild;
        int alpha, beta;

        public Node(Node parent, BoardModel bm, Point move, boolean noMove, int alpha, int beta) {
            this.parent = parent;
            this.bm = bm;
            this.move = move;
            this.noMove = noMove;
            this.alpha = alpha;
            this.beta = beta;            
        }
    }
}
