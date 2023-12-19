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
    private List<Point> move;
    private int depth;

    // Constructor
    public Value(List<Point> move, int depth) {
        this.move = move;
        this.depth = depth;
    }

    // Getter para move
    public List<Point> getMove() {
        return move;
    }

    // Setter para move
    public void setMove(List<Point> move) {
        this.move = move;
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
