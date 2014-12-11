/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.view;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import reversi.ai.AI;
import reversi.control.GameController;
import reversi.control.Player;

/**
 *
 * @author James
 */
public class StatusBar extends HBox {

    private GameController gc;
    private Label progress;
    private ChangeListener<Number> listner;
    private AI ai;
    //private int id = 0;

    public StatusBar(GameController gc) {

        this.gc = gc;

        progress = new Label();
        gc.currentPlayerProperty().addListener((ob, ov, nv) -> {
            //System.out.println("call");
            setProgress(nv);
        });

        listner = (ob, ov, nv) -> {
            if (nv.doubleValue() == 1) {
                progress.setText("AI done");
            } else {
                //int percentage = (int) (nv.doubleValue() * 100);
                double per = nv.doubleValue()*100;
                String percentage = String.format("%2.2f", per);
                progress.setText("AI thinking: " + percentage + "%");
            }
        };

        setProgress(gc.currentPlayerProperty().get());

        setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, CornerRadii.EMPTY, Insets.EMPTY)));
        setPadding(new Insets(2, 2, 2, 4));

        getChildren().add(progress);
    }

    private void setProgress(Player player) {
        if (player.isAI()) {

            ai = player.getAI();
            player.getAI().progressProperty().removeListener(listner);
            player.getAI().progressProperty().addListener(listner);

        } else {
            progress.setText("Waiting on the fleshy one...");
        }
    }
}
