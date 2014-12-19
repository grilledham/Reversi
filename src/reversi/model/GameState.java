/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.model;

import java.awt.Point;

/**
 *
 * @author James
 */
public class GameState {

    private final BitBoard board;
    private final Point move;
    private final Owner owner;
    private final boolean end;

    public GameState(BitBoard board, Point move, Owner owner, boolean end) {
        this.board = new BitBoard(board);
        this.move = move;
        this.owner = owner;
        this.end = end;
    }

    public GameState(GameState gs) {
        board = new BitBoard(gs.board);
        move = gs.move;
        owner = gs.owner;
        end = gs.end;
    }

    public BitBoard getBoard() {
        return board;
    }

    public Point getMove() {
        return move;
    }

    public Owner getOwner() {
        return owner;
    }

    public boolean isEnd() {
        return end;
    }
}
