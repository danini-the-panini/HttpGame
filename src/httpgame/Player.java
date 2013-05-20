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
    private AlphaBetaTree<TTTBoard> abtree = new AlphaBetaTree<TTTBoard>(new MyTTTHeuristic());
    private int depth;
    private int playerNum;
    
    public void startGame(int size, int goal, int depth)
    {
        this.depth = depth;
        
        board = new TTTBoard(size, size, goal);
        
        playerNum = (Math.random() > 0.5) ? 0 : 1;
        
        if (!board.isGameOver() && board.getCurrentPlayer() != playerNum)
        {
            botMove();
        }
    }
    
    public void endGame()
    {
        board = null;
    }

    public int getPlayerNum()
    {
        return playerNum;
    }
    
    public void botMove()
    {
        int[] move = abtree.evaluate(board, depth);
        if (board.valid(move[0],move[1]))
                board.applyMove(move[0], move[1]);
    }

    public TTTBoard getBoard()
    {
        return board;
    }
    
}
