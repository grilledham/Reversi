/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.ai;

import java.awt.Point;
import java.util.Random;
import reversi.model.BitBoard;
import reversi.model.GameModel;

/**
 *
 * @author James
 */
public class RandomAI extends AI {

    private Random r;

    public RandomAI(GameModel gm, int color) {
        super(gm, color);
        r = new Random();
    }

    @Override
    public Point suggestMove() {
        long[] moves = BitBoard.splitToArray(gm.getBoard().getLegalMove(playerColor * -1));

        int e = r.nextInt(moves.length);
        return BitBoard.longToPoint(moves[e]);
    }

    @Override
    public void requestNextMove() {
        readyProperty().set(false);
        isStopped = false;

        long[] moves = BitBoard.splitToArray(gm.getBoard().getLegalMove(playerColor));

        if (isStopped) {
            return;
        }

        int e = r.nextInt(moves.length);
        nextMove = BitBoard.longToPoint(moves[e]);

        readyProperty().set(true);
    }

    @Override
    public Point getMove() {
        return nextMove;
    }

}
