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
    
    private PlayerType whitePlayer;
    private PlayerType blackPlayer;
    private int minAIDelay;
    private int columns, rows;

    public Settings() {
        this.columns = DEFAULT_SIZE;
        this.rows = DEFAULT_SIZE;
        this.whitePlayer = PlayerType.HYBRID_AI;
        this.blackPlayer = PlayerType.HUMAN;
        this.minAIDelay = 1000;
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

}
