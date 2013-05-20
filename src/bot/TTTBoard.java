package bot;



import java.io.PrintStream;

/**
 * Implementation of a TicTacToe game board, but allows for any board size.
 * @author Daniel
 */
public class TTTBoard extends Board
{
    /** Player one (X) and two (O) pieces */
    public static final char[] pieces = {'X','O'};
    
    /**
     * Converts a player index into a piece char.
     * @param i
     * @return 
     */
    public static char i2c(int i)
    {
        if (i < 0) return ' ';
        if (i >= pieces.length) return '#'; // shouldn't happen. if you see a '#' in your game board let me know!
        return pieces[i];
    }
    
    private int[][] board; // actual board data
    private int winner = NO_WINNER; // who is winning?
    private int current = 0; // who's turn is it?
    private int goal; // how many pieces in a row to get?
    private int total = 0;
    
    /**
     * Creates a new game board.
     * @param rows Number of rows in board.
     * @param cols Number of columns in board.
     * @param goal The number of pieces to get in a row to win.
     */
    public TTTBoard(int rows, int cols, int goal)
    {
        this.goal = Math.min(goal, Math.min(rows, cols));
        board = new int[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                board[i][j] = E;
    }

    /**
     * 
     * @return The goal of this TTT board game
     */
    public int getGoal()
    {
        return goal;
    }

    @Override
    public int getRowCount()
    {
        return board.length;
    }

    @Override
    public int getColCount()
    {
        return board[0].length;
    }

    /**
     * Gets a value on the board.
     * @param row
     * @param col
     * @return The value at [row,col]
     */
    public int get(int row, int col)
    {
        return board[row][col];
    }

    @Override
    public int getCurrentPlayer()
    {
        return current;
    }

    @Override
    public void nextPlayer()
    {
        current = 1-current; // two player game...
    }
    @Override
    public void previousPlayer()
    {
        nextPlayer(); // two player game means no difference between next and previous player
    }

    @Override
    public int getNumMoves()
    {
        return (board.length*board[0].length)-total;
    }

    @Override
    public boolean valid(int row, int col)
    {
        return row >= 0 && col >= 0
                && row < getRowCount() && col < getColCount()
                && board[row][col] == E;
    }
    
    @Override
    public boolean undoMove(int row, int col)
    {
        if (row < 0 || col < 0
                || row >= getRowCount() || col >= getColCount()
                || board[row][col] == E) return false;
        board[row][col] = E;
        total--;
        winner = NO_WINNER;
        previousPlayer();
        return true;
    }
    
    @Override
    public boolean applyMove(int row, int col)
    {
        if (!valid(row,col)) return false;
        place(row,col);
        nextPlayer();
        return true;
    }
    
    // helper function for placing. Checks winner.
    private void place(int row, int col)
    {
        // set board
        board[row][col] = current;
        total++;
        
        // check for win
        int n = 1;
        for (int i = row+1; i < board.length; i++)
            if (board[i][col] != current) break;
            else n++;
        for (int i = row-1; i >= 0; i--)
            if (board[i][col] != current) break;
            else n++;
        if (n >= goal)
        {
            winner = current;
            return;
        }
        n = 1;
        for (int j = col+1; j < board[0].length; j++)
            if (board[row][j] != current) break;
            else n++;
        for (int j = col-1; j >= 0; j--)
            if (board[row][j] != current) break;
            else n++;
        if (n >= goal)
        {
            winner = current;
            return;
        }
        n = 1;
        for (int i = row+1, j = col+1; i < board.length && j < board[0].length; j++, i++)
            if (board[i][j] != current) break;
            else n++;
        for (int i = row-1, j = col-1; i >= 0 && j >= 0; j--, i--)
            if (board[i][j] != current) break;
            else n++;
        if (n >= goal)
        {
            winner = current;
            return;
        }
        n = 1;
        for (int i = row-1, j = col+1; i >= 0 && j < board[0].length; j++, i--)
            if (board[i][j] != current) break;
            else n++;
        for (int i = row+1, j = col-1; i < board.length && j >= 0; j--, i++)
            if (board[i][j] != current) break;
            else n++;
        if (n >= goal)
        {
            winner = current;
            return;
        }
        
        if (total == board.length*board[0].length) winner = DRAW; // draw
    }
    
    @Override
    public int getWinner()
    {
        return winner;
    }

    @Override
    public boolean isGameOver()
    {
        return winner > NO_WINNER;
    }
    
    @Override
    public String toString()
    {
        String str = "";
        for (int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board[0].length; j++)
            {
                str += " "+(board[i][j]+1);
            }
            str+=",";
        }
        return str;
    }
    
    @Override
    public void printBoard(PrintStream out)
    {
        out.print("   ");
        for (int j = 0; j < board.length; j++)
                out.print((j > 9 ? "" : " ") + j + "  ");
        out.println();
        out.print("  +");
        for (int j = 0; j < board.length; j++)
            out.print("---+");
        out.println();
        
        for (int i = 0; i < board.length; i++)
        {
            out.print("" + i + (i > 9 ? "" : " ") + "|");
            for (int j = 0; j < board[0].length; j++)
            {
                out.print(" "+i2c(board[i][j])+" |");
            }
           /* if (i == board.length-1)
            {
                out.print("\tworth: "+evaluate()+" GP");
            }*/
            out.println();
            out.print("  +");
            for (int j = 0; j < board.length; j++)
                out.print("---+");
            out.println();
        }
    }
    
}
