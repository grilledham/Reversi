/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.ai;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import reversi.model.Owner;

/**
 *
 * @author James
 */
public class LightWeightBoard {

    private int columns, rows;
    private Owner[][] board;
    private Owner turn;    

    public LightWeightBoard(int columns, int rows, Owner turn, Owner[][] board) {
        this.columns = columns;
        this.rows = rows;
        this.turn = turn;
        this.board = new Owner[columns][rows];

        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                this.board[x][y] = board[x][y];
            }
        }
    }

    public List<Point> getLegalMoves() {
        List<Point> list = new LinkedList<>();

        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                if (canFlip(x, y)) {
                    list.add(new Point(x, y));
                }
            }
        }

        return list;
    }

    public void takeTurn(int x, int y) {
        List<Point> list = flippedPieces(x, y);

        for (Point p : list) {
            board[p.x][p.y] = turn;
        }
        turn = turn.opposite();
    }

    public int calculateScore(Owner owner) {
        int us = 0;
        int them = 0;

        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                if (board[x][y].equals(owner)) {
                    us++;
                } else if (board[x][y].
                        equals(owner.opposite())) {
                    them++;
                }
            }
        }
        return us - them;
    }
    public void flipTurn(){
        turn = turn.opposite();
    }

    private boolean canFlip(int x, int y) {
        if (!(board[x][y].equals(Owner.NONE))) {
            return false;
        }
        for (int xd = -1; xd < 2; xd++) {
            for (int yd = -1; yd < 2; yd++) {
                if (xd == 0 && yd == 0) {
                    continue;
                }
                if (canFlipInDir(x, y, xd, yd)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canFlipInDir(int x, int y, int xd, int yd) {

        x += xd;
        y += yd;

        if (x < 0 || x >= columns || y < 0 || y >= rows) {
            return false;
        }

        if (board[x][y].opposite().equals(turn)) {
            x += xd;
            y += yd;
            while (x >= 0 && x < columns && y >= 0 && y < rows) {
                if (board[x][y].equals(turn)) {
                    return true;
                }
                if (board[x][y].equals(Owner.NONE)) {
                    return false;
                }
                x += xd;
                y += yd;
            }
        }
        return false;
    }

    private List<Point> flippedPieces(int x, int y) {
        List<Point> list = new LinkedList<>();
        list.add(new Point(x, y));

        for (int xd = -1; xd < 2; xd++) {
            for (int yd = -1; yd < 2; yd++) {
                if (xd == 0 && yd == 0) {
                    continue;
                }
                list.addAll(flippedPiecesInDir(x, y, xd, yd));
            }
        }
        return list;
    }

    private List<Point> flippedPiecesInDir(int x, int y, int xd, int yd) {
        List<Point> list = new LinkedList<>();

        x += xd;
        y += yd;

        if (x < 0 || x >= columns || y < 0 || y >= rows) {
            return list;
        }

        if (board[x][y].equals(turn.opposite())) {
            list.add(new Point(x, y));
            x += xd;
            y += yd;
            while (x >= 0 && x < columns && y >= 0 && y < rows) {
                if (board[x][y].equals(turn)) {
                    return list;
                } else if (board[x][y].equals(Owner.NONE)) {
                    list.clear();
                    return list;
                } else {
                    list.add(new Point(x, y));
                }
                x += xd;
                y += yd;
            }
        }
        list.clear();
        return list;
    }   

    public Owner[][] getBaord() {
        return this.board;
    }

    public Owner getTurn() {
        return turn;
    }
    
}
