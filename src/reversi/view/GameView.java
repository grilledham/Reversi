/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.view;

import java.awt.Point;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import reversi.ai.AI;
import reversi.ai.SimpleAI;
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

        BorderPane.setMargin(black, new Insets(3));
        BorderPane.setMargin(white, new Insets(3));

        borderPane.setLeft(black);
        borderPane.setRight(white);
        //borderPane.setTop(new GameMenu(this));

        getChildren().setAll(borderPane);

    }

    public void reset() {
        initGame();
    }

    private final void initBoard() {

    }

//    private void intiAI() {
//        AI ai = new SimpleAI(gameModel);
//        uiReady.addListener((ob, ov, nv) -> {
//            if (nv && gameModel.turnProperty().get().equals(Owner.WHITE)) {
//                PauseTransition pt = new PauseTransition(new Duration(500));
//                pt.setOnFinished(e -> {
//                    Point p = ai.move();
////                    gameModel.takeTurn(p.x, p.y);
////                    board.animationFlipPieces(p.x, p.y);
//                    gameController.takeTurn(p.x, p.y);
//                });
//                pt.play();
//            }
//        });
//    }
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
