
package edu.upc.epsevg.prop.checkers.players;

import edu.upc.epsevg.prop.checkers.CellType;
import edu.upc.epsevg.prop.checkers.GameStatus;
import edu.upc.epsevg.prop.checkers.PlayerMove;
import edu.upc.epsevg.prop.checkers.PlayerType;
import edu.upc.epsevg.prop.checkers.SearchType;
import java.awt.Point;
import java.util.List;
import java.util.Random;

/**
 * ElMeuStatus
 * @author Marc Pascual Ivan Garcia
 */
public class ElMeuStatus extends GameStatus {
    
    private static final long[][][] zobristTable = new long[GameStatus.N][GameStatus.N][4];
    private static final long zobristTableDiferenciador;
    private long hash;
    
    static {
        
        // Inicialitzar la tabla Zobrist amb valors aleatoris
        Random random = new Random();
        
        for (int i = 0; i < GameStatus.N; ++i) {
            for (int j = 0; j < GameStatus.N; ++j) {
                for (int k = 0; k < 4; ++k) {
                    zobristTable[i][j][k] = random.nextLong();
                }
            }
        }
        zobristTableDiferenciador = random.nextLong();

    }
    
    /**
    * 
    * @param tauler Matriu d'ints
    */
    public ElMeuStatus(int [][] tauler){
        
        super(tauler);
       
    }
    
    /**
    * 
    * @param gs Tauler i estat actual de joc.
    */
    public ElMeuStatus(GameStatus gs){
         
        super(gs);
        hash= CalculateHash();
        
    }
     
    /**
    *
    * 
    * @param move Llista de punts
    */
    @Override
    public void movePiece(List<Point> move) {
        
        updateHash(move);
        super.movePiece(move);
        
    }
    
    /**
    * Actualitza el hash del tauler després de fer un moviment. 
    * 
    * @param move Llista de punts
    */
    private void updateHash(List<Point>move){
        
        Point firstPoint=move.get(0);
        for(int i=0; i<move.size();++i){
            Point currentPoint=move.get(i);
            int x= currentPoint.x;
            int y= currentPoint.y;
            Point capture=null;
            Point nextPoint=null;
            int nextX=0;
            int nextY=0;
            if (i< move.size()-1){
                nextPoint =move.get(i+1);
                nextX= nextPoint.x;
                nextY= nextPoint.y;
                capture= detectCapture(x,y,nextX,nextY);
            }
            if (nextPoint!=null){
                hash ^=zobristTable[x][y][getPieceIndex(
                                getPos(firstPoint.x, firstPoint.y))];
                hash ^=zobristTable[nextX][nextY][getPieceIndex(
                                getPos(firstPoint.x, firstPoint.y))];
                
                //Si existeix una captura
                if (capture!= null){
                    hash ^=zobristTable[capture.x][capture.y][getPieceIndex(
                                     getPos(capture.x, capture.y))];
                }       
            }
        }
    }
    
    /**
    * Retorna el punt on es capturat una ficha tenint una posicio inicial x y i 
    * una final nextX nextY
    * Si no hi ha cap captura retorna null
    * 
    * @param x Posició x al tauler
    * @param y Posició y al tauler
    * @param nextx Posició següent al tauler
    * @param nexty Posició següent al tauler
    */
    
    private Point detectCapture(int x, int y, int nextX, int nextY){
        
        if (Math.abs(nextX - x) == 2 && Math.abs(nextY - y) == 2) {
            
            int capturedX = (x +nextX) / 2;
            int capturedY = (y + nextY) / 2;
            
            return(new Point(capturedX,capturedY));
        }
        
        return null;
    }
    
    /**
    * Retorna segons la fitxa un index
    * 
    * @param type tipus de fitxa
    */
    private int getPieceIndex(CellType type) {
        
        if (type == CellType.P1) return 0;
            
        else if (type == CellType.P1Q) return 1;
            
        else if (type == CellType.P2) return 2;
            
        else if (type == CellType.P2Q) return 3;
            
        return -1; // Valor per defecto o error.
        
    }
    
    /**
    * Retorna el hash d'un tauler 
    * 
    */
    private long CalculateHash() {
        
        long h = 0;
        
        for (int y = 0; y < getSize(); ++y) {
            for (int x = 0; x < getSize(); ++x) {
                
                CellType type = getPos(x, y);
               
                if (type != CellType.EMPTY) {
                    
                    h ^= zobristTable[x][y][getPieceIndex(type)];
                    
                }
            }
        }
        
        return h;
    }
    
    /**
    * Retorna el hash d'un tauler diferenciant players 
    * 
    */
    public long getHash(){
        
        if (this.getCurrentPlayer() == PlayerType.PLAYER1) 
            return hash^ zobristTableDiferenciador;
        return hash;
    
    }
}
