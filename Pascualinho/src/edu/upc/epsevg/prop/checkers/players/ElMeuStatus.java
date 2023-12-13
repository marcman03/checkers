
package edu.upc.epsevg.prop.checkers.players;

import edu.upc.epsevg.prop.checkers.CellType;
import edu.upc.epsevg.prop.checkers.GameStatus;
import edu.upc.epsevg.prop.checkers.PlayerType;
import java.util.Random;

/**
 *
 * @author Usuari
 */
public class ElMeuStatus extends GameStatus {
    private static final long[][][] zobristTable = new long[GameStatus.N][GameStatus.N][4];
    private static long diferenciador;
    static {
        // Inicializar la tabla Zobrist con valores aleatorios
        Random random = new Random();
        diferenciador =random.nextLong();
        for (int i = 0; i < GameStatus.N; ++i) {
            for (int j = 0; j < GameStatus.N; ++j) {
                for (int k = 0; k < 4; ++k) {
                    zobristTable[i][j][k] = random.nextLong();
                }
            }
        }
    }
    public ElMeuStatus(int [][] tauler){
        super(tauler);
    }
    
     public ElMeuStatus(GameStatus gs){
        super(gs);
        
    }
     
    
    public long getHash(PlayerType player) {
        long h = 0;
        for (int y = 0; y < getSize(); ++y) {
            for (int x = 0; x < getSize(); ++x) {
                CellType type = getPos(x, y);
               
                if (type != CellType.EMPTY) {
                    int pieza=0;
                    if (type == CellType.P1) {
                        pieza = 0;
                    } else if (type == CellType.P1Q) {
                        pieza = 1;
                    } else if (type == CellType.P2) {
                        pieza = 2;
                    } else if (type == CellType.P2Q) {
                        pieza = 3;
                    }
                    h ^= zobristTable[x][y][pieza];
                    
                   
                    
                }
            }
        }
        if (player == PlayerType.PLAYER1) {
            h ^= diferenciador;
        }
        return h;
    }
    
    
}
