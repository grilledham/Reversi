/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.view;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import reversi.control.GameController;
import reversi.model.GameModel;

/**
 *
 * @author James
 */
public class Board extends GridPane {

    private List<Point> animatedWouldFlipPieces;
    private final GameModel gm;
    private final Square[][] squares;
    private boolean flipAnimationInProgress = true;    
    private SimpleObjectProperty<Point> mouseSquareProperty;
    private int columns, rows;

    public Board(GameController gc) {
        this.gm = gc.getGameModel();
        columns = gm.getColumns();
        rows = gm.getRows();
        animatedWouldFlipPieces = new ArrayList<>();

        squares = new Square[gm.getColumns()][gm.getRows()];

        mouseSquareProperty = new SimpleObjectProperty<>(new Point(-1, -1));

        gc.blockUserProperty().addListener((ob, ov, nv) -> {
            int x = mouseSquareProperty.get().x;
            int y = mouseSquareProperty.get().y;

            if (x == -1 || y == -1) {
                return;
            }
            if (!nv && gm.legalMovesProperty()[x][y].getValue()) {
                animationWouldFlipPieces(x, y);
            }

        });

        setBorder(new Border(
                new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(25)),
                new BorderStroke(Color.CORNSILK, BorderStrokeStyle.SOLID, null, new BorderWidths(20))
        ));

        setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        setVgap(1);
        setHgap(1);
        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                Square s = new Square(x, y, gc, this);
                squares[x][y] = s;
                //GridPane.setMargin(s, new Insets(1));
                GridPane.setHgrow(s, Priority.ALWAYS);
                GridPane.setVgrow(s, Priority.ALWAYS);
                add(s, x, y);
            }
        }

    }
    public void placePiece(int x, int y){
        squares[x][y].placePiece();
    }

    public void updateBoard() {
        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                squares[x][y].placePiece();
            }
        }
    }

    public void animationWouldFlipPieces(int x, int y) {
        animatedWouldFlipPieces = gm.getBoard().flippedPieces(x, y, gm.turnProperty().get());

        boolean first = true;
        for (Point p : animatedWouldFlipPieces) {
            Square s = squares[p.x][p.y];
            if (first) {
                s.setCursorPiece();
                first = false;
            } else {
                s.animateFadePiece();
            }
        }
    }

    public void unAnimationWouldFlipPieces() {
        boolean first = true;
        for (Point p : animatedWouldFlipPieces) {
            Square s = squares[p.x][p.y];
            if (first) {
                s.unSetCursorPiece();
                first = false;
            } else {
                s.unAnimateFadePiece();
            }
        }
        animatedWouldFlipPieces.clear();
    }

    private int compareDistanceFrom(Point p1, Point p2, int x, int y) {
        int d1 = Math.max(Math.abs(p1.x - x), Math.abs(p1.y - y));
        int d2 = Math.max(Math.abs(p2.x - x), Math.abs(p2.y - y));

        return d1 - d2;
    }

    public void animationFlipPieces(final List<Point> list) {
        unAnimationWouldFlipPieces();

        int x = list.get(0).x;
        int y = list.get(0).y;
        list.sort((Point p1, Point p2) -> {
            return compareDistanceFrom(p1, p2, x, y);
        });

        Timeline tm = new Timeline();

        int i = 1;
        int j = 1;
        while (i < list.size()) {
            do {
                Point p = list.get(j);
                KeyFrame kf = new KeyFrame(new Duration(150 * i), e -> {
                    squares[p.x][p.y].animateFlipPiece();
                });
                tm.getKeyFrames().add(kf);
                j++;
            } while (j < list.size() && (compareDistanceFrom(list.get(j - 1), list.get(j), x, y) == 0));
            i = j;
        }

        squares[x][y].placePiece();
        tm.play();
    }

    public void setMouseSquare(int x, int y) {
        mouseSquareProperty.set(new Point(x, y));
    }
}
