/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.control;

import reversi.model.Settings;
import java.awt.Point;
import java.util.List;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.Stage;
import javafx.util.Duration;
import reversi.ai.AI;
import reversi.model.GameModel;
import reversi.model.Owner;
import reversi.view.GameMenu;
import reversi.view.GameView;

/**
 *
 * @author James
 */
public class GameController {

    private SimpleBooleanProperty finished;
    private SimpleBooleanProperty go;
    private AI ai;

    private GameMenu gameMenu;
    private GameModel gameModel;
    private GameView gameView;
    private final Stage stage;
    private Settings settings;
    private SimpleBooleanProperty blockUserProperty;
    private SimpleObjectProperty<Player> blackPlayer, whitePlayer, currentPlayer;

    public GameController(Stage stage) {
        this.stage = stage;
        settings = new Settings();
        blockUserProperty = new SimpleBooleanProperty();
        currentPlayer = new SimpleObjectProperty<>();

        intiGame();
    }

    public void takeTurn(int x, int y) {

        if (!gameModel.isLegalMove(x, y)) {
            System.out.println("Illegal move");
            return;
        }

        List<Point> list = gameModel.takeTurn(x, y);
        gameView.getBoard().animationFlipPieces(list);

        setCurrentPlayer();
        requestCurrentPlayerMove();
    }

    public void undoTurn() {
        if (currentPlayer.get().isAI()) {
            currentPlayer.get().getAI().setIsStopped(true);
        }

        gameModel.undoTurn();
        gameView.updateBoard();
        setCurrentPlayer();
        requestCurrentPlayerMove();
    }

    public void redoTurn() {
        if (currentPlayer.get().isAI()) {
            currentPlayer.get().getAI().setIsStopped(true);
        }

        gameModel.redoTurn();
        gameView.updateBoard();
        setCurrentPlayer();
        requestCurrentPlayerMove();
    }

    public void gotoTurn(int turn) {

    }

    public void play() {

    }

    public void reset() {//Settings settings) {
        intiGame();
        stage.getScene().setRoot(gameView);

    }

    private void intiGame() {
        gameModel = new GameModel(settings.getColumns(), settings.getRows());
        gameMenu = new GameMenu(this);

        finished = new SimpleBooleanProperty(false);
        go = new SimpleBooleanProperty(false);

        if (settings.getBlackPlayer().isHuman()) {
            blackPlayer = new SimpleObjectProperty<>(new Player(Owner.BLACK, null));
        } else {
            blackPlayer = new SimpleObjectProperty<>(new Player(Owner.BLACK, settings.getBlackPlayer().pickAI(gameModel)));
        }

        if (settings.getWhitePlayer().isHuman()) {
            whitePlayer = new SimpleObjectProperty<>(new Player(Owner.WHITE, null));
        } else {
            whitePlayer = new SimpleObjectProperty<>(new Player(Owner.WHITE, settings.getWhitePlayer().pickAI(gameModel)));
        }

        setCurrentPlayer();
        gameView = new GameView(this);

        go.addListener((ob, ov, nv) -> {
            if (nv && !ai.isStopped()) {
                Point p = ai.getMove();
                takeTurn(p.x, p.y);
            }
        });

        //setCurrentPlayer();
        requestCurrentPlayerMove();

    }

    private void requestCurrentPlayerMove() {
        if (gameModel.blackWinProperty().get()
                || gameModel.whiteWinProperty().get()
                || gameModel.drawProperty().get()) {
            blockUserProperty.set(true);
            return;
        }

        if (currentPlayer.get().isHuman()) {
            blockUserProperty.set(false);
        } else {
            ai = currentPlayer.get().getAI();
            blockUserProperty.set(true);
            scheduleAIMove();
        }
    }

    private void scheduleAIMove() {
        finished.set(false);

        go.bind(finished.and(ai.ReadyProperty()));

        PauseTransition pt = new PauseTransition(new Duration(settings.getMinAIDelay()));
        pt.setOnFinished(e -> {
            finished.set(true);
        });
        pt.play();

        ai.requestNextMove();

    }

    private void setCurrentPlayer() {
        if (gameModel.turnProperty().get() == Owner.BLACK) {
            currentPlayer.set(blackPlayer.get());
        } else {
            currentPlayer.set(whitePlayer.get());
        }
    }

    public GameMenu getGameMenu() {
        return gameMenu;
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    public GameView getGameView() {
        return gameView;
    }

    public Stage getStage() {
        return stage;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public SimpleBooleanProperty blockUserProperty() {
        return blockUserProperty;
    }

    public SimpleObjectProperty<Player> currentPlayerProperty() {
        return currentPlayer;
    }

}
