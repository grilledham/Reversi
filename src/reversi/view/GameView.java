/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.view;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import reversi.control.GameController;
import reversi.model.GameModel;
import reversi.model.Owner;

/**
 *
 * @author James
 */
public class GameView extends StackPane {

    private GameModel gameModel;
    private GameController gameController;
    private Board board;
    private BorderPane borderPane;

    public GameView(GameController gameContorller) {
        this.gameController = gameContorller;
        this.gameModel = gameController.getGameModel();

        setBackground(new Background(new BackgroundFill(Color.DARKGREY, CornerRadii.EMPTY, Insets.EMPTY)));

        borderPane = new BorderPane();

        initGame();

        borderPane.setTop(gameContorller.getGameMenu());

    }

    private void initGame() {
        //gameModel.resetBoard();
//        borderPane = new BorderPane();

        board = new Board(gameController);
        borderPane.setCenter(board);
        BorderPane.setMargin(board, new Insets(3, 1, 3, 1));

        PlayerScore black = new PlayerScore(Owner.BLACK, gameController);
        PlayerScore white = new PlayerScore(Owner.WHITE, gameController);
        StatusBar sb = new StatusBar(gameController);

        BorderPane.setMargin(black, new Insets(3));
        BorderPane.setMargin(white, new Insets(3));
        BorderPane.setMargin(sb, new Insets(1, 3, 3, 3));

        borderPane.setLeft(black);
        borderPane.setRight(white);
        borderPane.setBottom(sb);
        //borderPane.setTop(new GameMenu(this));

        getChildren().setAll(borderPane);

    }
    public void updateBoard(){
        board.updateBoard();
    }
 
    public GameModel getGameModel() {
        return gameModel;
    }

    public GameController getGameController() {
        return gameController;
    }

    public Board getBoard() {
        return board;
    }

}
