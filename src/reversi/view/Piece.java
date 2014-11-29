/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.view;

import javafx.animation.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import reversi.model.GameModel;
import reversi.model.Owner;

/**
 *
 * @author James
 */
public class Piece extends Region {

    private final static Color DEFUALT_BLACK_COLOR = Color.BLACK;
    private final static Color DEFUALT_WHITE_COLOR = Color.WHITE;
    private final Timeline flipTl;
    private boolean replay = false;
    private final FadeTransition fadeTransition;

    private GameModel gm;

    private final SimpleObjectProperty<Owner> owner;

    public Piece(int x, int y, GameModel gm) {
        this.gm = gm;
        owner = gm.boardProperty()[x][y];

        setBackgroundColor(owner.get());

        setRotationAxis(Rotate.X_AXIS);

        KeyFrame kf = new KeyFrame(new Duration(0), new KeyValue(rotateProperty(), 0));
        KeyFrame kf2 = new KeyFrame(new Duration(250), e -> {
            setBackgroundColor(owner.get());
            replay=false;
        });
        KeyFrame kf3 = new KeyFrame(new Duration(500), new KeyValue(rotateProperty(), 180));
        flipTl = new Timeline(kf, kf2, kf3);
        flipTl.setOnFinished(e -> {
            if (replay) {
                replay = false;
                flipTl.play();
            }
        });

        fadeTransition = new FadeTransition(new Duration(500), this);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(.3);

//        owner.addListener((ob, ov, nv) -> {
//            setBackgroundColor(nv);
//
//        });
        setMouseTransparent(true);
    }

    private void setBackgroundColor(Owner nv) {
        if (nv.equals(Owner.BLACK)) {
            setBackground(new Background(new BackgroundFill(DEFUALT_BLACK_COLOR, new CornerRadii(1, true), null)));
        } else if (nv.equals(Owner.WHITE)) {
            setBackground(new Background(new BackgroundFill(DEFUALT_WHITE_COLOR, new CornerRadii(1, true), null)));
        } else {
            setBackground(Background.EMPTY);
        }
    }

    @Override
    public void resize(double width, double height) {
        setHeight(height / 1.15);
        setWidth(width / 1.15);
    }

    public void setCursorPiece() {
        setBackgroundColor(gm.turnProperty().get());
        setOpacity(.3);
    }

    public void unSetCursorPiece() {
        setBackgroundColor(Owner.NONE);
        setOpacity(1);
    }

    public void animateFlipPiece() {
        if (flipTl.getStatus().equals(Timeline.Status.RUNNING)) {
            replay = true;
        } else {
            flipTl.play();
        }
    }

    public void animateFadePiece() {
        fadeTransition.setRate(1);
        fadeTransition.play();
    }

    public void unAnimateFadePiece() {
        fadeTransition.setRate(-1);
        fadeTransition.play();
    }

    public void placePiece() {
        setBackgroundColor(owner.get());
    }

}
