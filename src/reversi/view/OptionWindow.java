/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.view;

import java.util.Collection;
import java.util.List;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
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

    private final ComboBox<PlayerType> blackCombo;
    private final ComboBox<PlayerType> whiteCombo;
    private final Slider blackTargetDepthSlider = new Slider();
    private final Slider blackCornerWeightSlider = new Slider();
    private final Slider blackEdgeWeightSlider = new Slider();
    private final Slider blackInnerEdgeWeightSlider = new Slider();
    private final Slider blackMiddleWeightSlider = new Slider();
    private final Label blackTargetDepthLabel = new Label();
    private final Label blackCornerWeightLabel = new Label();
    private final Label blackEdgeWeightLabel = new Label();
    private final Label blackInnerEdgeWeightLabel = new Label();
    private final Label blackMiddleWeightLabel = new Label();
    private final Slider whiteTargetDepthSlider = new Slider();
    private final Slider whiteCornerWeightSlider = new Slider();
    private final Slider whiteEdgeWeightSlider = new Slider();
    private final Slider whiteInnerEdgeWeightSlider = new Slider();
    private final Slider whiteMiddleWeightSlider = new Slider();
    private final Label whiteTargetDepthLabel = new Label();
    private final Label whiteCornerWeightLabel = new Label();
    private final Label whiteEdgeWeightLabel = new Label();
    private final Label whiteInnerEdgeWeightLabel = new Label();
    private final Label whiteMiddleWeightLabel = new Label();

    public OptionWindow(GameController gc) {
        this.gc = gc;
        settings = gc.getSettings();

        AnchorPane ap = new AnchorPane();
        GridPane gp = new GridPane();

        GridPane blackGrid = new GridPane(), whiteGrid = new GridPane();

        Label blackPlayer = new Label("Black Player: ");
        GridPane.setHalignment(blackPlayer, HPos.LEFT);
        GridPane.setValignment(blackPlayer, VPos.TOP);
        Label whitePlayer = new Label("White Player: ");
        GridPane.setHalignment(whitePlayer, HPos.LEFT);
        GridPane.setValignment(whitePlayer, VPos.TOP);

        ObservableList<PlayerType> ol = FXCollections.observableArrayList(PlayerType.values());
        blackCombo = new ComboBox<>(ol);
        blackCombo.setValue(settings.getBlackPlayer());
        GridPane.setValignment(blackCombo, VPos.TOP);
        whiteCombo = new ComboBox<>(ol);
        whiteCombo.setValue(settings.getWhitePlayer());
        GridPane.setValignment(whiteCombo, VPos.TOP);

        blackGrid.add(blackPlayer, 0, 0);
        blackGrid.add(blackCombo, 2, 0);
        RowConstraints rc = new RowConstraints(35);
        blackGrid.getRowConstraints().add(rc);
        blackGrid.addRow(1, makeLabelSliderPair("target depth:", blackTargetDepthLabel, blackTargetDepthSlider, 3, 15, settings.getBlackTargetDepth(), blackCombo, false));
        blackGrid.addRow(2, makeLabelSliderPair("corner weight:", blackCornerWeightLabel, blackCornerWeightSlider, -16, 128, settings.getBlackCornerWeight(), blackCombo, true));
        blackGrid.addRow(3, makeLabelSliderPair("edge weight:", blackEdgeWeightLabel, blackEdgeWeightSlider, -16, 128, settings.getBlackEdgeWeight(), blackCombo, true));
        blackGrid.addRow(4, makeLabelSliderPair("inner edge weight:", blackInnerEdgeWeightLabel, blackInnerEdgeWeightSlider, -16, 128, settings.getBlackInnerEdgeWeight(), blackCombo, true));
        blackGrid.addRow(5, makeLabelSliderPair("middle weight:", blackMiddleWeightLabel, blackMiddleWeightSlider, -16, 128, settings.getBlackMiddleWeight(), blackCombo, true));        

        whiteGrid.add(whitePlayer, 0, 0);
        whiteGrid.add(whiteCombo, 2, 0);
        whiteGrid.getRowConstraints().add(rc);
        whiteGrid.addRow(1, makeLabelSliderPair("target depth:", whiteTargetDepthLabel, whiteTargetDepthSlider, 3, 15, settings.getWhiteTargetDepth(), whiteCombo, false));
        whiteGrid.addRow(2, makeLabelSliderPair("corner weight:", whiteCornerWeightLabel, whiteCornerWeightSlider, -16, 128, settings.getWhiteCornerWeight(), whiteCombo, true));
        whiteGrid.addRow(3, makeLabelSliderPair("edge weight:", whiteEdgeWeightLabel, whiteEdgeWeightSlider, -16, 128, settings.getWhiteEdgeWeight(), whiteCombo, true));
        whiteGrid.addRow(4, makeLabelSliderPair("inner edge weight:", whiteInnerEdgeWeightLabel, whiteInnerEdgeWeightSlider, -16, 128, settings.getWhiteInnerEdgeWeight(), whiteCombo, true));
        whiteGrid.addRow(5, makeLabelSliderPair("middle weight:", whiteMiddleWeightLabel, whiteMiddleWeightSlider, -16, 128, settings.getWhiteMiddleWeight(), whiteCombo, true));

        blackGrid.setPadding(new Insets(5));
        whiteGrid.setPadding(new Insets(5));
        gp.add(blackGrid, 0, 0);
        gp.add(whiteGrid, 1, 0);
        gp.setPadding(new Insets(10));

        Button apply = new Button("Apply");
        apply.setOnAction(e -> {
            makeResults();
            hide();
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

        Scene scene = new Scene(ap, 800, 220);
        setTitle("Options");
        setResizable(false);
        setScene(scene);
    }

    private void makeResults() {
        settings = new Settings();

        settings.setBlackPlayer(blackCombo.getValue());
        settings.setWhitePlayer(whiteCombo.getValue());
        settings.setBlackTargetDepth((int) blackTargetDepthSlider.getValue());
        settings.setBlackCornerWeight((int) blackCornerWeightSlider.getValue());
        settings.setBlackEdgeWeight((int) blackEdgeWeightSlider.getValue());
        settings.setBlackInnerEdgeWeight((int) blackInnerEdgeWeightSlider.getValue());
        settings.setBlackMiddleWeight((int) blackMiddleWeightSlider.getValue());
        settings.setWhiteTargetDepth((int) whiteTargetDepthSlider.getValue());
        settings.setWhiteCornerWeight((int) whiteCornerWeightSlider.getValue());
        settings.setWhiteEdgeWeight((int) whiteEdgeWeightSlider.getValue());
        settings.setWhiteInnerEdgeWeight((int) whiteInnerEdgeWeightSlider.getValue());
        settings.setWhiteMiddleWeight((int) whiteMiddleWeightSlider.getValue());
    }

    public Settings getSettings() {
        return settings;
    }

    private Node[] makeLabelSliderPair(String text, Label label, Slider slider, double low, double high, double start, ComboBox<PlayerType> player, boolean isWeight) {
        slider.setMin(low);
        slider.setMax(high);
        slider.setValue(start);
        slider.setPrefWidth(245);

        label.textProperty().bind(new StringRounded(slider.valueProperty()));
        label.setPrefWidth(35);
        label.setMaxWidth(35);
        label.setPadding(new Insets(0, 10, 0, 0));
        label.setAlignment(Pos.BASELINE_RIGHT);

        Label word = new Label(text);
        word.setAlignment(Pos.BASELINE_RIGHT);

        PlayerType pt = player.getValue();
        boolean disable = (pt == PlayerType.HUMAN || pt == PlayerType.SIMPLE_AI || (pt == PlayerType.COMPLEX_AI && isWeight));
        label.setDisable(disable);
        slider.setDisable(disable);
        word.setDisable(disable);
        
        player.valueProperty().addListener((ob, ov, nv) -> {
            boolean disable2 = (nv == PlayerType.HUMAN || nv == PlayerType.SIMPLE_AI || (nv == PlayerType.COMPLEX_AI && isWeight));
            label.setDisable(disable2);
            slider.setDisable(disable2);
            word.setDisable(disable2);
        });

        return new Node[]{word, label, slider};
    }

    private static class StringRounded extends ObjectBinding<String> {

        private final DoubleProperty value;

        public StringRounded(DoubleProperty value) {
            this.value = value;
            bind(value);
        }

        @Override
        protected String computeValue() {
            return Integer.toString((int) value.get());
        }
    }
}
