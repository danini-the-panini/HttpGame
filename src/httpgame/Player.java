package httpgame;

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

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state%6;
    }
    
    public void nextState()
    {
        setState(state+1);
    }
    
}
