/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.ai;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Objects;
import reversi.ai.TranspositionTable.Key;
import reversi.ai.TranspositionTable.Value;
import reversi.model.BitBoard;

/**
 *
 * @author James
 */
public class TranspositionTable extends LinkedHashMap<Key, Value> {

    private static final long serialVersionUID = 1L;

    
    private final int maxAllowedSize;   

    public TranspositionTable(int startSize) {
        super(startSize, .75f, true);      
        maxAllowedSize=startSize<<1;
    }

    @Override
    protected boolean removeEldestEntry(Entry<Key, Value> eldest) {
        return size() > maxAllowedSize;
    }

    public static class Key {

        final BitBoard bb;
        final int color;

        public Key(BitBoard bb, int color) {
            this.bb = bb;
            this.color = color;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 41 * hash + Objects.hashCode(this.bb);
            hash = 41 * hash + this.color;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Key other = (Key) obj;
            if (!Objects.equals(this.bb, other.bb)) {
                return false;
            }
            if (this.color != other.color) {
                return false;
            }
            return true;
        }
        

    }

    public static class Value {

        final long move;
        final int score;

        public Value(long move, int score) {
            this.move = move;
            this.score = score;

        }
    }
}
