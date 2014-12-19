/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.view;

import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCombination;
import reversi.control.Editor;
import reversi.control.GameController;
import reversi.model.GameModel;
import reversi.model.Owner;

/**
 *
 * @author James
 */
public class GameMenu extends MenuBar {

    private final GameController gameController;
    private final GameModel gm;

    public GameMenu(GameController gameController) {
        this.gameController = gameController;
        gm = gameController.getGameModel();
        OptionWindow ow = new OptionWindow(gameController);

        Menu file = new Menu("_File");
        Menu edit = new Menu("_Edit");
        Menu options = new Menu("_Options");

        MenuItem newGame = new MenuItem("New Game");
        newGame.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
        newGame.setOnAction(e -> {
            Platform.runLater(() -> {
                gameController.setSettings(ow.getSettings());
                gameController.reset();
            });
        });

        MenuItem save = new MenuItem("Save Game");
        save.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        save.setOnAction(e -> {
            System.out.println("save");
        });

        MenuItem load = new MenuItem("Load Game");
        load.setAccelerator(KeyCombination.keyCombination("Ctrl+L"));
        load.setOnAction(e -> {
            System.out.println("load");
        });

        MenuItem undoTurn = new MenuItem("Undo Turn");
        undoTurn.setAccelerator(KeyCombination.keyCombination("Ctrl+Z"));
        undoTurn.setOnAction(e -> {
            gameController.undoTurn();
        });
        undoTurn.disableProperty().bind(gm.getBoardHistoryManager().UndoProperty().not());

        MenuItem redoTurn = new MenuItem("Redo Turn");
        redoTurn.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
        redoTurn.setOnAction(e -> {
            gameController.redoTurn();
        });
        redoTurn.disableProperty().bind(gm.getBoardHistoryManager().RedoProperty().not());

        MenuItem editMode = new MenuItem("Enter Edit Mode");
        editMode.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));
        Editor editor = gameController.getEditor();
        editor.inEditedModeProperty().addListener((ob, ov, nv) -> {
            if (nv) {
                editMode.setText("Leave Edit Mode");
            } else {
                editMode.setText("Enter Edit Mode");
            }
        });

        editMode.setOnAction(e -> {
            if (editor.inEditedModeProperty().get()) {
                editor.leaveEditMode();
            } else {
                editor.enterEditMode();
            }
        });

        ToggleGroup place = new ToggleGroup();
        RadioMenuItem black = new RadioMenuItem("Place Black");
        black.setSelected(true);
        black.disableProperty().bind(editor.inEditedModeProperty().not());
        black.setOnAction(e -> {
            editor.selectionProperty().set(Owner.BLACK);
        });
        editor.selectionProperty().addListener((ob, ov, nv) -> {
            if (nv == Owner.BLACK) {
                black.setSelected(true);
            }
        });
        black.setToggleGroup(place);

        RadioMenuItem white = new RadioMenuItem("Place White");
        white.setSelected(false);
        white.disableProperty().bind(editor.inEditedModeProperty().not());
        white.setOnAction(e -> {
            editor.selectionProperty().set(Owner.WHITE);
        });
        editor.selectionProperty().addListener((ob, ov, nv) -> {
            if (nv == Owner.WHITE) {
                white.setSelected(true);
            }
        });
        white.setToggleGroup(place);

        MenuItem flip = new MenuItem("Flip side");
        flip.disableProperty().bind(editor.inEditedModeProperty().not());
        flip.setOnAction(e -> {
            editor.flipSide();
        });

        MenuItem settingsMenuItem = new MenuItem("Settings");
        settingsMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        settingsMenuItem.setOnAction(e -> {
            ow.show();
        });

        file.getItems().addAll(newGame, save, load);
        edit.getItems().addAll(undoTurn, redoTurn, new SeparatorMenuItem(), editMode, black, white, flip);
        options.getItems().addAll(settingsMenuItem);
        getMenus().addAll(file, edit, options);
    }
}
