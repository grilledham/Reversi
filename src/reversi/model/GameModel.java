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
    private List<Point> LastChanges;

    private BoardModel board;
    private BoardHistoryManager boardHistoryManager;
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

    public GameModel(int columns, int rows) {
        ready = new SimpleBooleanProperty(false);
        this.columns = columns;
        this.rows = rows;
        LastChanges = new ArrayList<>();
        turn = new SimpleObjectProperty<>(Owner.NONE);

        board = new BoardModel(columns, rows, Owner.BLACK);
        legalMoves = new SimpleBooleanProperty[columns][rows];

        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
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

        boardHistoryManager = new BoardHistoryManager(this);

        initBoard();
    }

    private void initBoard() {

        int c = columns / 2;
        int r = rows / 2;

        board.placePiece(c, r, Owner.BLACK);
        board.placePiece(c, r - 1, Owner.WHITE);
        board.placePiece(c - 1, r, Owner.WHITE);
        board.placePiece(c - 1, r - 1, Owner.BLACK);

        whiteScore.set(2);
        blackScore.set(2);

        whiteNoMoves.set(false);
        blackNoMoves.set(false);
        whiteWin.set(false);
        blackWin.set(false);
        draw.set(false);

        boardHistoryManager.reset();
        boardHistoryManager.recordTurn();
        turn.set(Owner.BLACK);
        findLegalMoves();
        ready.set(true);
    }

    public void resetBoard() {
        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                board.placePiece(x, y, Owner.NONE);
            }
        }
        initBoard();
    }

    public List<Point> takeTurn(int x, int y) {

        if(!isLegalMove(x, y)){
            System.out.println("illegal move!");
        }
        
        List<Point> list = board.takeTurn(x, y);

        boardHistoryManager.recordTurn();

        LastChanges = list;
        updateScores();
        flipSide();

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

    public void undoTurn() {
        board = boardHistoryManager.undoTurn();
        flipSide();
        updateScores();
    }

    public void redoTurn() {
        board = boardHistoryManager.redoTurn();
        flipSide();
        updateScores();
    }

    public void recordTurn() {
        boardHistoryManager.recordTurn();
    }

    private void flipSide() {
        turn.setValue(turn.getValue().opposite());
        findLegalMoves();
    }

    private void testForEnd() {
        if (!hasLegalMoves()) {
            setNoMove();
            boardHistoryManager.recordTurn();
            board.flipTurn();
            flipSide();
        }
        if (!hasLegalMoves()) {
            setNoMove();
            boardHistoryManager.recordTurn();
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

    private boolean hasLegalMoves() {
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
        List<Point> list = board.getLegalMoves();

        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                legalMoves[x][y].set(false);

            }
        }
        for (Point p : list) {
            legalMoves[p.x][p.y].set(true);
        }
    }

    private void updateScores() {
        int black = 0, white = 0;

        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                if (board.getPiece(x, y).equals(Owner.BLACK)) {
                    black++;
                } else if (board.getPiece(x, y).equals(Owner.WHITE)) {
                    white++;
                }
            }
        }

        blackScore.set(black);
        whiteScore.set(white);
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

    public List<Point> getLastChanges() {
        return LastChanges;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
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

    public BoardHistoryManager getBoardHistoryManager() {
        return boardHistoryManager;
    }

    public BoardModel getBoard() {
        return board;
    }

}
