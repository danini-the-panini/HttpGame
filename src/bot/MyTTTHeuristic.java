package bot;



/**
 * This class defines the heuristic I decided to use to evaluate TicTacToe game states.
 * @author Daniel
 */
public class MyTTTHeuristic implements Board.Heuristic<TTTBoard>
{
    @Override
    public int evaluate(TTTBoard board, int player)
    {
        // TODO: perhaps these should be kept a global somewhere?
        // I think function calls are too expensive to be called in an heuristic. (gets called A LOT for large boards)
        int goal = board.getGoal();
        int rows = board.getRowCount();
        int cols = board.getColCount();
        
        int eval = 0;
        
        // Go through the entire board block by block and evaluate each "line" (see evalLine function below)
        // right, down, diagonal up, diagonal down, adding up each line as it goes.
        // fairly informed. on a 10x10 board ply depth of 5 takes up to 5 seconds,
        // which is reasonable, considering it is mildly challenging sometimes.
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                if (board.get(i,j) != TTTBoard.E)
                {
                    int[] line = new int[goal];
                    int ee; // temp variable for debug.
                    
                    if (i < rows-goal)  // horizontal line
                    {
                        for (int k = 0; k < goal; k++)
                        {
                            line[k] = board.get(i+k,j);
                        }
                        ee = evalLine(line, player);
                        eval+=ee;
                        //FOO System.out.println("=" + ee);
                    }
                    if (j < cols-goal+1) // vertical line
                    {
                        for (int k = 0; k < goal; k++)
                        {
                            line[k] = board.get(i,j+k);
                        }
                        ee = evalLine(line, player);
                        eval+=ee;
                        //FOO System.out.println("=" + ee);
                    }
                    if (i < rows-goal+1 && j < cols-goal+1) // diagonal line down
                    {
                        for (int k = 0; k < goal; k++)
                        {
                            line[k] = board.get(i+k,j+k);
                        }
                        ee = evalLine(line, player);
                        eval+=ee;
                        //FOO System.out.println("=" + ee);
                    }
                    if (i < rows-goal+1 && j >= goal-1) // diagonal line up
                    {
                        for (int k = 0; k < goal; k++)
                        {
                            line[k] = board.get(i+k,j-k);
                        }
                        ee = evalLine(line, player);
                        eval+=ee;
                        //FOO System.out.println("=" + ee);
                    }
                }
            }
        }
        
        return eval;
    }

    // a^b helper function
    private int pow(int a, int b)
    {
        int result = 1;
        for (int i = 0; i < b; i++)
        {
            result *= a;
        }
        return result;
    }
    // evaluate a "line". How it works is if there are x > 0 number of my pieces in the row and no other pieces then score 10^(x-1),
    // on the other hand, if there are x > 0 number of my opponent's pieces and no other pieces then score -11^(x-1) (overcompensate loss so to enforce defensive strategy)
    // all other situations lead to 0
    private int evalLine(int[] line, int player)
    {
        //FOO System.out.print("eval{"+i2c(player)+"}[" + line.length + "]|");
        //FOO for (int p :line)
        //FOO     System.out.print(" " + i2c(p) + " |");
        int good = 0, bad = 0;
        for (int i = 0; i < line.length; i++)
        {
            if (line[i] == player) good++;
            else if (line[i] != TTTBoard.E) bad++;
        }
        if (bad == 0 && good > 0)
        {
            return pow(10,good-1);
        }
        if (good == 0 && bad > 0)
        {
            return -pow(11,bad-1);
        }
        return 0;
    }
}
