/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.model;

import java.awt.Point;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import reversi.ai.BitBoard;

/**
 *
 * @author James
 */
public class BoardModel {

    private final int columns, rows;
    private final Owner[][] board;
    private Owner turn;

    public BoardModel(int columns, int rows, Owner turn) {
        this.columns = columns;
        this.rows = rows;
        this.turn = turn;
        this.board = new Owner[columns][rows];

        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                this.board[x][y] = Owner.NONE;
            }
        }
    }

    public BoardModel(BoardModel boardModel) {
        this.columns = boardModel.columns;
        this.rows = boardModel.rows;
        this.turn = boardModel.turn;
        this.board = new Owner[columns][rows];

        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                this.board[x][y] = boardModel.getBoard()[x][y];

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

    public List<Point> takeTurn(int x, int y) {
        List<Point> list = flippedPieces(x, y);

        for (Point p : list) {
            board[p.x][p.y] = turn;
        }
        turn = turn.opposite();

        return list;
    }

    public int calculateScoreDifference(Owner owner) {
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

    public List<Point> flippedPieces(int x, int y) {
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

    public void placePiece(int x, int y, Owner owner) {
        board[x][y] = owner;
    }

    public Owner getPiece(int x, int y) {
        return board[x][y];
    }

    public void flipTurn() {
        turn = turn.opposite();
    }

    public Owner[][] getBoard() {
        return this.board;
    }

    public Owner getTurn() {
        return turn;
    }

}
