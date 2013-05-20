package bot;



import java.io.PrintStream;

/**
 * Abstract "grid-based" board game. Could theoretically implement any 2D grid-based board game from this interface.
 * However for the project game "Bao" may require some extra stuff (like moves have more than just x and y)
 * @author Daniel
 */
public abstract class Board
{
    
    // represents an empty block in the game board.
    public static final int E = -1;
    
    // represents the overall game states. NO_WINNER means the game has not been completed
    public static final int NO_WINNER = -2, DRAW = -1;
    
    /**
     * Interface for defining a heuristic.
     * @param <B> The implementation of the board game this heuristic is defined for.
     */
    public static interface Heuristic<B extends Board>
    {
        public int evaluate(B board, int player);
    }
    
    /**
     * @return The index of the player who is currently in play.
     */
    public abstract int getCurrentPlayer();
    /**
     * Advances the game to the next player whose turn it is to play.
     */
    public abstract void nextPlayer();
    /**
     * Reverts the game to previous player who was in play.
     */
    public abstract void previousPlayer();
    /**
     * @return The index of the player who has won the game, if any, otherwise returns NO_WINNER if the game is not over, or DRAW if the game was a draw.
     */
    public abstract int getWinner();
    
    /**
     * Evaluates the current board state based on the given heuristic.
     * @param heuristic The heuristic to use to evaluate the game state.
     * @param player The player for whom the heuristic should evaluate in favour for.
     * @return An integer value representing the favourability of the current board state for the specified player.
     */
    public int evaluate(Heuristic heuristic, int player)
    {
        return heuristic.evaluate(this, player);
    }
    
    /**
     * 
     * @return The number of rows in the game board.
     */
    public abstract int getRowCount();
    /**
     * @return The number of columns in the game board.
     */
    public abstract int getColCount();
    
    /**
     * @return The total number of moves available to the player. If this is 0 then the game has reached an end.
     */
    public abstract int getNumMoves();
    /**
     * The current player makes a move at the specified location on the board.
     * @param row The row to apply the move to.
     * @param col The column to apply the move to.
     * @return True if the move was successful, false otherwise. If false is returned it should be assumed the game state was not changed.
     */
    public abstract boolean applyMove(int row, int col);
    /**
     * Undoes a move at a specified location on the board.
     * @param row The row where the move should be undone.
     * @param col The column where the move should be undone.
     * @return True if the undo was successful, false otherwise. If false is returned it should be assumed the game state was not changed.
     */
    public abstract boolean undoMove(int row, int col);
    /**
     * Checks if the current player may apply a move at a specified location on the board. This function should not modify the game state.
     * @param row The row where the move should be validated.
     * @param col The column where the move should be validated.
     * @return True if the current player may apply a move at the location, false otherwise.
     */
    public abstract boolean valid(int row, int col);
    
    /**
     * @return True if the game is over, false otherwise.
     */
    public abstract boolean isGameOver();
    
    /**
     * Prints a textual representation of the board.
     * @param out The PrintStream to which the board should be printed.
     */
    public abstract void printBoard(PrintStream out);
}
