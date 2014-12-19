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

    protected Point nextMove, suggestMove;
    
    protected int playerColor;

    protected volatile boolean isStopped;

    protected AI(GameModel gm, int color) {
        this.gm = gm;
        this.ready = new ReadOnlyBooleanWrapper(false);
        this.progress = new SimpleDoubleProperty(0);
        this.playerColor=color;
    }

    public abstract Point suggestMove();

    public abstract void requestNextMove();

    public abstract Point getMove();    

    public ReadOnlyBooleanWrapper ReadyProperty() {
        return readyProperty();
    }

    public ReadOnlyBooleanWrapper readyProperty() {
        return ready;
    }

    public SimpleDoubleProperty progressProperty() {
        return progress;
    }

    public boolean isStopped() {
        return isStopped;
    }

    public void setIsStopped(boolean isStopped) {
        this.isStopped = isStopped;
    }

}
