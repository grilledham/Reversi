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

/**
 *
 * @author James
 */
public class BoardHistoryManager {

    private final SimpleIntegerProperty turnNumberProperty;
    private final List<GameState> boardHistory;
    private final SimpleIntegerProperty boardHistorySizeProperty;
    private final SimpleBooleanProperty undoProperty;
    private final SimpleBooleanProperty redoProperty;
    private final GameModel gm;
    private final int columns, rows;

    public BoardHistoryManager(GameModel gm) {
        this.gm = gm;
        this.columns = gm.getColumns();
        this.rows = gm.getRows();

        boardHistory = new ArrayList<>();

        turnNumberProperty = new SimpleIntegerProperty();
        boardHistorySizeProperty = new SimpleIntegerProperty(0);
        redoProperty = new SimpleBooleanProperty();
        redoProperty.bind(turnNumberProperty.lessThan(boardHistorySizeProperty));
        undoProperty = new SimpleBooleanProperty();
        undoProperty.bind(turnNumberProperty.greaterThan(0));
    }

    public void reset() {
        turnNumberProperty.set(-1);
        boardHistorySizeProperty.set(0);
        boardHistory.clear();
    }

    public GameState undoTurn() {
        turnNumberProperty.set(turnNumberProperty.get() - 1);
        GameState gs = boardHistory.get(turnNumberProperty.get());
        return new GameState(gs);
    }

    public GameState redoTurn() {
        turnNumberProperty.set(turnNumberProperty.get() + 1);
        GameState gs = boardHistory.get(turnNumberProperty.get());
        return new GameState(gs);
    }

    public void recordTurn(GameState gs) {
        turnNumberProperty.set(turnNumberProperty.get() + 1);
        boardHistorySizeProperty.set(turnNumberProperty.get());

        if (boardHistorySizeProperty.get() < boardHistory.size()) {
            boardHistory.set(boardHistorySizeProperty.get(), gs);
        } else {
            boardHistory.add(gs);
        }
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
