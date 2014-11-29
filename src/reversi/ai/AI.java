/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.ai;

import java.awt.Point;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import reversi.model.GameModel;

/**
 *
 * @author James
 */
public abstract class AI {

    protected ReadOnlyBooleanWrapper ready;

    protected GameModel gm;
    
    protected Point nextMove;

    protected AI(GameModel gm) {
        this.gm = gm;
        this.ready = new ReadOnlyBooleanWrapper(false);
    }

    public abstract Point suggestMove();
    
    public abstract void requestNextMove();

    public abstract Point getMove();

    public ReadOnlyBooleanWrapper ReadyProperty() {
        return ready;
    }

}
