/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.control;

import reversi.ai.AI;
import reversi.ai.MinMaxAI;
import reversi.ai.WeightedAI;
import reversi.ai.SimpleAI;
import reversi.model.GameModel;

/**
 *
 * @author James
 */
public enum PlayerType {

    HUMAN,
    SIMPLE_AI,
    RANDOM_AI,
    MINMAX_AI,
    WEIGHTED_AI;

    public boolean isHuman() {
        return this.equals(HUMAN);
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
