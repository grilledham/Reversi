/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.view;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import reversi.control.GameController;
import reversi.model.GameModel;

/**
 *
 * @author James
 */
public class Square extends StackPane {

    private final Piece p;

    public Square(int x, int y, GameController gc) {
        GameModel gm = gc.getGameModel();
        Color c = Color.GREEN;
        Color c2 = Color.hsb(c.getHue(), c.getSaturation(), c.getBrightness() - 0.05);

        if (x % 2 == y % 2) {
            setBackground(new Background(new BackgroundFill(c, null, null)));
        } else {
            setBackground(new Background(new BackgroundFill(c2, null, null)));
        }

        p = new Piece(x, y, gm);
        LegalMoveMarker l = new LegalMoveMarker(x, y, gc);

        getChildren().addAll(p, l);

        setOnMousePressed(e -> {
            if(gc.blockUserProperty().get()){
                return;
            }
            if (gm.legalMovesProperty()[x][y].getValue()) {
                gc.takeTurn(x, y);

            }
        });
        setOnMouseEntered(e -> {
            if(gc.blockUserProperty().get()){
                return;
            }
            if (gm.legalMovesProperty()[x][y].getValue()) {
                Board b = (Board) getParent();
                b.animationWouldFlipPieces(x, y);
            }
        });
        setOnMouseExited(e -> {
            if (gm.legalMovesProperty()[x][y].getValue()) {
                Board b = (Board) getParent();
                b.unAnimationWouldFlipPieces();
            }
        });

    }

    public void animateFlipPiece() {
        p.animateFlipPiece();
    }

    public void animateFadePiece() {
        p.animateFadePiece();
    }

    public void unAnimateFadePiece() {
        p.unAnimateFadePiece();
    }

    public void placePiece() {
        p.placePiece();
    }

    public void setCursorPiece() {
        p.setCursorPiece();
    }

    public void unSetCursorPiece() {
        p.unSetCursorPiece();
    }

}
