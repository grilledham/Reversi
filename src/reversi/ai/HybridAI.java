/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.ai;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import reversi.model.BitBoard;
import reversi.model.GameModel;
import reversi.model.Owner;

/**
 *
 * @author James
 */
public class HybridAI extends ComplexAI {

    private static final long EDGE = BitBoard.stringToBoard(""
            + "01111110"
            + "10000001"
            + "10000001"
            + "10000001"
            + "10000001"
            + "10000001"
            + "10000001"
            + "01111110");

    private static final long CORNER = BitBoard.stringToBoard(""
            + "10000001"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "10000001");

    private static final long INNER_EDGE = BitBoard.stringToBoard(""
            + "01000010"
            + "11111111"
            + "01000010"
            + "01000010"
            + "01000010"
            + "01000010"
            + "11111111"
            + "01000010");

//    private static final long MIDDLE = BitBoard.stringToBoard(""
//            + "00000000"
//            + "00000000"
//            + "00111100"
//            + "00111100"
//            + "00111100"
//            + "00111100"
//            + "00000000"
//            + "00000000");
    
    private static final long MIDDLE = BitBoard.stringToBoard(""
            + "00000000"
            + "01111110"
            + "01111110"
            + "01111110"
            + "01111110"
            + "01111110"
            + "01111110"
            + "00000000");

    private int cornerWieght = 64;
    private int edgeWeight = 12;
    private int middleWeight = 1;
    private int innerEdgeWeight = -8;

    //From worst to best.
    private long[] proirityArray;
    private int[] weights;

    public HybridAI(GameModel gm, int color) {
        super(gm, color);
    }

    @Override
    protected int scoreChild(Node child) {

        if ((child.move == 0 && child.parent.move == 0)) {
            int score = child.bb.calculateScoredifferenceForBlack();
            if (score > 0) {
                score += WIN_VALUE;
            } else if (score < 0) {
                score += LOSE_VALUE;
            }
            return score;
        }

        return getWeightValue(child.bb);
    }

    @Override
    protected void nextMoveHelper() {

        System.out.println("remaningTurns: " + gm.getRemainingTurns());

        calculateWeights();
        
        super.nextMoveHelper();
    }

    @Override
    protected long[] getSearchOrder(BitBoard bb) {
        List<Long> list = BitBoard.split(bb.getEmptySquares());

        Collections.sort(list, (l1, l2) -> getPriority(l2) - getPriority(l1));

        long[] array = new long[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }

        return array;
    }

    private int getPriority(long move) {

        for (int i = 0; i < proirityArray.length; i++) {
            if ((move & proirityArray[i]) != 0) {
                return i;
            }
        }
        return -1;
    }

    private void calculateWeights() {
        //From worst to best.
        proirityArray = new long[]{/*INNER_EDGE,*/MIDDLE, EDGE, CORNER};
        weights = new int[]{/*INNER_EDGE_WIEGHT,*/middleWeight, edgeWeight, cornerWieght};

//
//        int remainingTurns = gm.getRemainingTurns();
//        int depth = getGoToDepth();
//        int seenTurns = remainingTurns < depth ? remainingTurns : depth;
//
//        System.out.println("remaining: " + remainingTurns + ", seen: " + seenTurns);
//        double ratio = ((double) remainingTurns / seenTurns);
//
//        edgeValue = (int) ((ratio - 1) * EDGE_WIEGHT);
//        cornerValue = (int) ((ratio - 1) * CORNER_WIEGHT);
//
//        System.out.println("edge: " + edgeValue + ", corner: " + cornerValue);
    }

    private int getWeightValue(BitBoard board) {
        int score = 0;

        for (int i = 0; i < proirityArray.length; i++) {
            score += (Long.bitCount(board.getBlackPieces() & proirityArray[i]) * weights[i]);
            score -= (Long.bitCount(board.getWhitePieces() & proirityArray[i]) * weights[i]);
        }
        return score;
    }

    public int getCornerWieght() {
        return cornerWieght;
    }

    public void setCornerWieght(int cornerWieght) {
        this.cornerWieght = cornerWieght;
    }

    public int getEdgeWeight() {
        return edgeWeight;
    }

    public void setEdgeWeight(int edgeWeight) {
        this.edgeWeight = edgeWeight;
    }

    public int getMiddleWeight() {
        return middleWeight;
    }

    public void setMiddleWeight(int middleWeight) {
        this.middleWeight = middleWeight;
    }

    public int getInnerEdgeWeight() {
        return innerEdgeWeight;
    }

    public void setInnerEdgeWeight(int innerEdgeWeight) {
        this.innerEdgeWeight = innerEdgeWeight;
    }   
    
}
