package edu.upc.epsevg.prop.checkers.players;


import edu.upc.epsevg.prop.checkers.CellType;
import edu.upc.epsevg.prop.checkers.GameStatus;
import edu.upc.epsevg.prop.checkers.IAuto;
import edu.upc.epsevg.prop.checkers.IPlayer;
import edu.upc.epsevg.prop.checkers.MoveNode;
import edu.upc.epsevg.prop.checkers.PlayerMove;
import edu.upc.epsevg.prop.checkers.PlayerType;
import edu.upc.epsevg.prop.checkers.SearchType;
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

    public Pascualinho(int depth) {
        name = "Pascualinho";
        stop = false;
        this.depth = depth;
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
        int h = 0, index = 0;
        List<List<Point>> moves = get_list(s);
        
        for (int i = 0; i < moves.size(); ++i) {  
            //System.out.println(moves.get(i));
            int aux = minmax(s, moves.get(i), depth, Integer.MIN_VALUE, Integer.MAX_VALUE,true);
            System.out.println(aux);
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
        
        return new PlayerMove(moves.get(index), 0L, 0, SearchType.MINIMAX);          
    }
    
    public int minmax(GameStatus s, List<Point> points, int depth, int alpha, int beta, boolean max) {
        if (depth == 0) {
            return getHeuristic(s, s.getCurrentPlayer());
        }
        else {
            GameStatus copy = new GameStatus(s);
            copy.movePiece(points);
                
            List<List<Point>> moves = get_list(copy);
            for (int i = 0; i < moves.size(); ++i) {
                int h = minmax(copy, moves.get(i), depth-1, alpha, beta, !max);

                if (max) alpha = Math.max(alpha, h);
                else beta = Math.min(beta, h);

                if (alpha >= beta) break; // Alpha-Beta Pruning
            }
        return max ? alpha : beta;
        }
    }
    
    public int getHeuristic(GameStatus s,PlayerType team){
        //thinking width and height is the same
        int heuristic=0;
        for(int i=0; i<s.getSize();i+=1){
            int j = 0;
            if (i % 2 == 0) j = 1;
            while (j < s.getSize()) {
                if (s.getPos(i, j) == CellType.P2) {
                    if (i == 4) {
                        heuristic += 1;
                    }
                }
                
                j += 2;
            }
            
        
        } 
        return heuristic;
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
