/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.control;

import java.awt.Point;
import reversi.ai.AI;
import reversi.model.Owner;

/**
 *
 * @author James
 */
public class Player {

    private final Owner side;
    private AI ai;

    public Player(Owner side, AI ai) {
        this.side = side;
        this.ai = ai;
    }

    public AI getAI() {
        return ai;
    }

    public boolean isHuman() {
        return ai == null;
    }

    public boolean isAI() {
        return ai != null;
    }

    public Owner getSide() {
        return side;
    }
}
