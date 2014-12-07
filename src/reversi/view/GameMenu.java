/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.view;

import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import reversi.control.GameController;
import reversi.model.GameModel;

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

        MenuItem editMode = new MenuItem("Edit Mode");
        editMode.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));
        editMode.setOnAction(e -> {
            System.out.println("edit mode");
        });

        MenuItem settingsMenuItem = new MenuItem("Settings");
        settingsMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        settingsMenuItem.setOnAction(e -> {
            ow.show();
        });

        file.getItems().addAll(newGame, save, load);
        edit.getItems().addAll(undoTurn, redoTurn, editMode);
        options.getItems().addAll(settingsMenuItem);
        getMenus().addAll(file, edit, options);
    }
}
