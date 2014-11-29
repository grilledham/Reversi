/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.view;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import reversi.control.GameController;
import reversi.control.PlayerType;
import reversi.model.Settings;

/**
 *
 * @author James
 */
public class OptionWindow extends Stage {

    private Settings settings;
    private final GameController gc;
    private final Label cTF;
    private final Label rTF;
    private Slider cSlider;
    private Slider rSlider;
    private CheckBox lockRatio;
    private ComboBox<PlayerType> blackCombo;
    private ComboBox<PlayerType> whiteCombo;

    public OptionWindow(GameController gc) {
        this.gc = gc;
        settings = gc.getSettings();

        AnchorPane ap = new AnchorPane();
        GridPane gp = new GridPane();

        Label columns = new Label("Number of Columns: ");
        GridPane.setHalignment(columns, HPos.RIGHT);
        Label rows = new Label("Number of Rows: ");
        GridPane.setHalignment(rows, HPos.RIGHT);

        cTF = new Label();
        cTF.setMinWidth(20);
        rTF = new Label();
        rTF.setMinWidth(20);

        //Sliders
        cSlider = new Slider(4, 16, settings.getColumns());
        cSlider.setSnapToTicks(true);
        cSlider.setBlockIncrement(2);
        cSlider.setMajorTickUnit(2);
        cSlider.setMinorTickCount(0);
        cSlider.setShowTickMarks(true);
        cSlider.setShowTickLabels(true);
        cTF.textProperty().bind(new StringRounded(cSlider.valueProperty()));
        rSlider = new Slider(4, 16, settings.getRows());
        rSlider.setSnapToTicks(true);
        rSlider.setBlockIncrement(2);
        rSlider.setMajorTickUnit(2);
        rSlider.setMinorTickCount(0);
        rSlider.setShowTickMarks(true);
        rSlider.setShowTickLabels(true);
        rTF.textProperty().bind(new StringRounded(rSlider.valueProperty()));

        lockRatio = new CheckBox("Lock aspect ratio");
        lockRatio.setOnAction(e -> {
            if (lockRatio.isSelected()) {
                rSlider.valueProperty().bindBidirectional(cSlider.valueProperty());
            } else {
                rSlider.valueProperty().unbindBidirectional(cSlider.valueProperty());
            }
        });

        Label blackPlayer = new Label("Black Player: ");
        GridPane.setHalignment(blackPlayer, HPos.RIGHT);
        Label whitePlayer = new Label("White Player: ");
        GridPane.setHalignment(whitePlayer, HPos.RIGHT);
        ObservableList<PlayerType> ol = FXCollections.observableArrayList(PlayerType.values());
        blackCombo = new ComboBox<>(ol);
        blackCombo.setValue(settings.getBlackPlayer());
        whiteCombo = new ComboBox<>(ol);
        whiteCombo.setValue(settings.getWhitePlayer());

        Region blank = new Region();
        blank.setPrefHeight(20);

        gp.add(columns, 0, 0);
        gp.add(cTF, 1, 0);
        gp.add(cSlider, 2, 0);
        gp.add(rows, 0, 1);
        gp.add(rTF, 1, 1);
        gp.add(rSlider, 2, 1);
        gp.add(lockRatio, 0, 2);
        gp.add(blank, 0, 3);
        gp.add(blackPlayer, 0, 4);
        gp.add(blackCombo, 2, 4);
        gp.add(whitePlayer, 0, 5);
        gp.add(whiteCombo, 2, 5);
        gp.setPadding(new Insets(10));

        Button apply = new Button("Apply");
        apply.setOnAction(e -> {
            makeResults();
            //gc.setSettings(settings);
            close();
        });

        Button cancel = new Button("Cancel");
        cancel.setOnAction(e -> {
            hide();
        });

        HBox hBox = new HBox(apply, cancel);
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.BOTTOM_RIGHT);

        ap.getChildren().addAll(gp, hBox);
        AnchorPane.setRightAnchor(hBox, 10d);
        AnchorPane.setBottomAnchor(hBox, 10d);

        initStyle(StageStyle.UTILITY);
        initModality(Modality.WINDOW_MODAL);
        initOwner(gc.getStage());

        Scene scene = new Scene(ap, 300, 300);
        setTitle("Options");
        setResizable(false);
        setScene(scene);        
    }

    private void makeResults() {
        int rows = (int) rSlider.getValue();
        int columns = (int) cSlider.getValue();

        settings = new Settings();
        settings.setColumns(columns);
        settings.setRows(rows);
        settings.setBlackPlayer(blackCombo.getValue());
        settings.setWhitePlayer(whiteCombo.getValue());
    }

    public Settings getSettings() {
        return settings;
    }

    private static class StringRounded extends ObjectBinding<String> {

        private final DoubleProperty value;

        public StringRounded(DoubleProperty value) {
            this.value = value;
            bind(value);
        }

        @Override
        protected String computeValue() {
            double round = Math.round(value.get() / 2) * 2;
            return String.format("%2.0f", round);
        }

    }

}
