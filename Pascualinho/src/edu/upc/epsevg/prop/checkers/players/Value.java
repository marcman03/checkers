/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upc.epsevg.prop.checkers.players;

import java.awt.Point;
import java.util.List;

/**
 *
 * @author marc
 */
import java.util.List;

public class Value {
    private int index;
    private int depth;

    // Constructor
    public Value(int index , int depth) {
        this.index = index;
        this.depth = depth;
    }

    // Getter para move
    public int getIndex() {
        return index;
    }

    // Setter para move
    public void setIndex(int index) {
        this.index = index;
    }

    // Getter para depth
    public int getDepth() {
        return depth;
    }

    // Setter para depth
    public void setDepth(int depth) {
        this.depth = depth;
    }
}
