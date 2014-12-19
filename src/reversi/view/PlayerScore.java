/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.view;

import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import reversi.control.GameController;
import reversi.model.GameModel;
import reversi.model.Owner;

/**
 *
 * @author James
 */
public class PlayerScore extends StackPane {

    public PlayerScore(Owner owner, GameController gc) {
        GameModel gm = gc.getGameModel();

        SimpleObjectProperty<String> scoreString = new SimpleObjectProperty<>("Score ");

        VBox vb = new VBox();
        Text player = new Text();
        Text playerType = new Text();
        Text score = new Text();
        Text noMove = new Text("No Moves");
        Text win = new Text("Winner!");

        FadeTransition noMoveFT = new FadeTransition(new Duration(250), noMove);
        noMove.setOpacity(0);
        noMoveFT.setFromValue(0);
        noMoveFT.setToValue(1);
        FadeTransition winFT = new FadeTransition(new Duration(100), win);
        win.setOpacity(0);
        winFT.setFromValue(0);
        winFT.setToValue(1);

        Color backgroundColor;
        Color foregroundColor;

        Region fade = new Region();
        fade.setOpacity(0);
        fade.setBorder(new Border(new BorderStroke(Color.DODGERBLUE, BorderStrokeStyle.SOLID, null, new BorderWidths(5))));
        FadeTransition yourMoveFT = new FadeTransition(new Duration(500), fade);
        yourMoveFT.setFromValue(0);
        yourMoveFT.setToValue(1);

        if (owner.equals(Owner.BLACK)) {
            backgroundColor = Color.BLACK;
            foregroundColor = Color.WHITE;
            player.setText("Black");
            playerType.setText(gc.getSettings().getBlackPlayer().toString());
            score.textProperty().bind(scoreString.asString().concat(gm.blackScoreProperty().asString("%2d")));

            yourMoveFT.play();
            gm.turnProperty().addListener((ob, ov, nv) -> {
                if (nv.equals(Owner.BLACK)) {
                    yourMoveFT.setRate(1);
                    yourMoveFT.play();
                } else {
                    if (yourMoveFT.getRate() == -1) {
                        return;
                    }
                    yourMoveFT.setRate(-1);
                    yourMoveFT.play();
                }
            });
            gm.blackNoMovesProperty().addListener((ob, ov, nv) -> {
                if (nv) {
                    noMoveFT.setRate(1);
                    noMoveFT.play();
                } else {                    
                    noMoveFT.setRate(-1);
                    noMoveFT.play();
                }
            });
            gm.blackWinProperty().addListener((ob, ov, nv) -> {
                if (nv) {
                    win.setText("Winner!");
                    winFT.setRate(1);
                    winFT.play();
                } else {
                    winFT.setRate(-1);
                    winFT.play();
                }
            });
            gm.drawProperty().addListener((ob, ov, nv) -> {
                if (nv) {
                    win.setText("Draw");
                    winFT.play();
                }
            });

        } else {
            backgroundColor = Color.WHITE;
            foregroundColor = Color.BLACK;
            player.setText("White");
            playerType.setText(gc.getSettings().getWhitePlayer().toString());
            score.textProperty().bind(scoreString.asString().concat(gm.whiteScoreProperty().asString("%2d")));

            gm.turnProperty().addListener((ob, ov, nv) -> {
                if (nv.equals(Owner.WHITE)) {
                    yourMoveFT.setRate(1);
                    yourMoveFT.play();
                } else {
                    if (yourMoveFT.getRate() == -1) {
                        return;
                    }
                    yourMoveFT.setRate(-1);
                    yourMoveFT.play();
                }
            });
            gm.whiteNoMovesProperty().addListener((ob, ov, nv) -> {
                if (nv) {
                    noMoveFT.setRate(1);
                    noMoveFT.play();
                } else {
                    noMoveFT.setRate(-1);
                    noMoveFT.play();
                }
            });
            gm.whiteWinProperty().addListener((ob, ov, nv) -> {
                if (nv) {
                    win.setText("Winner!");
                    winFT.setRate(1);
                    winFT.play();
                } else {
                    winFT.setRate(-1);
                    winFT.play();
                }
            });
            gm.drawProperty().addListener((ob, ov, nv) -> {
                if (nv) {
                    win.setText("Draw");
                    winFT.play();
                } else {
                    winFT.setRate(-1);
                    winFT.play();
                }
            });
        }

        setBackground(new Background(new BackgroundFill(backgroundColor, null, null)));
        player.setFill(foregroundColor);
        playerType.setFill(foregroundColor);
        score.setFill(foregroundColor);
        noMove.setFill(foregroundColor);
        win.setFill(foregroundColor);

        Font font = new Font("Monospaced Bold", 36);
        player.setFont(font);
        playerType.setFont(font);
        score.setFont(font);
        noMove.setFont(font);
        win.setFont(font);

        vb.setAlignment(Pos.CENTER);
        vb.setPadding(new Insets(10));
        vb.setMinWidth(240);

        vb.getChildren().addAll(player, playerType, score, noMove, win);
        getChildren().addAll(vb, fade);

    }

}
