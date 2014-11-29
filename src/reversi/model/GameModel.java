/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.model;

import java.awt.Point;
import java.util.ArrayList;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author James
 */
public class GameModel {

    private final int columns, rows;
    private ArrayList<Point> LastChanges;

    private final SimpleObjectProperty<Owner>[][] board;
    private final SimpleBooleanProperty[][] legalMoves;
    private final SimpleObjectProperty<Owner> turn;
    private final SimpleIntegerProperty whiteScore;
    private final SimpleIntegerProperty blackScore;
    private final SimpleBooleanProperty whiteNoMoves;
    private final SimpleBooleanProperty blackNoMoves;
    private final SimpleBooleanProperty whiteWin;
    private final SimpleBooleanProperty blackWin;
    private final SimpleBooleanProperty draw;
    private final SimpleBooleanProperty ready;
    private final boolean noMove = false;

    public GameModel(int columns, int rows) {
        ready = new SimpleBooleanProperty(false);
        this.columns = columns;
        this.rows = rows;
        LastChanges = new ArrayList<>();
        turn = new SimpleObjectProperty<>(Owner.NONE);

        board = new SimpleObjectProperty[columns][rows];

        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                board[i][j] = new SimpleObjectProperty<>(Owner.NONE);
            }
        }

        legalMoves = new SimpleBooleanProperty[columns][rows];
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                legalMoves[i][j] = new SimpleBooleanProperty(false);
                //legalMoves[i][j].bind(new legalMoveBooleanBinding(i, j));
            }
        }
        whiteScore = new SimpleIntegerProperty();
        blackScore = new SimpleIntegerProperty();

        whiteNoMoves = new SimpleBooleanProperty();
        blackNoMoves = new SimpleBooleanProperty();
        whiteWin = new SimpleBooleanProperty();
        blackWin = new SimpleBooleanProperty();
        draw = new SimpleBooleanProperty();

        initBoard();
    }

    private void initBoard() {

        int c = columns / 2;
        int r = rows / 2;

        board[c][r].setValue(Owner.BLACK);
        board[c][r - 1].setValue(Owner.WHITE);
        board[c - 1][r].setValue(Owner.WHITE);
        board[c - 1][r - 1].setValue(Owner.BLACK);

        whiteScore.set(2);
        blackScore.set(2);

        whiteNoMoves.set(false);
        blackNoMoves.set(false);
        whiteWin.set(false);
        blackWin.set(false);
        draw.set(false);

        turn.set(Owner.BLACK);
        findLegalMoves();
        ready.set(true);
    }

    public void resetBoard() {
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                board[i][j].setValue(Owner.NONE);
            }
        }
        initBoard();
    }

    public ArrayList<Point> takeTurn(int x, int y) {

        ArrayList<Point> list = flippedPieces(x, y);

        for (Point p : list) {
            board[p.x][p.y].set(turn.get());
        }

        LastChanges = list;
        updateScores();
        flipSide();
        //findLegalMoves();
        blackNoMoves.set(false);
        whiteNoMoves.set(false);
        testForEnd();

        return list;
    }

    public void placePiece(int x, int y, Owner owner) {

    }

    public void flipSide() {
        turn.setValue(turn.getValue().opposite());
        findLegalMoves();
    }

    public boolean isLegalMove(int x, int y) {
        return legalMoves[x][y].get();
    }

    public ArrayList<Point> flippedPieces(int x, int y) {
        ArrayList<Point> list = new ArrayList<>();
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

    private void testForEnd() {
        if (!isLegalMoves()) {
            setNoMove();
            flipSide();
        }
        if (!isLegalMoves()) {
            setNoMove();
            endGame();
        }
    }

    private void setNoMove() {
        if (turn.get().equals(Owner.BLACK)) {
            blackNoMoves.set(true);
        } else {
            whiteNoMoves.set(true);
        }
    }

    private void endGame() {
        if (blackScore.get() > whiteScore.get()) {
            blackWin.set(true);
        } else if (blackScore.get() < whiteScore.get()) {
            whiteWin.set(true);
        } else {
            draw.set(true);
        }
    }

    private boolean isLegalMoves() {
        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                if (legalMoves[x][y].getValue()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void findLegalMoves() {
        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                //legalMoves[x][y].set(false);
                legalMoves[x][y].setValue(canFlip(x, y));
            }
        }
    }

    private boolean canFlip(int x, int y) {
        if (!(board[x][y].getValue().equals(Owner.NONE))) {
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

        Owner t = turn.getValue();
        x += xd;
        y += yd;

        if (x < 0 || x >= columns || y < 0 || y >= rows) {
            return false;
        }

        if (board[x][y].getValue().opposite().equals(t)) {
            x += xd;
            y += yd;
            while (x >= 0 && x < columns && y >= 0 && y < rows) {
                if (board[x][y].getValue().equals(t)) {
                    return true;
                }
                if (board[x][y].getValue().equals(Owner.NONE)) {
                    return false;
                }
                x += xd;
                y += yd;
            }
        }
        return false;
    }

    private void printList(ArrayList<Point> list) {
        for (Point p : list) {
            System.out.println(p);
        }
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    private ArrayList<Point> flippedPiecesInDir(int x, int y, int xd, int yd) {
        ArrayList<Point> list = new ArrayList<>();
        Owner t = turn.getValue();
        x += xd;
        y += yd;

        if (x < 0 || x >= columns || y < 0 || y >= rows) {
            return list;
        }

        if (board[x][y].getValue().opposite().equals(t)) {
            list.add(new Point(x, y));
            x += xd;
            y += yd;
            while (x >= 0 && x < columns && y >= 0 && y < rows) {
                if (board[x][y].getValue().equals(t)) {
                    return list;
                } else if (board[x][y].getValue().equals(Owner.NONE)) {
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

    private void updateScores() {
        int count = LastChanges.size();
        int black = blackScore.get();
        int white = whiteScore.get();
        if (turn.get().equals(Owner.BLACK)) {
            blackScore.set(black + count);
            whiteScore.set(white - (count - 1));
        } else {
            blackScore.set(black - (count - 1));
            whiteScore.set(white + count);
        }
    }

    public int getRemainingTurns() {
        int numberOfSquare = columns * rows;
        int sumOfScores = whiteScore.get() + blackScore.get();

        return numberOfSquare - sumOfScores;
    }

    public SimpleObjectProperty<Owner> turnProperty() {
        return turn;
    }

    public SimpleIntegerProperty whiteScoreProperty() {
        return whiteScore;
    }

    public SimpleIntegerProperty blackScoreProperty() {
        return blackScore;
    }

    public ArrayList<Point> getLastChanges() {
        return LastChanges;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public SimpleObjectProperty<Owner>[][] boardProperty() {
        return board;
    }

    public SimpleBooleanProperty[][] legalMovesProperty() {
        return legalMoves;
    }

    public SimpleBooleanProperty whiteNoMovesProperty() {
        return whiteNoMoves;
    }

    public SimpleBooleanProperty blackNoMovesProperty() {
        return blackNoMoves;
    }

    public SimpleBooleanProperty whiteWinProperty() {
        return whiteWin;
    }

    public SimpleBooleanProperty blackWinProperty() {
        return blackWin;
    }

    public SimpleBooleanProperty drawProperty() {
        return draw;
    }

    public SimpleBooleanProperty ReadyProperty() {
        return ready;
    }

//    private class legalMoveBooleanBinding extends BooleanBinding {
//
//        int x, y;
//
//        public legalMoveBooleanBinding(int x, int y) {
//            this.x = x;
//            this.y = y;
//            bind(turn);
////            for (int xd = -1; xd < 2; xd++) {
////                for (int yd = -1; yd < 2; yd++) {
////                    if (xd == 0 && yd == 0) {
////                        continue;
////                    }
////                    int xc = x;
////                    int yc = y;
////                    xc += xd;
////                    yc += yd;
////                    while (xc >= 0 && xc < columns && yc >= 0 && yc < rows) {
////                        bind(board[xc][yc]);
////                        xc += xd;
////                        yc += yd;
////                    }
////                }
////            }
//        }
//
//        @Override
//        protected boolean computeValue() {
//            if (!(board[x][y].getValue().equals(Owner.NONE))) {
//                return false;
//            }
//            for (int xd = -1; xd < 2; xd++) {
//                for (int yd = -1; yd < 2; yd++) {
//                    if (xd == 0 && yd == 0) {
//                        continue;
//                    }
//                    if (canFlipInDir(x, y, xd, yd)) {
//                        return true;
//                    }
//                }
//            }
//            return false;
//
//        }
//
//        private boolean canFlipInDir(int x, int y, int xd, int yd) {
//            Owner t = turn.getValue();
//            x += xd;
//            y += yd;
//
//            if (x < 0 || x >= columns || y < 0 || y >= rows) {
//                return false;
//            }
//
//            if (board[x][y].getValue().opposite().equals(t)) {
//                x += xd;
//                y += yd;
//                while (x >= 0 && x < columns && y >= 0 && y < rows) {
//                    if (board[x][y].getValue().equals(t)) {
//                        return true;
//                    }
//                    if (board[x][y].getValue().equals(Owner.NONE)) {
//                        return false;
//                    }
//                    x += xd;
//                    y += yd;
//                }
//            }
//            return false;
//        }
//    }
}
