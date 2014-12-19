/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.control;

import java.awt.Point;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import reversi.model.GameModel;
import reversi.model.Owner;
import reversi.view.GameView;

/**
 *
 * @author James
 */
public class Editor {

    private SimpleObjectProperty<Owner> selection;

    private GameController gc;
    private SimpleBooleanProperty inEditedMode;
    private boolean changesMade;

    Editor(GameController gc) {
        this.gc = gc;

        inEditedMode = new SimpleBooleanProperty(false);
        selection = new SimpleObjectProperty<>(Owner.BLACK);
    }

    public void processClick(MouseEvent e, int x, int y) {
        if (e.getButton().equals(MouseButton.PRIMARY)) {
            gc.getGameModel().getBoard().placePiece(selection.get(), x, y);
            gc.getGameView().getBoard().placePiece(x, y);

        } else if (e.getButton().equals(MouseButton.SECONDARY)) {
            gc.getGameModel().getBoard().placePiece(Owner.NONE, x, y);
            gc.getGameView().getBoard().placePiece(x, y);

        } else if (e.getButton().equals(MouseButton.MIDDLE)) {
            if (selection.get() == Owner.BLACK) {
                selection.set(Owner.WHITE);
            } else {
                selection.set(Owner.BLACK);
            }
        }

        changesMade = true;
        gc.getGameModel().updateScores();
    }

    public void flipSide() {
        if (gc.getGameModel().turnProperty().get() == Owner.BLACK) {
            gc.getGameModel().turnProperty().set(Owner.WHITE);
        } else {
            gc.getGameModel().turnProperty().set(Owner.BLACK);
        }

        changesMade = true;
    }

    public SimpleBooleanProperty inEditedModeProperty() {
        return inEditedMode;
    }

    public SimpleObjectProperty<Owner> selectionProperty() {
        return selection;
    }

    public void enterEditMode() {
        if (inEditedMode.get()) {
            return;
        }
        inEditedMode.set(true);
        gc.blockUserProperty().set(true);
        changesMade = false;
    }

    public void leaveEditMode() {
        inEditedMode.set(false);
        if (changesMade) {
            gc.getGameModel().applyEditChanges();
        }
        gc.setAfterEditPlayer();
    }
}
