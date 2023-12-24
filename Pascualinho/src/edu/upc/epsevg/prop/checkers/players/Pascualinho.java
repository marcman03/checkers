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

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Pascualinho
 * @author Marc Pascual Ivan Garcia
 */
public class Pascualinho implements IPlayer, IAuto {
    
    //Guardem en un String el nom del jugador
    private String name;
    
    //Guardem en un int la profunditat del minmax i el nombre de nodes visitats 
    private int depth;
    private int nodes;
    
    //Guardem en booleà la opció de fer una trampa al rival(heurística)
    private boolean trap;
    
    //Guardem un PlayerType per saber si som Player1 o Player2
    private PlayerType player;
    
    
    public Pascualinho(int depth) {
        
        name = "Pascualinho";
        
        this.depth = depth;
        nodes = 0;
        
        trap = false;
        
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
        
    }

    /**
     * Decideix el moviment del jugador donat un tauler 
     *
     * @param s Tauler i estat actual de joc.
     * @return el moviment que fa el jugador.
     */
    @Override
    public PlayerMove move(GameStatus s) {
        
        //Obtenim el nostre PlayerType
        player = s.getCurrentPlayer();
        
        //Obtenim el número de fitxes de cada jugador 
        int pieces1 = s.getScore(player);
        int pieces2 = s.getScore(PlayerType.opposite(player));
        
        //Si tenim més peces que el rival utilitzarem l'estratègia de posar-li 
        //trampes. Sinò, no l'utilitzarem.
        if (pieces1 > pieces2) trap = true;
        else trap = false;
        
        
        int h = 0, index = 0;
        
        //Obtenim la llista de punts dels possibles moviments del tauler s
        List<List<Point>> moves = get_list(s);
        
        for (int i = 0; i < moves.size(); ++i) {  

            //Per cada moviment fem una còpia del tauler i executem el moviment
            GameStatus copy = new GameStatus(s);
            copy.movePiece(moves.get(i));
            
            //Cridem a minmax amb la còpia
            int aux = minmax(copy, depth-1, Integer.MIN_VALUE, 
                             Integer.MAX_VALUE,false);

            //Per el primer moviment guardem l'heurística i l'índex del vector 
            //del moviment
            if (i == 0) {
                h = aux;
                index = i;
            }
            //Per la resta, si l'heurística obtinguda és major que la més gran 
            //guardem la nova heurística màxima i l'índex del vector
            else {
                if (aux > h) {
                    h = aux;
                    index = i;
                }
            }
            
        }
        
        return new PlayerMove(moves.get(index), nodes, depth, 
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
    public int minmax(GameStatus s, int depth, int alpha, int beta, 
                      boolean max) {
        
        //Augmentem el nombre de nodes visitats
        ++nodes;

        //Si hem arribat al límit de la profunditat, si hi ha un guanyador
        //o el jugador que li toca tirar no te moviments possibles.
        if (depth == 0 | s.checkGameOver() | !s.currentPlayerCanMove()) {
            
            Heuristica h = new Heuristica();
            return h.getHeuristic(s, player, trap);
            
        }
        
        else {
            
            //Obtenim la llista de punts dels possibles moviments del tauler s
            List<List<Point>> moves = get_list(s);
            
            
            for (int i = 0; i < moves.size(); ++i) {
                
                //Per cada moviment fem una còpia del tauler i executem el 
                //moviment
                GameStatus copy = new GameStatus(s);
                copy.movePiece(moves.get(i));
                
                //Cridem a minmax amb la còpia
                int h = minmax(copy, depth-1, alpha, beta, !max);
                
                //Si és un torn de maximitzar, si l'heurística obtinguda és 
                //major a l'alpha, alpha serà igual a l'heurística obtinguda
                if (max) alpha = Math.max(alpha, h);
                //Si és un torn de minimitzar, si l'heurística obtinguda és 
                //menor a la beta, beta serà igual a l'heurística obtinguda
                else beta = Math.min(beta, h);
                
                //Si alpha és més gran o igual a beta podem aquesta branca
                if (alpha >= beta) break; 
                
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
    public static List<List<Point>> get_list(GameStatus s) {
        
        //Obtenim els moviments possibles del tauler s
        List<MoveNode> moves_pos =  s.getMoves();
        
        List<List<Point>> moves = new ArrayList<>();
        
        for (int i = 0; i < moves_pos.size(); ++i) {
            
            //Per cada possible moviment cridem a la funció recorre_arbre
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
