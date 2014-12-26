/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.ai;

import reversi.model.BitBoard;
import java.awt.Point;
import java.util.List;
import javafx.application.Platform;
import reversi.model.GameModel;
import reversi.model.Owner;

/**
 *
 * @author James
 */
public class MinMaxAI extends AI {

    protected static final int WIN_VALUE = 1000000;
    protected static final int LOSE_VALUE = -1000000;

    protected final int col;
    protected final int row;

    private int targetDepth = 11;
    private int lastDepthThreshold = 4;
    private int goToDepth;

    private long[] searchOrder;

    private static final int DEFUALT_PROGRESS_DEPTH = 8;
    private int[] maxProgressTick, progressTick;
    private int progressDepth = DEFUALT_PROGRESS_DEPTH;
    private int lastDepth = 0;

    private long then;
    private long now;

    private TranspositionTable tt;
    private int hitCount;

    public MinMaxAI(GameModel gm, int color) {
        super(gm, color);

        col = gm.getColumns();
        row = gm.getRows();

        tt = new TranspositionTable(1 << 21);
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
            List<Point> list = BitBoard.splitToPoints(gm.getBoard().getLegalMove(playerColor));
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

        tt.clear();
        hitCount = 0;

        if (gm.getRemainingTurns() - targetDepth <= lastDepthThreshold) {
            goToDepth = targetDepth + lastDepthThreshold + 1;
        } else {
            goToDepth = targetDepth;
        }
        System.out.println("gotoDepth: " + goToDepth);

        maxProgressTick = new int[goToDepth - progressDepth + 1];
        progressTick = new int[goToDepth - progressDepth + 1];

        BitBoard bb = new BitBoard(gm.getBoard());

        Node root = new Node(null, bb, 0);

        searchOrder = getSearchOrder(bb);

        int score = 0;

        try {
            //score = negaMax(root, goToDepth, -Integer.MAX_VALUE, Integer.MAX_VALUE, playerColor);
            score = negaScout(root, goToDepth, -Integer.MAX_VALUE, Integer.MAX_VALUE, playerColor, true);
        } catch (InterruptedException e) {
        }

        if (isStopped) {
            return;
        }

        System.out.println("Player: " + playerColor);
        System.out.println("score: " + score);
        System.out.println("hitCount: " + hitCount);

        nextMove = BitBoard.longToPoint(root.chosenChild.move);
        suggestMove = BitBoard.longToPoint(root.chosenChild.chosenChild.move);

        long end = System.currentTimeMillis();

        System.out.println("Time: " + (end - start));

        Platform.runLater(() -> {
            progressProperty().set(1);
            readyProperty().set(true);
        });
    }

    protected long[] getSearchOrder(BitBoard bb) {
        return BitBoard.splitToArray(bb.getEmptySquares());
    }

    private int negaMax(Node parent, int depth, int alpha, int beta, int color) throws InterruptedException {
        if (isStopped) {
            throw new InterruptedException();
        }

        if (depth == 0) {
            return color * scoreChild(parent);
        }

        long list = parent.bb.getLegalMove(color);

        //startProgress(depth, list,true);
        if (list == 0) {

            if (parent.move == 0) {
                return color * scoreChild(new Node(parent, parent.bb, 0));
            } else {

                BitBoard bb = new BitBoard(parent.bb);
                Node child = new Node(parent, bb, 0);

                int score = -negaMax(child, depth, -beta, -alpha, -color);
                if (alpha < score) {
                    parent.chosenChild = child;
                    alpha = score;
                }
                return alpha;
            }

        } else {

            int score;
            for (long l : searchOrder) {
                if ((l & list) != 0) {
                    BitBoard bb = new BitBoard(parent.bb);
                    bb.takeTurn(l, color);
                    Node child = new Node(parent, bb, l);

//                    if ((l & BitBoard.CORNERS) != 0 && depth < 3) {
//                        score = -negaMax(child, depth + 1, -beta, -alpha, -color);
//                    } else {
//                        score = -negaMax(child, depth - 1, -beta, -alpha, -color);
//                    }
                    score = -negaMax(child, depth - 1, -beta, -alpha, -color);

                    if (alpha < score) {
                        parent.chosenChild = child;
                        alpha = score;
                    }

                }
                if (beta <= alpha) {
                    break;
                }
            }
            return alpha;
        }
    }

