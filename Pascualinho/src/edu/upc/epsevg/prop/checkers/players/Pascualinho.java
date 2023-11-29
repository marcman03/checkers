package edu.upc.epsevg.prop.checkers.players;


import edu.upc.epsevg.prop.checkers.GameStatus;
import edu.upc.epsevg.prop.checkers.IAuto;
import edu.upc.epsevg.prop.checkers.IPlayer;
import edu.upc.epsevg.prop.checkers.MoveNode;
import edu.upc.epsevg.prop.checkers.PlayerMove;
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

    public Pascualinho() {
        name = "Pascualinho";
    }

    @Override
    public void timeout() {
        // Nothing to do! I'm so fast, I never timeout 8-)
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


        List<MoveNode> moves_pos =  s.getMoves();
         List<Point> points = new ArrayList<>();
        List<List<Point>> moves = new ArrayList<>();
        
        for (int i = 0; i < moves_pos.size(); ++i) {
            MoveNode node = moves_pos.get(i);
            
            moves = recorre_arbre(node, moves);
        }
        
        return new PlayerMove( points, 0L, 0, SearchType.RANDOM);         
        
    }
    //function that returns the heuristic
    //Now only is counting the ally and enemy pieces.
    //Basic heuristic for testing
    public int getHeuristic(GameStatus s){
        return 1;
    
    
    
    
    
    
    }

    /**
     * Ens avisa que hem de parar la cerca en curs perquè s'ha exhaurit el temps
     * de joc.
     */
    @Override
    public String getName() {
        return name;
    }
    
    public static List<List<Point>> recorre_arbre(MoveNode node, List<List<Point>> list) {
        
        return list;
    }
}
