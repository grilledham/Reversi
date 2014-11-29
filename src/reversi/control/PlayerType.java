/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.control;

import reversi.ai.AI;
import reversi.ai.ComplexAI;
import reversi.ai.HybridAI;
import reversi.ai.SimpleAI;
import reversi.model.GameModel;

/**
 *
 * @author James
 */
public enum PlayerType {

    HUMAN,
    SIMPLE_AI,
    COMPLEX_AI,
    HYBRID_AI;

    public boolean isHuman() {
        return this.equals(HUMAN);
    }

    public AI pickAI(GameModel gm) {
        if (this.equals(SIMPLE_AI)) {
            return new SimpleAI(gm);
        } else if (this.equals(COMPLEX_AI)) {
            return new ComplexAI(gm);
        } else if (this.equals(HYBRID_AI)) {
            return new HybridAI(gm);
        }
        return null;
    }

    @Override
    public String toString() {
        String s = super.toString().toLowerCase();
        s = s.replace("_", " ");
        s = s.replace("ai", "AI");
        String s1 = s.subSequence(0, 1) + "";
        s1 = s1.toUpperCase();
        s = s1 + s.substring(1);

        return s;
    }
}
