/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi;

import java.awt.Point;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import reversi.model.BitBoard;
import reversi.control.GameController;

/**
 *
 * @author James
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        GameController gc = new GameController(primaryStage);

        Scene scene = new Scene(gc.getGameView(), 1039, 521);
        primaryStage.setTitle("Reversi");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);

    }
}
