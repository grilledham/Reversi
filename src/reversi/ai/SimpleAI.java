/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleBooleanProperty;
import reversi.model.GameModel;

/**
 * Assigns a priority to every square on the board. When
 * {@code requestNextMove()} is called sets {@code nextMove} to the highest
 * priority square that is a legal move.
 * <br> 
 * <br>
 * For a standard 8 by 8 board the priorities are: 
 * <br>
 * <br> 
 * (+64,+20,+36,+31,+31,+36,+20,+64) <br>
 * (+20,-24, -8,-13,-13, -8,-24,+20) <br>
 * (+36, -8, +8, +3, +3, +8, -8,+36) <br>
 * (+31,-13, +3, -2, -2, +3,-13,+31) <br>
 * (+31,-13, +3, -2, -2, +3,-13,+31) <br>
 * (+36, -8, +8, +3, +3, +8, -8,+36) <br>
 * (+20,-24, -8,-13,-13, -8,-24,+20) <br>
 * (+64,+20,+36,+31,+31,+36,+20,+64) <br>
 * <br>
 * Works for any size board.
 *
 *
 * @author James
 */
public class SimpleAI extends AI {

    private List<PriorityPoint> priorityList;
    private int cols;
    private int rows;
    private int numberOfXRings;
    private int numberOfYRings;

    public SimpleAI(GameModel gm, int color) {
        super(gm, color);
        initPriority();        
    }

    @Override
    public Point getMove() {
        return nextMove;
    }

    @Override
    public void requestNextMove() {
        readyProperty().set(false);
        isStopped=false;

        int x = -1;
        int y = -1;
        
        SimpleBooleanProperty[][] legalMoves = gm.legalMovesProperty();

        for (int i = 0; i < priorityList.size(); i++) {
            x = priorityList.get(i).x;
            y = priorityList.get(i).y;

            if (legalMoves[x][y].get()) {
                break;
            }
        }

        nextMove = new Point(x, y);

        if(isStopped){            
            return;
        }
        
        readyProperty().set(true);
    }

    @Override
    public Point suggestMove() {
        return getMove();
    }    

    private void initPriority() {
        cols = gm.getColumns();
        rows = gm.getRows();

        numberOfXRings = cols / 2;
        numberOfYRings = rows / 2;

        priorityList = new ArrayList<>(cols * rows);

        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                priorityList.add(determinePriority(x, y));                
            }            
        }

        priorityList.sort((p1, p2) -> p2.priority - p1.priority);

        readyProperty().set(true);
        progressProperty().set(1);
    }

    private PriorityPoint determinePriority(int x, int y) {
        PriorityPoint pp = new PriorityPoint();
        int xp;
        int yp;
        int pow;
        int xodd = numberOfXRings % 2;
        int yodd = numberOfYRings % 2;

        if (x < numberOfXRings) {
            xp = numberOfXRings - x;
        } else {
            xp = x - numberOfXRings + 1;
        }

        pow = (int) (Math.pow(2, xp - 1));
        if (xp % 2 != xodd) {
            xp = -xp;
        }
        xp *= pow;

        if (y < numberOfYRings) {
            yp = numberOfYRings - y;
        } else {
            yp = y - numberOfYRings + 1;
        }

        pow = (int) (Math.pow(2, yp - 1));
        if (yp % 2 != yodd) {
            yp = -yp;
        }
        yp *= pow;

        pp.setLocation(x, y);
        pp.priority = xp + yp;

        return pp;
    }

    private class PriorityPoint extends Point {

        public int priority;
    }

}
