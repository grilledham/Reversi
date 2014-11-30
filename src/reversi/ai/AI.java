/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.ai;

import java.awt.Point;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import reversi.model.GameModel;

/**
 *
 * @author James
 */
public abstract class AI {

    private ReadOnlyBooleanWrapper ready;

    private SimpleDoubleProperty progress;

    protected GameModel gm;

    protected Point nextMove;

    protected AI(GameModel gm) {
        this.gm = gm;
        this.ready = new ReadOnlyBooleanWrapper(false);
        this.progress = new SimpleDoubleProperty(0);
    }

    public abstract Point suggestMove();

    public abstract void requestNextMove();

    public abstract Point getMove();
    
    public abstract int getNumberOfMovesChecked();

    public ReadOnlyBooleanWrapper ReadyProperty() {
        return readyProperty();
    }

    public ReadOnlyBooleanWrapper readyProperty() {
        return ready;
    }

    public SimpleDoubleProperty progressProperty() {
        return progress;
    }

}
