/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
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

    private SimpleObjectProperty<Owner>[][] board;
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
    private final SimpleIntegerProperty turnNumberProperty;
    private final SimpleBooleanProperty undoProperty;
    private final SimpleBooleanProperty redoProperty;
    private final List<SimpleObjectProperty<Owner>[][]> gameHistory;
    private final SimpleIntegerProperty historySizeProperty;

    public GameModel(int columns, int rows) {
        ready = new SimpleBooleanProperty(false);
        this.columns = columns;
        this.rows = rows;
        LastChanges = new ArrayList<>();
        turn = new SimpleObjectProperty<>(Owner.NONE);

        board = new SimpleObjectProperty[columns][rows];
        legalMoves = new SimpleBooleanProperty[columns][rows];

        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                board[x][y] = new SimpleObjectProperty<>(Owner.NONE);
                legalMoves[x][y] = new SimpleBooleanProperty(false);
            }
        }

        whiteScore = new SimpleIntegerProperty();
        blackScore = new SimpleIntegerProperty();

        whiteNoMoves = new SimpleBooleanProperty();
        blackNoMoves = new SimpleBooleanProperty();
        whiteWin = new SimpleBooleanProperty();
        blackWin = new SimpleBooleanProperty();
        draw = new SimpleBooleanProperty();

        gameHistory = new ArrayList<>();

        turnNumberProperty = new SimpleIntegerProperty();
        historySizeProperty = new SimpleIntegerProperty(0);
        redoProperty = new SimpleBooleanProperty();
        redoProperty.bind(turnNumberProperty.lessThan(historySizeProperty));
        undoProperty = new SimpleBooleanProperty();
        undoProperty.bind(turnNumberProperty.greaterThan(0));

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

        turnNumberProperty.set(0);
        historySizeProperty.set(0);
        gameHistory.clear();
        recordTurn();

        turn.set(Owner.BLACK);
        findLegalMoves();
        ready.set(true);
    }

    public void resetBoard() {
        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                board[x][y].setValue(Owner.NONE);
            }
        }
        initBoard();
    }

    public ArrayList<Point> takeTurn(int x, int y) {

        turnNumberProperty.set(turnNumberProperty.get() + 1);

        ArrayList<Point> list = flippedPieces(x, y);

        for (Point p : list) {
            board[p.x][p.y].set(turn.get());
        }

        recordTurn();

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

    public void undoTurn() {
        if (undoProperty.get()) {
            turnNumberProperty.set(turnNumberProperty.get() - 1);
            board = makeBoardCopy(gameHistory.get(turnNumberProperty.get()));
            flipSide();
        }
    }

    public void redoTurn() {
        if (redoProperty.get()) {
            turnNumberProperty.set(turnNumberProperty.get() + 1);
            board = makeBoardCopy(gameHistory.get(turnNumberProperty.get()));
            flipSide();
        }
    }

    private void recordTurn() {

        SimpleObjectProperty<Owner>[][] copy = makeBoardCopy(board);

        historySizeProperty.set(turnNumberProperty.get());

        if (historySizeProperty.get() < gameHistory.size()) {
            gameHistory.set(historySizeProperty.get(), copy);
        } else {
            gameHistory.add(copy);
        }

    }

    private SimpleObjectProperty<Owner>[][] makeBoardCopy(SimpleObjectProperty<Owner>[][] toCopy) {        
        SimpleObjectProperty<Owner>[][] copy = new SimpleObjectProperty[columns][rows];

        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                copy[x][y] = new SimpleObjectProperty<>(toCopy[x][y].get());
            }
        }

        return copy;
    }

    private void flipSide() {
        turn.setValue(turn.getValue().opposite());
        findLegalMoves();
    }

    private void testForEnd() {
        if (!isLegalMoves()) {
            setNoMove();
            recordTurn();
            turnNumberProperty.set(turnNumberProperty.get() + 1);
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
        int black = 0, white = 0;

        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                if (board[x][y].get().equals(Owner.BLACK)) {
                    black++;
                } else if (board[x][y].get().equals(Owner.WHITE)) {
                    white++;
                }
            }
        }

        blackScore.set(black);
        whiteScore.set(white);

//        int count = LastChanges.size();
//        int black = blackScore.get();
//        int white = whiteScore.get();
//        if (turn.get().equals(Owner.BLACK)) {
//            blackScore.set(black + count);
//            whiteScore.set(white - (count - 1));
//        } else {
//            blackScore.set(black - (count - 1));
//            whiteScore.set(white + count);
//        }
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

    public SimpleBooleanProperty UndoProperty() {
        return undoProperty;
    }

    public SimpleBooleanProperty RedoProperty() {
        return redoProperty;
    }

    public SimpleIntegerProperty TurnNumberProperty() {
        return turnNumberProperty;
    }
}
