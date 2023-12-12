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
    private PlayerType player;

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
        player = s.getCurrentPlayer();
        
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
        
        return new PlayerMove(moves.get(index), 0L, 0, SearchType.MINIMAX);          
    }
    
    public int minmax(GameStatus s,/* PlayerType player,*/ int depth, int alpha, int beta, boolean max) {
        if (depth == 0 || s.checkGameOver() || !s.currentPlayerCanMove()) {
            return getHeuristic(s, player);
           
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
    
    public int getHeuristic(GameStatus s,PlayerType team){
        //System.out.println(""+s.toString());
        int h = 0;
        h = count_pieces(s, team) - count_pieces(s, PlayerType.opposite(team))
            + count_triangles(s, team)- count_triangles(s, PlayerType.opposite(team)) +
            trap(s, team)-trap(s, PlayerType.opposite(team));
     
        //System.out.println("H"+h);
       // System.out.println("==================================00");
        return h;
    }
    
    public int count_pieces (GameStatus s, PlayerType team) {
        int h=0;
        for (int y = 0; y < s.getSize(); ++y) {
            for (int x = 0; x < s.getSize(); ++x) { //TODO: recorrer bien madafaka + comprobaer si va bien
                if (team == PlayerType.PLAYER1) {
                    if (s.getPos(x, y) == CellType.P1) h += Math.pow(y+1, 2);
                    else if (s.getPos(x, y) == CellType.P1Q) h += 100; //TODO: las reinas no dan un buen valor
                }
                else {
                    if (s.getPos(x, y) == CellType.P2) h += Math.pow(s.getSize()-y,2);
                    else if (s.getPos(x, y) == CellType.P2Q) h += 100;
                }
            }
        }
        return h;
    }
    
    public int count_triangles (GameStatus s, PlayerType team) {
        int h=0;
        for (int y = 0; y < s.getSize(); ++y) {
            for (int x = 0; x < s.getSize(); ++x) { //TODO: recorrer bien madafaka + comprobaer si va bien
                if (team == PlayerType.PLAYER1) {
                    if (y > 0) {
                        if (s.getPos(x, y) == CellType.P1) {
                            h += 10 * comprovate_triangle(s, CellType.P1, x, y);
                        }
                    }    
                }
                else {
                    if (y < s.getSize()-1) {
                        if (s.getPos(x, y) == CellType.P2) {
                            h += 10 * comprovate_triangle(s, CellType.P2, x, y);
                        }
                    }    
                }
            }
        }
        return h;
    }
    
    public int comprovate_triangle (GameStatus s, CellType c, int x, int y) {
        int r = 0;
        
        if (c == CellType.P1) {
            if (x > 0) {
                if (s.getPos(x-1, y-1) == c) ++r; //Afegir s.getPos(x-1, y-1) == c or CellType.P1Q si volem comptar reines
            }
            else ++r;
            if (x < s.getSize()-1) {
                if (s.getPos(x+1, y-1) == c) ++r;
            }
            else ++r;
        }
        else if (c == CellType.P2) {
            if (x > 0) {
                if (s.getPos(x-1, y+1) == c) ++r; //Afegir s.getPos(x-1, y-1) == c or CellType.P1Q si volem comptar reines
            }
            else ++r;
            if (x < s.getSize()-1) {
                if (s.getPos(x+1, y+1) == c) ++r;
            }
            else ++r;
        }
        
        return r;
    }
    
    public int trap (GameStatus s, PlayerType player) {
        int h = 0;
        
        CellType c = CellType.P1;
        if (player == PlayerType.PLAYER2) c = CellType.P2; 
        
        for (int y = 0; y < s.getSize(); ++y) {
            for (int x = 0; x < s.getSize(); ++x) {
                
                    if (player == PlayerType.PLAYER1 ) {
                        if (y < 5) {
                            if (s.getPos(x, y) == c) h += comprovate_trap(s, c, x, y);  
                        }    
                    }
                    else {
                        if (y > 2) {
                            if (s.getPos(x, y) == c) h += comprovate_trap(s, c, x, y);
                        }    
                    }       
            }
        }
        
        return h;
    }
    
    public int comprovate_trap(GameStatus s, CellType c, int x, int y) {
        int h = 0;
        boolean out = false;
        boolean trap = false;
        
        if (c == CellType.P1) {
            
            
            if (y > 0) {
                if (x > 0) {
                    if (s.getPos(x-1, y-1) != c) out = true;      // si la ficha de atras no es del mismo tipo sale               
                } 
            }
            int x2 = x+1;
            int y2 = y+1;
            if (!out) {
               // System.out.println("x: "+ x +"y: "+y);
                while (x2 < s.getSize() & y2 < s.getSize() & !out) {
                    
                    if (x2-x == 1) {
                        if (s.getPos(x2, y2) != CellType.EMPTY) out = true; // una ficha alante tiene que ser espacio para seguir
                    }
                    if (x2-x == 2) {
                        if (s.getPos(x2, y2) != CellType.P1 & s.getPos(x2, y2) != CellType.P1Q) out = true; // 2 fichas alante que ser del mismo equipo para seguir
                    }
                    if (x2-x == 3) {
                        if (s.getPos(x2, y2) == CellType.P2 | s.getPos(x2, y2) == CellType.P2Q) trap = true;//3 fichas alante tiene que ser enemiga para trampa
                        out = true;
                    }
                    
                    ++x2;
                    ++y2;
                }
                if (trap) {
                    h += 30;
                }
            }
            
            out = false;
            trap = false;
        
            if (y > 0) {
                if (x < s.getSize()-1) {
                    if (s.getPos(x+1, y-1) != c) out = true;                     
                }
            x2 = x-1;
            y2 = y+1;
            
            }
            if (!out) {
                while (x2 >= 0 & y2 < s.getSize() & !out) {
                    if (x-x2 == 1) {
                        if (s.getPos(x2, y2) != CellType.EMPTY) out = true;
                    }
                    if (x-x2 == 2) {
                        if (s.getPos(x2, y2) != CellType.P1 & s.getPos(x2, y2) != CellType.P1Q) out = true;
                    }
                    if (x-x2 == 3) {
                        if (s.getPos(x2, y2) == CellType.P2 | s.getPos(x2, y2) == CellType.P2Q) trap = true;
                        out = true;
                    }
                    
                    --x2;
                    ++y2;
                }
                if (trap) {
                    h += 30;
                }
            }
        } 
        
        
        else if(c == CellType.P2) {  
            //P2 direccion arriba derecha es decir ++x --y
            if (y < s.getSize()-1) {
                    if (x > 0) {
                        if (s.getPos(x-1, y+1) != c) out = true;      // si la ficha de atras no es del mismo tipo sale               
                    } 
                }
            int x2 = x+1;
            int y2 = y-1;
            
            if (!out) {
                
                   
                    while (x2 < s.getSize() & y2 >=0   & !out) {

                        if (x2-x == 1) {
                            if (s.getPos(x2, y2) != CellType.EMPTY)out = true;// una ficha alante tiene que ser espacio para seguir
                        }
                        if (x2-x == 2) {
                            if (s.getPos(x2, y2) != CellType.P2 & s.getPos(x2, y2) != CellType.P2Q) out = true; // 2 fichas alante que ser del mismo equipo para seguir
                        }
                        if (x2-x == 3) {
                            if (s.getPos(x2, y2) == CellType.P1 | s.getPos(x2, y2) == CellType.P1Q) trap = true;//3 fichas alante tiene que ser enemiga para trampa
                            out = true;
                        }

                        ++x2;
                        --y2;
                    }
                    if (trap) {
                        h += 30;
                    }
                }
            
            out = false;
            trap = false;
            
            if (y > s.getSize()-1) {
                if (x > s.getSize()-1) {
                    if (s.getPos(x+1, y+1) != c) out = true;      // si la ficha de atras no es del mismo tipo sale               
                } 
            }
            x2 = x-1;
            y2 = y-1;
            
            if (!out) {
                   
                    while (x2 >=0 & y2 >=0   & !out) {
                        //System.out.println("x: "+ x +"y: "+y);

                        if (x-x2 == 1) {
                            if (s.getPos(x2, y2) != CellType.EMPTY)out = true;// una ficha alante tiene que ser espacio para seguir
                        }
                        if (x-x2 == 2) {
                            if (s.getPos(x2, y2) != CellType.P2 & s.getPos(x2, y2) != CellType.P2Q) out = true; // 2 fichas alante que ser del mismo equipo para seguir
                        }
                        if (x-x2 == 3) {
                            if (s.getPos(x2, y2) == CellType.P1 | s.getPos(x2, y2) == CellType.P1Q) trap = true;//3 fichas alante tiene que ser enemiga para trampa
                            out = true;
                        }

                        --x2;
                        --y2;
                    }
                    if (trap) {
                        h += 30;
                    }
                }
            
            
            
            
            
        
        
        
        
        }
        
        return h;
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
