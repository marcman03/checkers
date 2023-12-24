/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upc.epsevg.prop.checkers.players;

import edu.upc.epsevg.prop.checkers.CellType;
import edu.upc.epsevg.prop.checkers.GameStatus;
import edu.upc.epsevg.prop.checkers.MoveNode;
import edu.upc.epsevg.prop.checkers.PlayerType;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Heuristica
 * 
 * @author Marc Pascual Ivan Garcia
 */
public class Heuristica {
    
    public Heuristica () {
        
    }
    
    /**
     * Retorna un valor heurístic donat un tauler s, un PlayerType team i un 
     * booleà trap
     *
     * @param s Tauler i estat actual de joc.
     * @param player PlayerType respecte l'heuristica
     * @param trap Boolea que ens diu si fem traps o no.
     * @return valor heuristic
     */
    public int getHeuristic(GameStatus s, PlayerType player, boolean trap){
        
        //És crida a cada funció heurística pel nostre jugador i pel contrari
        //i després es resten els 2 valors
        
        int pieces_1 = count_pieces(s,player);
        int pieces_2 = count_pieces(s,PlayerType.opposite(player));
        
        int triangles_1 = count_triangles(s, player);
        int triangles_2 = count_triangles(s, PlayerType.opposite(player));
        
        int trap_1 = 0;
        int trap_2 = 0;
        
        if (trap) {
            trap_1 = trap(s, player);
            trap_2 = trap(s, PlayerType.opposite(player));
        }    
        
        int h = pieces_1 - pieces_2
                + triangles_1 - triangles_2
                + trap_1 - trap_2
                ;
     
        
        return h;
    }
    
    /**
     * Retorna un valor donat un tauler s i un PlayerType player
     *
     * @param s Tauler i estat actual de joc.
     * @param player PlayerType respecte l'heuristica
     * @return valor heuristic
     */
    public int count_pieces (GameStatus s, PlayerType player) {
        
        int h=0;
        
        //Recorrem el tauler i per cada fitxa nostra que trobem es suma a h un 
        //valorsegons si és una reina o no i segons la posició x i y de la fitxa  
        //al tauler
        for (int y = 0; y < s.getSize(); ++y) {
            for (int x = 0; x < s.getSize(); ++x) { 
                
                if (player == PlayerType.PLAYER1) {
                    if (s.getPos(x, y) == CellType.P1) h += 100 
                            + sum_y(x,y,player) + sum_x(x,y,player);
                    else if (s.getPos(x, y) == CellType.P1Q) h += 150 
                            + sum_y2(x,y,player) + sum_x(x,y,player); 
                }
                else {
                    if (s.getPos(x, y) == CellType.P2) h += 100 
                            + sum_y(x,y,player) + sum_x(x,y,player);
                    else if (s.getPos(x, y) == CellType.P2Q) h += 150 
                            + sum_y2(x,y,player) + sum_x(x,y,player);
                }
            }
        }
        return h;
    }
    
    /**
     * Retorna un valor segons l'altura per fitxes normals
     *
     * @param x Posició x al tauler
     * @param y Posició y al tauler
     * @param player PlayerType respecte l'heuristica
     * @return valor heuristic
     */
    private int sum_y(int x, int y,PlayerType team){
        int h;
        if (team == PlayerType.PLAYER1){
            switch (y) {
                case 0: h = 1;
                case 1: h= 0;
                case 2:  h=3;
                case 3: h=6;
                case 4: h= 10;
                case 5: h= 15;
                case 6: h= 21;
                case 7: h=28;
                default: h= 0;
            }
            if((y== 0&& x==1)||(y==1 &&x==0))h=+10;

        }
        else{
            switch (y) {
               case 0: h= 28;
               case 1: h= 21;
               case 2: h= 15;
               case 3: h= 10;
               case 4: h= 6;
               case 5: h= 3;
               case 6: h= 0;
               case 7: h= 1;
               default: h= 0;
           }
          if((y== 7&& x==6)||(y==6 &&x==7))h=+10; 
        }
        return h;
    }
    
