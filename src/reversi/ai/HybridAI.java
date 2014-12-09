/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.ai;

import reversi.model.GameModel;
import reversi.model.Owner;

/**
 *
 * @author James
 */
public class HybridAI extends ComplexAI {

    private int edgeValue;
    private int cornerValue;
    private static double edgeWieght = 2d;
    private static double cornerWieght = 16d;

    public HybridAI(GameModel gm) {
        super(gm);
    }

    @Override
    protected void scoreChild(int depth, Node child, Node parent) {
        Owner[][] board = child.bm.getBoard();
        int score = child.bm.calculateScoreDifference(turn);

        if (child.noMove && parent.noMove) {
            if (score > 0) {
                score += WIN_VALUE;
            } else if (score < 0) {
                score += LOSE_VALUE;
            }
        } else {
            score += getWeightValue(board);
        }

        if (depth % 2 == 0) {
            child.alpha = score;
        } else {
            child.beta = score;
        }
    }

    @Override
    protected void nextMoveHelper() {

        calculateWeights();
        super.nextMoveHelper();
    }

    private void calculateWeights() {

        int remainingTurns = gm.getRemainingTurns();
        int seenTurns = remainingTurns < MAX_DEPTH ? remainingTurns : MAX_DEPTH;

        //System.out.println("remaining: " + remainingTurns + ", seen: " + seenTurns);
        double ratio = ((double) remainingTurns / seenTurns);

        edgeValue = (int) ((ratio - 1) * edgeWieght);
        cornerValue = (int) ((ratio - 1) * cornerWieght);

        //System.out.println("edge: " + edgeValue + ", corner: " + cornerValue);
    }

    private int getWeightValue(Owner[][] board) {
        int us = 0;
        int them = 0;
        int c = col - 1;
        int r = row - 1;

        if (cornerValue != 0) {

            if (board[0][0] == turn) {
                us += cornerValue;
            } else {
                them += cornerValue;
            }
            if (board[0][r] == turn) {
                us += cornerValue;
            } else {
                them += cornerValue;
            }
            if (board[c][0] == turn) {
                us += cornerValue;
            } else {
                them += cornerValue;
            }
            if (board[c][r] == turn) {
                us += cornerValue;
            } else {
                them += cornerValue;
            }
        }

        if (edgeValue != 0) {

            for (int i = 1; i < c; i++) {
                if (board[i][0] == turn) {
                    us += edgeValue;
                } else {
                    them += edgeValue;
                }
                if (board[i][r] == turn) {
                    us += edgeValue;
                } else {
                    them += edgeValue;
                }
            }
            for (int i = 1; i < r; i++) {
                if (board[0][i] == turn) {
                    us += edgeValue;
                } else {
                    them += edgeValue;
                }
                if (board[c][i] == turn) {
                    us += edgeValue;
                } else {
                    them += edgeValue;
                }
            }
        }

        return us - them;
    }

}
