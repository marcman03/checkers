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
public class Heuristica2 {
    
    public Heuristica2 () {
        
    }

    public int getHeuristic(GameStatus s,PlayerType team){
        //System.out.println(""+s.toString());
        int pieces_1 = count_pieces(s,team);
        int pieces_2 = count_pieces(s,PlayerType.opposite(team));
        
        int trap_1 = trap(s, team);
        int trap_2 = trap(s, PlayerType.opposite(team));
        
        int sneak_1 = sneak(s, team);
        int sneak_2 = sneak(s, PlayerType.opposite(team));
        
        int h = pieces_1 - pieces_2
                + trap_1 - trap_2
                + sneak_1 - sneak_2
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
                    if (s.getPos(x, y) == CellType.P1) h += 10 + sum_y(x,y,team) + sum_x(x,y,team);
                    else if (s.getPos(x, y) == CellType.P1Q) h += 50; //TODO: las reinas no dan un buen valor
                }
                else {
                    if (s.getPos(x, y) == CellType.P2) h += 10 + sum_y(x,y,team) + sum_x(x,y,team);
                    else if (s.getPos(x, y) == CellType.P2Q) h += 50;
                }
            }
        }
        return h;
    }
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
    
    public int trap (GameStatus s, PlayerType player) {
        int h = 0;
        
        CellType c1 = CellType.P1;
        CellType c2 = CellType.P1Q;
        
        if (player == PlayerType.PLAYER2) {
            c1 = CellType.P2;
            c2 = CellType.P2Q;
        } 
        
        for (int y = 0; y < s.getSize(); ++y) {
            for (int x = 0; x < s.getSize(); ++x) {
                
                    if (player == PlayerType.PLAYER1 ) {
                        if (y < 5) {
                            if (s.getPos(x, y) == c1) h += comprovate_trap(s, c1, x, y);
                            if (s.getPos(x, y) == c2) h += comprovate_trap(s, c2, x, y);
                        }    
                    }
                    else {
                        if (y > 2) {
                            if (s.getPos(x, y) == c1) h += comprovate_trap(s, c1, x, y);
                            if (s.getPos(x, y) == c2) h += comprovate_trap(s, c2, x, y);
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
        
        if (c == CellType.P1 | c == CellType.P1Q) {
            
            if (y > 0) {
                if (x > 0) {
                    if (s.getPos(x-1, y-1) != CellType.P1 & s.getPos(x-1, y-1) != CellType.P1Q) out = true;      // si la ficha de atras no es del mismo tipo sale               
                } 
            }
            
            if (y > 0) {
                if (x+2 < s.getSize()-1) {
                    if (s.getPos(x+3, y-1) != CellType.P1 & s.getPos(x+3, y-1) != CellType.P1Q) out = true;
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
                    h += 10;
                }
            }
            
            out = false;
            trap = false;
        
            if (y > 0) {
                if (x < s.getSize()-1) {
                    if (s.getPos(x+1, y-1) != CellType.P1 & s.getPos(x+1, y-1) != CellType.P1Q) out = true;                     
                }
            }
            
            if (y > 0) {
                if (x-2 > 0) {
                    if (s.getPos(x-3, y-1) != CellType.P1 & s.getPos(x-3, y-1) != CellType.P1Q) out = true;
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
                    if (s.getPos(x-1, y+1) != CellType.P2 & s.getPos(x-1, y+1) != CellType.P2Q) out = true;      // si la ficha de atras no es del mismo tipo sale               
                } 
            }
            
            if (y < s.getSize()-1) {
                if (x+2 < s.getSize()-1) {
                    if (s.getPos(x+3, y+1) != CellType.P2 & s.getPos(x+3, y+1) != CellType.P2Q) out = true;
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
                    if (s.getPos(x+1, y+1) != CellType.P2 & s.getPos(x+1, y+1) != CellType.P2Q) out = true;      // si la ficha de atras no es del mismo tipo sale               
                } 
            }
            
            if (y < s.getSize()-1 ) {
                if (x-2 > 0) {
                    if (s.getPos(x-3, y+1) != CellType.P2 & s.getPos(x-3, y+1) != CellType.P2Q) out = true;
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
    
    public int sneak (GameStatus s, PlayerType player) {
        int h = 0;
        
        
        for (int y = 0; y < s.getSize(); ++y) {
            for (int x = 0; x < s.getSize(); ++x) {
                if (s.getPos(x, y) == CellType.P1Q | s.getPos(x, y) == CellType.P2Q) {
                    CellType c = s.getPos(x, y);
                    h += check_sneak(s, c, x, y);
                }    
            }
        }
        return h;
    }
    
    public int check_sneak (GameStatus s, CellType c, int x, int y) {
        int h = 0;
        
        if (c == CellType.P1Q) {
            if (y > 1) {
                if (x > 1) {
                    if (s.getPos(x-1, y-1) == CellType.P2) h += 10;
                }
                if (x < s.getSize()-2) {
                    if (s.getPos(x+1, y-1) == CellType.P2) h += 10;
                }
            }
        }
        else {
            if (y < s.getSize()-2) {
                if (x > 1) {
                    if (s.getPos(x-1, y+1) == CellType.P2) h += 10;
                }
                if (x < s.getSize()-2) {
                    if (s.getPos(x+1, y+1) == CellType.P2) h += 10;
                }
            }
        }
        
        return h;
    }
}    
