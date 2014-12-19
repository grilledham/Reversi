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

    private BitBoard board;
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

        //board = new BoardModel(columns, rows, Owner.BLACK);
        board = new BitBoard();

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
        whiteScore.set(2);
        blackScore.set(2);

        whiteNoMoves.set(false);
        blackNoMoves.set(false);
        whiteWin.set(false);
        blackWin.set(false);
        draw.set(false);
        turn.set(Owner.BLACK);

        boardHistoryManager.reset();
        recordTurn(new Point(-1, -1), false);

        findLegalMoves();
        ready.set(true);
    }

    public void resetBoard() {
        board.resetBoard();
        initBoard();
    }

    public List<Point> takeTurn(int x, int y) {

        if (!isLegalMove(x, y)) {
            System.out.println("illegal move!");
        }

        List<Point> list = board.flippedPieces(x, y, turn.get());
        LastChanges = list;

        board.takeTurn(x, y, turn.get());
        flipSide();
        updateScores();

        testForEnd(x, y);

        return list;
    }

    public void placePiece(int x, int y, Owner owner) {

    }

    public boolean isLegalMove(int x, int y) {
        return legalMoves[x][y].get();
    }

    public void undoTurn() {
        updateGameState(boardHistoryManager.undoTurn());
    }

    public void redoTurn() {
        updateGameState(boardHistoryManager.redoTurn());
    }

    public void recordTurn(Point move, boolean isEnd) {
        boardHistoryManager.recordTurn(new GameState(board, move, turn.get(), isEnd));
    }

    private void updateGameState(GameState gs) {
        board = gs.getBoard();
        turn.set(gs.getOwner());
        updateScores();
        whiteNoMoves.set(false);
        blackNoMoves.set(false);        

        if (gs.isEnd()) {
            endGame();
            return;
        }
        blackWin.set(false);
        whiteWin.set(false);
        draw.set(false);

        findLegalMoves();

        if (!hasLegalMoves()) {
            setNoMove();
        }
    }

    private void flipSide() {
        turn.setValue(turn.getValue().opposite());
        findLegalMoves();
    }

    private void testForEnd(int x, int y) {
        blackNoMoves.set(false);
        whiteNoMoves.set(false);

        if (!hasLegalMoves()) {
            flipSide();

            if (!hasLegalMoves()) {
                recordTurn(null, true);
                endGame();

            } else {
                flipSide();
                recordTurn(new Point(x, y), false);
                setNoMove();
                flipSide();
                recordTurn(null, false);
            }

        } else {
            recordTurn(new Point(x, y), false);
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
        blackNoMoves.set(false);
        whiteNoMoves.set(false);
        turn.set(Owner.NONE);

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
        List<Point> list = BitBoard.splitToPoints(board.getLegalMoves(turn.get()));

        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                legalMoves[x][y].set(false);

            }
        }

        for (Point p : list) {
            legalMoves[p.x][p.y].set(true);
        }
    }

    public void updateScores() {
        blackScore.set(Long.bitCount(board.getBlackPieces()));
        whiteScore.set(Long.bitCount(board.getWhitePieces()));
    }

    public int getRemainingTurns() {
        int numberOfSquare = columns * rows;
        int sumOfScores = whiteScore.get() + blackScore.get();

        return numberOfSquare - sumOfScores;
    }

    public void applyEditChanges() {
        boardHistoryManager.reset();
        updateScores();        
        blackWin.set(false);
        whiteWin.set(false);
        draw.set(false);
        findLegalMoves();
        testForEnd(-1, -1);
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

    public BitBoard getBoard() {
        return board;
    }

}