    /**
     * Retorna un valor segons l'altura per reines
     *
     * @param x Posició x al tauler
     * @param y Posició y al tauler
     * @param player PlayerType respecte l'heuristica
     * @return valor heuristic
     */
    private int sum_y2(int x, int y,PlayerType team){
        int h;
        if (team == PlayerType.PLAYER1){
            switch (y) {
                case 0: h = 28;
                case 1: h= 21;
                case 2:  h=15;
                case 3: h=10;
                case 4: h= 6;
                case 5: h= 3;
                case 6: h= 1;
                case 7: h=0;
                default: h= 0;
            }
            if((y== 0&& x==1)||(y==1 &&x==0))h=+10;

        }
        else{
            switch (y) {
               case 0: h= 0;
               case 1: h= 1;
               case 2: h= 3;
               case 3: h= 6;
               case 4: h= 10;
               case 5: h= 15;
               case 6: h= 21;
               case 7: h= 28;
               default: h= 0;
           }
          if((y== 7&& x==6)||(y==6 &&x==7))h=+10; 
        }
        return h;
    }
    
    /**
     * Retorna un valor segons l'amplada
     *
     * @param x Posició x al tauler
     * @param y Posició y al tauler
     * @param player PlayerType respecte l'heuristica
     * @return valor heuristic
     */
    private int sum_x(int x, int y,PlayerType team){
        int h;
        switch (x) {
            case 0: h = 1;
            case 1: h= 3;
            case 2:  h=6;
            case 3: h=10;
            case 4: h= 15;
            case 5: h= 6;
            case 6: h= 3;
            case 7: h=1;
            default: h= 0;
        }
        return h;
    }
    
    /**
     * Retorna un valor segons els triangles que formen les fitxes
     *
     * @param s Tauler i estat actual de joc.
     * @param player PlayerType respecte l'heuristica
     * @return valor heuristic
     */
    public int count_triangles (GameStatus s, PlayerType player) {
        
        int h=0;
        
        //Recorrem el tauler i per cada fitxa nostra que trobem es suma a h un 
        //valor segons si aquesta fitxa té darrera 1 o 2 fitxes més formant un 
        //triangle o un mig triangle. També es té en compte si té una paret que
        //actua com a protecció també
        for (int y = 0; y < s.getSize(); ++y) {
            for (int x = 0; x < s.getSize(); ++x) { 
                
                if (player == PlayerType.PLAYER1) {
                    if (y > 0) {
                        if (s.getPos(x, y) == CellType.P1 | 
                            s.getPos(x, y) == CellType.P1Q) {
                            
                            h += 10 * get_triangles(s, PlayerType.PLAYER1,
                                                    x, y);
                        }
                    }    
                }
                
                else {
                    if (y < s.getSize()-1) {
                        if (s.getPos(x, y) == CellType.P2 |
                            s.getPos(x, y) == CellType.P2Q) {
                            
                            h += 10 * get_triangles(s, PlayerType.PLAYER2,
                                                    x, y);
                        }
                    }    
                }
            }
        }
        
        return h;
    }
    
    /**
     * Retorna el nombre de 1/2 triangles té una fitxa
     *
     * @param s Tauler i estat actual de joc.
     * @param player PlayerType respecte l'heuristica
     * @param x Posició x al tauler
     * @param y Posició y al tauler
     * @return nombre de 1/2 triangles
     */
    public int get_triangles (GameStatus s, PlayerType player, int x, int y) {
        
        int half_triangle = 0;
        
        if (player == PlayerType.PLAYER1) {
            
            //Si no té una pared a l'esquerra, si té una fitxa del seu equip 
            //sumem 1/2 triangle
            if (x > 0) {
                if (s.getPos(x-1, y-1) == CellType.P1 | 
                    s.getPos(x-1, y-1) == CellType.P1Q) ++half_triangle; 
            }
            
            //Si té una paret també sumem 1/2 triangle
            else ++half_triangle;
            
            //Si no té una pared a la dreta, si té una fitxa del seu equip 
            //sumem 1/2 triangle
            if (x < s.getSize()-1) {
                if (s.getPos(x+1, y-1) == CellType.P1 | 
                    s.getPos(x+1, y-1) == CellType.P1Q) ++half_triangle;
            }
            
            //Si té una paret també sumem 1/2 triangle
            else ++half_triangle;
        }
        
        else {
            
            //Si no té una pared a l'esquerra, si té una fitxa del seu equip 
            //sumem 1/2 triangle
            if (x > 0) {
                if (s.getPos(x-1, y+1) == CellType.P2 | 
                    s.getPos(x-1, y+1) == CellType.P2Q) ++half_triangle; 
            }
            
            //Si té una paret també sumem 1/2 triangle
            else ++half_triangle;
            
            //Si no té una pared a la dreta, si té una fitxa del seu equip 
            //sumem 1/2 triangle
            if (x < s.getSize()-1) {
                if (s.getPos(x+1, y+1) == CellType.P2 | 
                    s.getPos(x+1, y+1) == CellType.P2Q) ++half_triangle;
            }
            //Si té una paret també sumem 1/2 triangle
            else ++half_triangle;
        }
        
        return half_triangle;
    
    }
    
