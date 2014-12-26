/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.control;

import reversi.ai.AI;
import reversi.ai.MinMaxAI;
import reversi.ai.WeightedAI;
import reversi.ai.RandomAI;
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
            case RANDOM_AI: {
                ai = new RandomAI(gm, color);
                break;
            }
            case MINMAX_AI: {
                ai = new MinMaxAI(gm, color);
                MinMaxAI a = (MinMaxAI) ai;
                if (color == BitBoard.BLACK_COLOR) {
                    a.setTargetDepth(s.getBlackTargetDepth());
                } else {
                    a.setTargetDepth(s.getWhiteTargetDepth());
                }
                break;
            }
            case WEIGHTED_AI: {
                ai = new WeightedAI(gm, color);
                WeightedAI a = (WeightedAI) ai;
                if (color == BitBoard.BLACK_COLOR) {
                    a.setTargetDepth(s.getBlackTargetDepth());
                    a.setCornerWieght(s.getBlackCornerWeight());
                    a.setEdgeWeight(s.getBlackEdgeWeight());
                    a.setInnerEdgeWeight(s.getBlackInnerEdgeWeight());
                    a.setMiddleWeight(s.getBlackMiddleWeight());
                } else {
                    a.setTargetDepth(s.getWhiteTargetDepth());
                    a.setCornerWieght(s.getWhiteCornerWeight());
                    a.setEdgeWeight(s.getWhiteEdgeWeight());
                    a.setInnerEdgeWeight(s.getWhiteInnerEdgeWeight());
                    a.setMiddleWeight(s.getWhiteMiddleWeight());
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
