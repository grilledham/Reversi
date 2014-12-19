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
import reversi.model.BitBoard;
import reversi.model.GameModel;
import reversi.model.Settings;

/**
 *
 * @author James
 */
public class Player {

    private final AI ai;
    private final int color;

    public Player(int color, GameController gc) {
        this.color = color;
        GameModel gm = gc.getGameModel();
        Settings s = gc.getSettings();
        PlayerType pt = color == 1 ? s.getBlackPlayer() : s.getWhitePlayer();

        switch (pt) {
            case SIMPLE_AI: {
                ai = new SimpleAI(gm, color);
                break;
            }
            case COMPLEX_AI: {
                ai = new ComplexAI(gm, color);
                ComplexAI c = (ComplexAI) ai;
                if (color == BitBoard.BLACK_COLOR) {
                    c.setTargetDepth(s.getBlackTargetDepth());
                } else {
                    c.setTargetDepth(s.getWhiteTargetDepth());
                }
                break;
            }
            case HYBRID_AI: {
                ai = new HybridAI(gm, color);
                HybridAI h = (HybridAI) ai;
                if (color == BitBoard.BLACK_COLOR) {
                    h.setTargetDepth(s.getBlackTargetDepth());
                    h.setCornerWieght(s.getBlackCornerWeight());
                    h.setEdgeWeight(s.getBlackEdgeWeight());
                    h.setInnerEdgeWeight(s.getBlackInnerEdgeWeight());
                    h.setMiddleWeight(s.getBlackMiddleWeight());
                } else {
                    h.setTargetDepth(s.getWhiteTargetDepth());
                    h.setCornerWieght(s.getWhiteCornerWeight());
                    h.setEdgeWeight(s.getWhiteEdgeWeight());
                    h.setInnerEdgeWeight(s.getWhiteInnerEdgeWeight());
                    h.setMiddleWeight(s.getWhiteMiddleWeight());
                }
                break;
            }
            default: {
                ai = null;
            }
        }
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

    public int getColor() {
        return color;
    }

}
