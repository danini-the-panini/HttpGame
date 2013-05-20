package httpgame;

import bot.AlphaBetaTree;
import bot.MyTTTHeuristic;
import bot.TTTBoard;

/**
 *
 * @author u11306026
 */
public class Player {
    
    private TTTBoard board;
    private AlphaBetaTree<TTTBoard> abtree;
    private int depth;
    
    public void startGame(int size, int goal, int depth)
    {
        this.depth = depth;
        
        board = new TTTBoard(size, size, goal); 
        abtree = new AlphaBetaTree<TTTBoard>(new MyTTTHeuristic());
        
        if (Math.random() > 0.5) board.nextPlayer();
        
        if (board.getCurrentPlayer() == 1) // bot starts first
        {
            botMove();
        }
    }
    
    public void botMove()
    {
        int[] move = abtree.evaluate(board, depth);
        board.applyMove(move[0], move[1]);
    }

    public TTTBoard getBoard()
    {
        return board;
    }
    
}
