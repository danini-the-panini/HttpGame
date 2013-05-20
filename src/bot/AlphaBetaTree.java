package bot;

/**
 * Generic MinMax tree using AlphaBeta pruning. Must be given a Heuristic object to evaluate leaf nodes.
 * @author Daniel
 * @param <B> The board implementation for which given heuristics are defined for.
 */
public class AlphaBetaTree<B extends Board>
{
    private Board.Heuristic heuristic; // hooray for heuristics!

    /**
     * Creates a new AlphaBetaTree.
     * @param heuristic The heuristic to use for evaluating leaf nodes in this tree.
     */
    public AlphaBetaTree(Board.Heuristic<B> heuristic)
    {
        this.heuristic = heuristic;
    }
    
    /**
     * Evaluates a game board using MinMax with AlphaBeta pruning.
     * Evaluates win/loss/draw as INT_MAX/INT_MIN/0 respectively,
     * with adjustments for depth (haven't seen this have much of an effect so far).
     * @param board Game board to evaluate.
     * @param maxDepth Depth limit.
     * @return The best place to move according to the calculations.
     */
    public int[] evaluate(B board, int maxDepth)
    {
        // Get my index (assuming it's my turn)
        int player = board.getCurrentPlayer();
        
        int cvalue; // holds temporary calculated value for comparison
        int value = Integer.MIN_VALUE; // holds the best value found so far.
        int[] choice = null; // holds best move found so far.
        
        int rows = board.getRowCount();
        int cols = board.getColCount();
        
        // go through all the moves and evaluate each one
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
            {
                if (!board.applyMove(i, j)) continue; // attempt to apply the move
                cvalue = alphaBeta(value, Integer.MAX_VALUE, board, 1, maxDepth-1, player);
                board.undoMove(i, j); // undo the move (we don't want to mess up the board now do we?)

                if (cvalue > value)
                {
                    value = cvalue;
                    choice = new int[]{i,j};
                }
            }
        //FOO System.out.println("Choosing: " + value);
        
        return choice;
    }
    
    // recursive helper function to find the value of a particular node using alpha beta pruning
    private int alphaBeta(int alpha, int beta, Board board, int depth, int maxDepth, int player)
    {
        
        String tabs = new String();
        for (int i = 0; i < depth; i++)
            tabs +="    ";
        // base case if the board represents a gameover state.
        if (board.isGameOver())
        {
            //FOO System.out.println(tabs+"WIN?"+board.getWinner());
            int eval = (board.getWinner() == player)
                    ? (Integer.MAX_VALUE - (1 << depth))        // WIN (depth is bad)
                    : ((board.getWinner() < 0)
                        ? 0                                     // DRAW
                        : (Integer.MIN_VALUE + (1 << depth)));  // LOSS (deeper is good)
            String msg = eval < 0 ? "loss" : (eval > 0 ? "win" : "draw");
            //FOO System.out.println(tabs+"T["+msg+"]: " + eval);
            return eval;
        }
        
        // base case if depth limit reached
        if (depth == maxDepth)
        {
            int eval = board.evaluate(heuristic, player);
            //FOO System.out.println(tabs+"D: " + eval);
            return eval;
        }
        
        int rows = board.getRowCount();
        int cols = board.getColCount();
        
        // NOTE: I tried incorporating randomness when choosing between children with equal values,
        // but going with the first occurence of the best value yielded better results.
        // Randomly picking resulted in what seemed to be completely erratic and stupid behaviour,
        // and somehow missed blocking opportunitied, and had no regard for winning.
        
        // NOTE: Possible variation could be traversing differently (instead of top-down,left-right)
        // such as spiral outwards, spiral inwards, or spiral from last move.
        
        // NOTE: Could be made parallel. Not sure how much of a performance increase could be gained from this.
        // One drawback is that alpha and beta should be atomic.
        
        // max
        if (depth % 2 == 0)
        {
            for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
            {
                if (!board.applyMove(i, j)) continue;
                alpha = Math.max(alpha, alphaBeta(alpha, beta, board, depth+1, maxDepth, player));
                board.undoMove(i, j);
                if (beta <= alpha) {
                    //FOO System.out.println(tabs+"A " + alpha + ": (" + beta + "<=" + alpha + ")");
                    return alpha;
                }
            }
            //FOO System.out.println(tabs+"A " + alpha);
            return alpha;
        }
        
        // min
        for (int i = 0; i < rows; i++)
        for (int j = 0; j < cols; j++)
        {
            if (!board.applyMove(i, j)) continue;
            beta = Math.min(beta, alphaBeta(alpha, beta, board, depth+1, maxDepth, player));
            board.undoMove(i, j);
            if (beta <= alpha) {
                //FOO System.out.println(tabs+"B " + beta + ": (" + beta + "<=" + alpha + ")");
                return beta;
            }
        }

        //FOO System.out.println(tabs+"B " + beta);
        return beta;
    }
    
}
