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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * Jugador aleatori
 * @author bernat
 */
public class Pascualinho_IDS implements IPlayer, IAuto {

    private String name;
    private boolean stop;
    private PlayerType player;
    private HashMap<Long, Value> hashTable;
    int nodes=0;
    public Pascualinho_IDS() {
        name = "PascualinhoIDS";
        stop = false;
        hashTable=new HashMap<>();
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
        //System.out.println("SE ACABO el tiempo");
     
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
    public PlayerMove move(GameStatus sg) {
        player = sg.getCurrentPlayer();
        
        int depth = 6; 
        
        int h = 0, index = 0;
        ElMeuStatus s=new ElMeuStatus(sg);
       
        Value resultValue = hashTable.get(s.getHash());
        List<List<Point>> moves = get_list(s);

        if (resultValue != null){
            
           List<Point>aux=moves.get(resultValue.getIndex());
           moves.remove(resultValue.getIndex());
           moves.add(0, aux);
        }
        
        //PRUEBAS .-----------------------------
 
   //---------------------------------
   int bestmove=0;
        while (!stop) {
            
            for (int i = 0; i < moves.size(); ++i) {  
                //System.out.println(moves.get(i));
                ElMeuStatus copy = new ElMeuStatus(s);
                copy.movePiece(moves.get(i));
                int aux = minmax(copy,depth, Integer.MIN_VALUE, Integer.MAX_VALUE,false);
                
                if (stop)break;
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
        if(!stop)bestmove=index;
        ++depth;
     
    }
        
        Value v= new Value(bestmove, depth);
        if(resultValue!=null){
            if (depth> resultValue.getDepth())hashTable.put(s.getHash(),v);

        }
        else{
            hashTable.put(s.getHash(),v);
        }
        
        System.out.println("nodes: " + nodes);
        System.out.println("depth: " + depth);
        stop=false;

        return new PlayerMove(moves.get(bestmove), 0L, 0, SearchType.MINIMAX);          
    }
    
    public int minmax(ElMeuStatus s, int depth, int alpha, int beta, boolean max) {
        ++nodes;
        if (depth == 0 || s.checkGameOver() || !s.currentPlayerCanMove()) {
            Heuristica_IDS h=new Heuristica_IDS();
            return h.getHeuristic(s, player);
           
        }
        else {
            Value resultValue = hashTable.get(s.getHash());
            List<List<Point>> moves = get_list(s);
            

            if (resultValue != null){
                
                List<Point>aux=moves.get(resultValue.getIndex());
                moves.remove(resultValue.getIndex());
                moves.add(0, aux);
                
               
            }

            
            int index = 0;
            for ( int i = 0; i < moves.size(); ++i) {
                if (stop)break;
                ElMeuStatus copy = new ElMeuStatus(s);
                copy.movePiece(moves.get(i));
                
                int h = minmax(copy, depth-1, alpha, beta, !max);
              
                if (max) {
                    if(h>alpha)index=i;
                    alpha = Math.max(alpha, h);
                    
                }
                else{
                    if(h<beta)index=i;
                    beta = Math.min(beta, h);
                    
                }

                if (alpha >= beta) break; // Alpha-Beta Pruning
            }
            
            Value v= new Value(index, depth);
            if(resultValue!=null){
                if (depth> resultValue.getDepth())hashTable.put(s.getHash(),v);

            }
            else{
                hashTable.put(s.getHash(),v);
            }

            return max ? alpha : beta;
        }
    }
    
   
    public static List<List<Point>> get_list(ElMeuStatus s) {
        
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