    /**
     * Retorna un valor per cada trap que podem fer
     *
     * @param s Tauler i estat actual de joc.
     * @param player PlayerType respecte l'heuristica
     * @return valor heuristic
     */
    public int trap (GameStatus s, PlayerType player) {
        
        int h = 0;
        
        CellType c1 = CellType.P1;
        CellType c2 = CellType.P1Q;
        
        if (player == PlayerType.PLAYER2) {
            c1 = CellType.P2;
            c2 = CellType.P2Q;
        } 
        
        //Recorrem el tauler i per cada fitxa nostra que trobem cridem a 
        //get_trap
        for (int y = 0; y < s.getSize(); ++y) {
            for (int x = 0; x < s.getSize(); ++x) {
                
                    if (player == PlayerType.PLAYER1 ) {
                        if (y < 5) {
                            if (s.getPos(x, y) == c1) 
                                h += get_trap(s, player, x, y);
                            if (s.getPos(x, y) == c2) 
                                h += get_trap(s, player, x, y);
                        }    
                    }
                    
                    else {
                        if (y > 2) {
                            if (s.getPos(x, y) == c1) 
                                h += get_trap(s, player, x, y);
                            if (s.getPos(x, y) == c2) 
                                h += get_trap(s, player, x, y);
                        }    
                    }       
            }
        }
        
        return h;
    }
    
