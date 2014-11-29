/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.view;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import reversi.control.GameController;
import reversi.model.GameModel;

/**
 *
 * @author James
 */
public class LegalMoveMarker extends Region {

    public LegalMoveMarker(int x, int y, GameController gc) {
        GameModel gm = gc.getGameModel();
        setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(1, true), null)));
        visibleProperty().bind(gm.legalMovesProperty()[x][y].and(gc.blockUserProperty().not()));
        setMouseTransparent(true);
    }

    @Override
    public void resize(double width, double height) {
        setHeight(height / 5);
        setWidth(width / 5);
    }

}
