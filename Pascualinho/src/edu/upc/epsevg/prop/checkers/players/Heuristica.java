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
 *
 * @author ivanciu
 */
public class Heuristica {
    
    public Heuristica () {
        
    }

    public int getHeuristic(GameStatus s,PlayerType team){
        //System.out.println(""+s.toString());
        
        int pieces_1 = count_pieces(s,team);
        int pieces_2 = count_pieces(s,PlayerType.opposite(team));
        
        int triangles_1 = count_triangles(s, team);
        int triangles_2 = count_triangles(s, PlayerType.opposite(team));
        
        int trap_1 = trap(s, team);
        int trap_2 = trap(s, PlayerType.opposite(team));
        
        int h = pieces_1 - pieces_2
                + triangles_1 - triangles_2
                + trap_1 - trap_2
                ;
     
        //System.out.println("Heuristica: " + h);
        //System.out.println("Peces1: " + pieces_1);
        //System.out.println("Peces2: " + pieces_2);
        //System.out.println("Triangles1: " + triangles_1);
        //System.out.println("Triangles2: " + triangles_2);
        //System.out.println("Trap1: " + trap_1);
        //System.out.println("Trap2: " + trap_2);
        //System.out.println("==================================00");
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
                            h += 5 * comprovate_triangle(s, CellType.P1, x, y);
                        }
                    }    
                }
                else {
                    if (y < s.getSize()-1) {
                        if (s.getPos(x, y) == CellType.P2) {
                            h += 5 * comprovate_triangle(s, CellType.P2, x, y);
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
            
            if (y > 0) {
                if (x+2 < s.getSize()-1) {
                    if (s.getPos(x+3, y-1) != c) out = true;
                }
            }
            
            int x2 = x+1;
            int y2 = y+1;
            if (!out) { 
                while (x2 < s.getSize() & y2 < s.getSize() & !out) {
                    
                    if (x2-x == 1) {
                        if (s.getPos(x2, y2) != CellType.EMPTY) out = true; // una ficha alante tiene que ser espacio para seguir
                    }
                    if (x2-x == 2) {
                        if (s.getPos(x2, y2) != CellType.P1) out = true; // 2 fichas alante que ser del mismo equipo para seguir
                    }
                    if (x2-x == 3) {
                        if (s.getPos(x2, y2) == CellType.P2) trap = true;//3 fichas alante tiene que ser enemiga para trampa
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
                    if (s.getPos(x+1, y-1) != c) out = true;                     
                }
            }
            
            if (y > 0) {
                if (x-2 > 0) {
                    if (s.getPos(x-3, y-1) != c) out = true;
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
                    h += 10;
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
            
            if (y < s.getSize()-1) {
                if (x+2 < s.getSize()-1) {
                    if (s.getPos(x+3, y+1) != c) out = true;
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
                    h += 10;
                }
            }
            
            out = false;
            trap = false;
            
            if (y < s.getSize()-1) {
                if (x < s.getSize()-1) {
                    if (s.getPos(x+1, y+1) != c) out = true;      // si la ficha de atras no es del mismo tipo sale               
                } 
            }
            
            if (y < s.getSize()-1 ) {
                if (x-2 > 0) {
                    if (s.getPos(x-3, y+1) != c) out = true;
                }
            }
            
            x2 = x-1;
            y2 = y-1;
            
            if (!out) {  
                while (x2 >=0 & y2 >=0   & !out) {
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
                    h += 10;
                }
            }
        }
        
        return h;
    }
}    