    private int negaScout(Node parent, int depth, int alpha, int beta, int color, boolean countProgress) throws InterruptedException {
        if (isStopped) {
            throw new InterruptedException();
        }

        if (depth == 0) {
            return color * scoreChild(parent);
        }

        long list = parent.bb.getLegalMove(color);

        if (list == 0) {

            if (parent.move == 0) {
                return color * scoreChild(new Node(parent, parent.bb, 0));
            } else {

                BitBoard bb = new BitBoard(parent.bb);
                Node child = new Node(parent, bb, 0);

                int score = -negaScout(child, depth, -beta, -alpha, -color, true);
                if (alpha < score) {
                    parent.chosenChild = child;
                    alpha = score;
                }
                return alpha;
            }

        } else {

            startProgress(depth, list);

            boolean first = true;
            int score;
            for (long l : searchOrder) {
                if ((l & list) != 0) {
                    BitBoard bb = new BitBoard(parent.bb);
                    bb.takeTurn(l, color);
                    Node child = new Node(parent, bb, l);

                    if (first) {
                        first = false;
                        score = -negaScout(child, depth - 1, -beta, -alpha, -color, true);
                    } else {
                        score = -negaScout(child, depth - 1, -alpha - 1, -alpha, -color, false);
                        if (alpha < score && score < beta) {
                            score = -negaScout(child, depth - 1, -beta, -score, -color, true);
                        }
                    }

                    if (alpha < score) {
                        parent.chosenChild = child;
                        alpha = score;
                    }

                    if (beta <= alpha) {
                        break;
                    }

                    countProgress(depth, countProgress);
                }
            }

            return alpha;
        }
    }

    protected int scoreChild(Node child) {

        int score = child.bb.calculateScoredifferenceForBlack();

        if (child.move == 0 && child.parent.move == 0) {
            if (score > 0) {
                score += WIN_VALUE;
            } else if (score < 0) {
                score += LOSE_VALUE;
            }
        }

        return score;
    }

    private void startProgress(int depth, long moves) {
        if (depth > progressDepth) {

            int element = goToDepth - depth;

            maxProgressTick[element] = Long.bitCount(moves) + 1;
            progressTick[element] = 0;

        }
    }

    private void countProgress(int depth, boolean countProgress) {
        if (countProgress && depth > progressDepth) {
            progressTick[goToDepth - depth]++;
            updateProgress();
        }
    }

    private void updateProgress() {

        //prevents flooding the gui thread with update requests
        now = System.currentTimeMillis();
        if (now - then > 50) {
            then = now;

            Platform.runLater(() -> {
                double value = 0;
                for (int i = goToDepth - progressDepth; i >= 0; i--) {
                    if (maxProgressTick[i] == 0) {
                        continue;
                    }

                    value += progressTick[i];
                    value /= maxProgressTick[i];
                }

                progressProperty().set(value);
            });
        }
    }

    public int getTargetDepth() {
        return targetDepth;
    }

    public void setTargetDepth(int targetDepth) {
        this.targetDepth = targetDepth;
        progressDepth = Math.min(targetDepth, DEFUALT_PROGRESS_DEPTH);
    }

    protected static class Node {

        final Node parent;
        final BitBoard bb;
        final long move;
        protected Node chosenChild;

        public Node(Node parent, BitBoard bb, long move) {
            this.parent = parent;
            this.bb = bb;
            this.move = move;
        }
    }

}
