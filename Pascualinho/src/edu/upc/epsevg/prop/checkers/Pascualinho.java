package edu.upc.epsevg.prop.checkers.players;


import edu.upc.epsevg.prop.checkers.CellType;
import edu.upc.epsevg.prop.checkers.GameStatus;
import edu.upc.epsevg.prop.checkers.IAuto;
import edu.upc.epsevg.prop.checkers.IPlayer;
import edu.upc.epsevg.prop.checkers.MoveNode;
import edu.upc.epsevg.prop.checkers.PlayerMove;
import edu.upc.epsevg.prop.checkers.PlayerType;
import edu.upc.epsevg.prop.checkers.SearchType;

import edu.upc.epsevg.prop.checkers.players.Heuristica;
import edu.upc.epsevg.prop.checkers.players.Heuristica2;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Jugador aleatori
 * @author bernat
 */
public class Pascualinho implements IPlayer, IAuto {

    private String name;
    private GameStatus s;
    
    private boolean stop;
    private int depth;
    private PlayerType player;
    private int nodes;
    private boolean lategame;

    public Pascualinho(int depth) {
        name = "Pascualinho";
        stop = false;
        lategame = false;
        this.depth = depth;
        nodes = 0;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    /**
     * Ens avisa que hem de parar la cerca en curs perquè s'ha exhaurit el temps
     * de joc.
     */
    @Override
    public void timeout() {
        stop = true;
    }

    /**
     * Decideix el moviment del jugador donat un tauler i un color de peça que
     * ha de posar.
     *
     * @param s Tauler i estat actual de joc.
     * @return el moviment que fa el jugador.
     */
    @Override
    public PlayerMove move(GameStatus s) {
        //System.out.println("NOU MOVIMENT");
        //System.out.println("");
        //System.out.println("==================================00");
        //System.out.println("");
        player = s.getCurrentPlayer();
        
        if (s.getEmptyCellsCount() > s.getSize()*s.getSize()-10) lategame = true; 
            
        int h = 0, index = 0;
        List<List<Point>> moves = get_list(s);
        
        for (int i = 0; i < moves.size(); ++i) {  
            //System.out.println(moves.get(i));
            GameStatus copy = new GameStatus(s);
            copy.movePiece(moves.get(i));
                
            int aux = minmax(copy, depth-1, Integer.MIN_VALUE, Integer.MAX_VALUE,false);
         //   System.out.println(aux);
            if (i == 0) {
                h = aux;
                index = i;
            }  
            else {
                if (aux > h) {
                    h = aux;
                    index = i;
                }
            }
        }
        //System.out.println("Nodes: " + nodes);
        return new PlayerMove(moves.get(index), 0L, 0, SearchType.MINIMAX);          
    }
    
    public int minmax(GameStatus s,/* PlayerType player,*/ int depth, int alpha, int beta, boolean max) {
        ++nodes;
        if (depth == 0 || s.checkGameOver() || !s.currentPlayerCanMove()) {
            if (!lategame) {
                Heuristica h = new Heuristica();
                return h.getHeuristic(s, player);
            }
            else {
                Heuristica2 h = new Heuristica2();
                return h.getHeuristic(s, player);
            }
        }
        else {
                
            List<List<Point>> moves = get_list(s);
            for (int i = 0; i < moves.size(); ++i) {
                GameStatus copy = new GameStatus(s);
                copy.movePiece(moves.get(i));
                
                int h = minmax(copy, depth-1, alpha, beta, !max);

                if (max) alpha = Math.max(alpha, h);
                else beta = Math.min(beta, h);

                if (alpha >= beta) break; // Alpha-Beta Pruning
            }
        return max ? alpha : beta;
        }
    }
    
    public static List<List<Point>> get_list(GameStatus s) {
        List<List<Point>> moves = new ArrayList<>();
        List<MoveNode> moves_pos =  s.getMoves();
        for (int i = 0; i < moves_pos.size(); ++i) {
            MoveNode node = moves_pos.get(i);
            recorre_arbre(node, moves);
        }
        
        return moves;
    }
    
    public static void recorre_arbre(MoveNode node, List<List<Point>> list) {
        if (node.getChildren().isEmpty()) {
            List<Point> points = new ArrayList<>();
            
            MoveNode aux = node;        
            while (aux != null) {
                points.add(0,aux.getPoint());
                aux = aux.getParent();
            }
            
            list.add(points);
        }
        
        else {
            List<MoveNode> children = node.getChildren();
            for (int i = 0; i < children.size(); ++i) {
                recorre_arbre(children.get(i), list);
            }
        }
    }
}