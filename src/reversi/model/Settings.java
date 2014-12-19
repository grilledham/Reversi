/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.model;

import reversi.control.PlayerType;

/**
 *
 * @author James
 */
public class Settings {

    private final static int DEFAULT_SIZE = 8;

    private final static int DEFAULT_TARGET_DEPTH = 11;
    private final static int DEFAULT_CORNER_WEIGHT = 32;
    private final static int DEFAULT_EDGE_WEIGHT = 6;
    private final static int DEFAULT_INNER_EDGE_WEIGHT = -8;
    private final static int DEFAULT_MIDDLE_WEIGHT = -2;

    private final static int DEFAULT_MIN_AI_DELAY = 1000;

    private PlayerType whitePlayer;
    private PlayerType blackPlayer;
    private int blackTargetDepth;
    private int blackCornerWeight, blackEdgeWeight, blackInnerEdgeWeight, blackMiddleWeight;
    private int whiteTargetDepth;
    private int whiteCornerWeight, whiteEdgeWeight, whiteInnerEdgeWeight, whiteMiddleWeight;

    private int minAIDelay;

    private int columns, rows;

    public Settings() {
        this.columns = DEFAULT_SIZE;
        this.rows = DEFAULT_SIZE;

        this.whitePlayer = PlayerType.HYBRID_AI;
        this.blackPlayer = PlayerType.HUMAN;
        this.blackTargetDepth = DEFAULT_TARGET_DEPTH;
        this.blackCornerWeight = DEFAULT_CORNER_WEIGHT;
        this.blackEdgeWeight = DEFAULT_EDGE_WEIGHT;
        this.blackInnerEdgeWeight = DEFAULT_INNER_EDGE_WEIGHT;
        this.blackMiddleWeight = DEFAULT_MIDDLE_WEIGHT;
        this.whiteTargetDepth = DEFAULT_TARGET_DEPTH;
        this.whiteCornerWeight = DEFAULT_CORNER_WEIGHT;
        this.whiteEdgeWeight = DEFAULT_EDGE_WEIGHT;
        this.whiteInnerEdgeWeight = DEFAULT_INNER_EDGE_WEIGHT;
        this.whiteMiddleWeight = DEFAULT_MIDDLE_WEIGHT;

        this.minAIDelay = DEFAULT_MIN_AI_DELAY;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public PlayerType getWhitePlayer() {
        return whitePlayer;
    }

    public void setWhitePlayer(PlayerType whitePlayer) {
        this.whitePlayer = whitePlayer;
    }

    public PlayerType getBlackPlayer() {
        return blackPlayer;
    }

    public void setBlackPlayer(PlayerType blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

    public int getMinAIDelay() {
        return minAIDelay;
    }

    public void setMinAIDelay(int minAIDelay) {
        this.minAIDelay = minAIDelay;
    }

    public int getBlackTargetDepth() {
        return blackTargetDepth;
    }

    public void setBlackTargetDepth(int blackTargetDepth) {
        this.blackTargetDepth = blackTargetDepth;
    }

    public int getBlackCornerWeight() {
        return blackCornerWeight;
    }

    public void setBlackCornerWeight(int blackCornerWeight) {
        this.blackCornerWeight = blackCornerWeight;
    }

    public int getBlackEdgeWeight() {
        return blackEdgeWeight;
    }

    public void setBlackEdgeWeight(int blackEdgeWeight) {
        this.blackEdgeWeight = blackEdgeWeight;
    }

    public int getBlackInnerEdgeWeight() {
        return blackInnerEdgeWeight;
    }

    public void setBlackInnerEdgeWeight(int blackInnerEdgeWeight) {
        this.blackInnerEdgeWeight = blackInnerEdgeWeight;
    }

    public int getBlackMiddleWeight() {
        return blackMiddleWeight;
    }

    public void setBlackMiddleWeight(int blackMiddleWeight) {
        this.blackMiddleWeight = blackMiddleWeight;
    }

    public int getWhiteTargetDepth() {
        return whiteTargetDepth;
    }

    public void setWhiteTargetDepth(int whiteTargetDepth) {
        this.whiteTargetDepth = whiteTargetDepth;
    }

    public int getWhiteCornerWeight() {
        return whiteCornerWeight;
    }

    public void setWhiteCornerWeight(int whiteCornerWeight) {
        this.whiteCornerWeight = whiteCornerWeight;
    }

    public int getWhiteEdgeWeight() {
        return whiteEdgeWeight;
    }

    public void setWhiteEdgeWeight(int whiteEdgeWeight) {
        this.whiteEdgeWeight = whiteEdgeWeight;
    }

    public int getWhiteInnerEdgeWeight() {
        return whiteInnerEdgeWeight;
    }

    public void setWhiteInnerEdgeWeight(int whiteInnerEdgeWeight) {
        this.whiteInnerEdgeWeight = whiteInnerEdgeWeight;
    }

    public int getWhiteMiddleWeight() {
        return whiteMiddleWeight;
    }

    public void setWhiteMiddleWeight(int whiteMiddleWeight) {
        this.whiteMiddleWeight = whiteMiddleWeight;
    }

}
