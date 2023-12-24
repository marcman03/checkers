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
import edu.upc.epsevg.prop.checkers.players.Heuristica;


/**
 * Jugador aleatori
 * @author Marc Pascual i Ivan Garcia
 */
public class Pascualinho_IDS implements IPlayer, IAuto {

    //Guardem en un String el nom del jugador
    private String name;
    
    //Guardem en un int el nombre de nodes visitats
    int nodes = 0;
    
    //Guardem en booleans la opció de fer una trampa al rival(heurística)
    //i de parar degut al timeout
    boolean trap;
    private boolean stop;
    
    //Guardem un PlayerType per saber si som Player1 o Player2
    private PlayerType player;
    
    //Guardem en un HashMap un long i un Value
    private HashMap<Long, Value> hashTable;
    
    
    public Pascualinho_IDS() {
        
        name = "PascualinhoIDS";
        
        stop = false;
        
        hashTable=new HashMap<>();
        
    }
    
    /**
     * Retorna el nom del jugador
     *
     * 
     * @return el nom del jugador.
     */
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
     * @param sg Tauler i estat actual de joc.
     * @return el moviment que fa el jugador.
     */
    @Override
    public PlayerMove move(GameStatus sg) {
        
        player = sg.getCurrentPlayer();
        
        int depth = 6; 
        
        int h = 0, index = 0;
        
        ElMeuStatus s=new ElMeuStatus(sg);
        
        //Obtenim el número de fitxes de cada jugador 
        int pieces1 = s.getScore(player);
        int pieces2 = s.getScore(PlayerType.opposite(player));
        
        //Si tenim més fitxes farem una trampa sinò no
        if (pieces1 > pieces2) trap = true;
        else trap=false;
        
        Value resultValue = hashTable.get(s.getHash());
        
        //Obtenim la llista de punts dels possibles moviments del tauler s
        List<List<Point>> moves = get_list(s);

        if (resultValue != null){
            
           List<Point>aux=moves.get(resultValue.getIndex());
           moves.remove(resultValue.getIndex());
           moves.add(0, aux);
           
        }
        
        int bestmove=0;
        
        //Va augmentant la depth fins que stop== true
        //bestmove= es queda amb el millor moviment es a dir si para en mitad
        //del nivell es guarda el del nivell anterior.
        while (!stop) {
            
            for (int i = 0; i < moves.size(); ++i) {  
                
                //Per cada moviment fem una còpia del tauler i executem el 
                //moviment
                ElMeuStatus copy = new ElMeuStatus(s);
                copy.movePiece(moves.get(i));
                
                int aux = minmax(copy, depth, Integer.MIN_VALUE, 
                                Integer.MAX_VALUE,false);
                
                if (stop)break;
                
                //Per el primer moviment guardem l'heurística i l'índex del  
                //vector del moviment
                if (i == 0) {
                    h = aux;
                    index = i;
                }
                //Per la resta, si l'heurística obtinguda és major que la més 
                //gran guardem la nova heurística màxima i l'índex del vector
                else {
                    if (aux > h) {
                        h = aux;
                        index = i;
                    }
                }  
            
            }
            
        if(!stop) bestmove=index;
        ++depth;
     
        }
        
        Value v= new Value(bestmove, depth);
        if(resultValue != null){
            
            if (depth > resultValue.getDepth())hashTable.put(s.getHash(),
                                                            v);

        }
        else hashTable.put(s.getHash(),v);
            
        stop=false;

        return new PlayerMove(moves.get(bestmove), nodes, depth, 
                              SearchType.MINIMAX);          
    }
    
    /**
     * Retorna un valor heurístic per cada tauler del nivell donat incialment 
     *
     * @param s Tauler i estat actual de joc.
     * @param depth Nivells que queden per baixar.
     * @param alpha Valor per la poda.
     * @param beta Valor per la poda.
     * @param max Indica i el nivell és un max o un min.
     * @return valor heurístic.
     */
    public int minmax(ElMeuStatus s, int depth, int alpha, int beta, 
                      boolean max) {
        
        //Augmentem el nombre de nodes visitats
        ++nodes;
        
        //Si hem arribat al límit de la profunditat, si hi ha un guanyador
        //o el jugador que li toca tirar no te moviments possibles.
        if (depth == 0 || s.checkGameOver() || !s.currentPlayerCanMove()) {
            
            Heuristica h=new Heuristica();
            return h.getHeuristic(s, player,trap);
           
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
                
                //Per cada moviment fem una còpia del tauler i executem el 
                //moviment
                ElMeuStatus copy = new ElMeuStatus(s);
                copy.movePiece(moves.get(i));
                
                //Cridem a minmax amb la còpia
                int h = minmax(copy, depth-1, alpha, beta, !max);
              
                //Si és un torn de maximitzar, si l'heurística obtinguda és 
                //major a l'alpha, alpha serà igual a l'heurística obtinguda
                if (max) {
                    if(h > alpha)index = i;
                    alpha = Math.max(alpha, h);
                    
                }
                //Si és un torn de minimitzar, si l'heurística obtinguda és 
                //menor a la beta, beta serà igual a l'heurística obtinguda
                else{
                    if(h < beta) index = i;
                    beta = Math.min(beta, h);
                    
                }
                
                //Si alpha és més gran o igual a beta podem aquesta branca
                if (alpha >= beta) break; // Alpha-Beta Pruning
            }
            
            Value v= new Value(index, depth);
            if(resultValue!=null){
                
                if (depth> resultValue.getDepth())hashTable.put(s.getHash(),
                                                                v);

            }
            else{
                
                hashTable.put(s.getHash(),v);
            }
            
            //Si és torn de maximitzar retornem alpha i sinò beta
            return max ? alpha : beta;
        }
    }
    
    /**
     *Retorna una llista de possibles moviments en forma de llista de llista de  
     *punts donat un tauler s
     * 
     * @param s Tauler i estat actual de joc.
     * 
     * @return Llista de llista de punts.
     */
    public static List<List<Point>> get_list(ElMeuStatus s) {
        
        List<List<Point>> moves = new ArrayList<>();
        List<MoveNode> moves_pos =  s.getMoves();
        for (int i = 0; i < moves_pos.size(); ++i) {
            MoveNode node = moves_pos.get(i);
            recorre_arbre(node, moves);
        }
        
        return moves;
    }
    
    /**
     *Omple una llista de llista de punts a partir d'un MoveNode 
     * 
     * @param node MoveNode d'un arbre d'un moviment.
     * @param list Llista de llista de punts a omplir.
     * 
     */
    public static void recorre_arbre(MoveNode node, List<List<Point>> list) {
        //Si node és una fulla recorrem l'arbre cap amunt per introduir a una 
        //llista de punts els punts dels nodes pares de node
        if (node.getChildren().isEmpty()) {
            
            List<Point> points = new ArrayList<>();
            
            MoveNode aux = node;  
            
            while (aux != null) {
                
                points.add(0,aux.getPoint());
                aux = aux.getParent();
            }
            
            list.add(points);
            
        }
        //Si no ho és cridem a recorre_arbre amb tots els seus fills
        else {
            
            List<MoveNode> children = node.getChildren();
            
            for (int i = 0; i < children.size(); ++i) {
                
                recorre_arbre(children.get(i), list);
                
            }
        }
    }
}
