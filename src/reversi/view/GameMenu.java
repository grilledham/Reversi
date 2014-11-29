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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
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
    //private final GameView g;
    //private final GameMenu gameMenu = this;

    public GameMenu(GameController gameController) {
        this.gameController = gameController;
        gm = gameController.getGameModel();
        OptionWindow ow = new OptionWindow(gameController);

        Menu menuFile = new Menu("_File");

        MenuItem menuItemNewGame = new MenuItem("New Game");
        menuItemNewGame.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
        menuItemNewGame.setOnAction(e -> {
            Platform.runLater(() -> {
                gameController.setSettings(ow.getSettings());
                gameController.reset();
            });
        });

        MenuItem menuItemOption = new MenuItem("Options");
        menuItemOption.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        menuItemOption.setOnAction(e -> {
            ow.show();
        });

        menuFile.getItems().addAll(menuItemNewGame, menuItemOption);
        getMenus().addAll(menuFile);
    }
}