    /**
     * Retorna un valor depenent de quantes traps podem fer
     *
     * @param s Tauler i estat actual de joc.
     * @param player PlayerType respecte l'heuristica
     * @param x Posició x al tauler
     * @param y Posició y al tauler
     * @return valor heuristic
     */
    public int get_trap(GameStatus s, PlayerType player, int x, int y) {
     
    //Degut la complexitat explicarem el funcionament al document amb imatges
        
        int h = 0;
        
        boolean out = false;
        boolean trap = false;
        
        if (player == PlayerType.PLAYER1) {
            
            if (y > 0) {
                if (x > 0) {
                    if (s.getPos(x-1, y-1) != CellType.P1 
                        & s.getPos(x-1, y-1) != CellType.P1Q) out = true;                  
                } 
            }
            
            if (y > 0) {
                if (x < s.getSize()-3) {
                    if (s.getPos(x+2, y) == CellType.P1 
                        | s.getPos(x+2, y) == CellType.P1Q) {
                        
                        if (s.getPos(x+3, y-1) != CellType.P1 
                            & s.getPos(x+3, y-1) != CellType.P1Q) out = true;
                    }
                }
            }
            
            int x2 = x+1;
            int y2 = y+1;
            
            if (!out) { 
                while (x2 < s.getSize() & y2 < s.getSize() & !out) {
                    
                    if (x2-x == 1) {
                        if (s.getPos(x2, y2) != CellType.EMPTY) out = true; 
                    }
                    if (x2-x == 2) {
                        if (s.getPos(x2, y2) != CellType.P1 
                            & s.getPos(x2, y2) != CellType.P1Q) out = true; 
                    }
                    if (x2-x == 3) {
                        if (s.getPos(x2, y2) == CellType.P2 
                           | s.getPos(x2, y2) == CellType.P2Q) trap = true;
                        out = true;
                    }
                    
                    ++x2;
                    ++y2;
                }
                
                if (trap) {
                    h += 10;
                }
            }
            
            out = false;
            trap = false;
        
            if (y > 0) {
                if (x < s.getSize()-1) {
                    if (s.getPos(x+1, y-1) != CellType.P1 
                        & s.getPos(x+1, y-1) != CellType.P1Q) out = true;                     
                }
            }
            
            if (y > 0) {
                if (x > 2) {
                    if (s.getPos(x-2, y) == CellType.P1 
                        | s.getPos(x-2, y) == CellType.P1Q) {
                        if (s.getPos(x-3, y-1) != CellType.P1 
                            & s.getPos(x-3, y-1) != CellType.P1Q) out = true;
                    }
                }
            }
           
            x2 = x-1;
            y2 = y+1;
            
            if (!out) {
                while (x2 >= 0 & y2 < s.getSize() & !out) {
                    
                    if (x-x2 == 1) {
                        if (s.getPos(x2, y2) != CellType.EMPTY) out = true;
                    }
                    if (x-x2 == 2) {
                        if (s.getPos(x2, y2) != CellType.P1 
                            & s.getPos(x2, y2) != CellType.P1Q) out = true;
                    }
                    if (x-x2 == 3) {
                        if (s.getPos(x2, y2) == CellType.P2 
                           | s.getPos(x2, y2) == CellType.P2Q) trap = true;
                        out = true;
                    }
                    
                    --x2;
                    ++y2;
                }
                
                if (trap) {
                    h += 10;
                }
            }
        } 
        
        else {  

            if (y < s.getSize()-1) {
                if (x > 0) {
                    if (s.getPos(x-1, y+1) != CellType.P2 
                        & s.getPos(x-1, y+1) != CellType.P2Q) out = true;      
                } 
            }
            
            if (y < s.getSize()-1) {
                if (x < s.getSize()-3) {
                    if (s.getPos(x+2, y) == CellType.P2 
                        | s.getPos(x+2, y) == CellType.P2Q) {
                        if (s.getPos(x+3, y+1) != CellType.P2 
                            & s.getPos(x+2, y) != CellType.P2Q) out = true;
                    }
                }
            }
            
            int x2 = x+1;
            int y2 = y-1;
            
            if (!out) {
                while (x2 < s.getSize() & y2 >=0   & !out) {
                    if (x2-x == 1) {
                        if (s.getPos(x2, y2) != CellType.EMPTY)out = true;
                    }
                    if (x2-x == 2) {
                        if (s.getPos(x2, y2) != CellType.P2 
                            & s.getPos(x2, y2) != CellType.P2Q) out = true; 
                    }
                    if (x2-x == 3) {
                        if (s.getPos(x2, y2) == CellType.P1 
                           | s.getPos(x2, y2) == CellType.P1Q) trap = true;
                        out = true;
                    }

                    ++x2;
                    --y2;
                }
                
                if (trap) {
                    h += 10;
                }
            }
            
            out = false;
            trap = false;
            
            if (y < s.getSize()-1) {
                if (x < s.getSize()-1) {
                    if (s.getPos(x+1, y+1) != CellType.P2 
                        & s.getPos(x+1, y+1) != CellType.P2Q) out = true;                    
                } 
            }
            
            if (y < s.getSize()-1 ) {
                if (x > 2) {
                    if (s.getPos(x-2, y) == CellType.P2 
                        | s.getPos(x-2, y) == CellType.P2Q) {
                        if (s.getPos(x-3, y+1) != CellType.P2 
                            & s.getPos(x-3, y+1) != CellType.P2Q) out = true;
                    }
                }
            }
            
            x2 = x-1;
            y2 = y-1;
            
            if (!out) {  
                while (x2 >=0 & y2 >=0   & !out) {
                    if (x-x2 == 1) {
                        if (s.getPos(x2, y2) != CellType.EMPTY)out = true;
                    }
                    if (x-x2 == 2) {
                        if (s.getPos(x2, y2) != CellType.P2 
                            & s.getPos(x2, y2) != CellType.P2Q) out = true; 
                    }
                    if (x-x2 == 3) {
                        if (s.getPos(x2, y2) == CellType.P1 
                           | s.getPos(x2, y2) == CellType.P1Q) trap = true;
                        out = true;
                    }

                    --x2;
                    --y2;
                }
                
                if (trap) {
                    h += 10;
                }
            }
        }
        
        return h;
    }
}    
