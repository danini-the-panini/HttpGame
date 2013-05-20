package httpgame;

import java.util.UUID;

/**
 *
 * @author u11306026
 */
public class Player {
    
    public static final int NON_EXISTANT = 0,
            REGISTERED = 1,
            WAITING_FOR_GAME = 2,
            WAITING_FOR_TURN = 3,
            PLAYING = 4,
            ENDGAME = 5;
    
    private int state;

    public Player() {
        state = NON_EXISTANT;
    }
    
    
            
    
}
