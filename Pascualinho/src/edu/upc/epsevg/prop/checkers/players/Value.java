package edu.upc.epsevg.prop.checkers.players;

import java.awt.Point;
import java.util.List;

/**
 * Value
 * @author Marc Pascual Ivan Garcia
 */
public class Value {
    private int index;
    private int depth;

    /**
    * Constructora
    * 
    * @param index int valor d'index
    * @param depth Profunditat
    */
    public Value(int index , int depth) {
        
        this.index = index;
        this.depth = depth;
    }

    /**
    * Getter per index
    * 
    * @return index
    */
    public int getIndex() {
        
        return index;
    }

    /**
    * Setter per index
    * 
    * @param index 
    */
    public void setIndex(int index) {
        
        this.index = index;
        
    }

    /**
    * Getter per depth
    * 
    * @return Profunditat
    */
    public int getDepth() {
        
        return depth;
        
    }

    /**
    * Sether per depth
    * 
    * @param Profunditat
    */
    public void setDepth(int depth) {
        
        this.depth = depth;
    }
}
