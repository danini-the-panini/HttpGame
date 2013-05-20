package bot;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * Abstract "input provider" for a game.
 * An input provider can be anything, from a heuristic based automation to a user operated terminal to standard IO.
 * Heck, go ahead an make an SMTP-based input provider to play over email, I dare you...
 * @author Daniel
 * @param <B> The game board for which the input provider is to be used with.
 */
public abstract class InputProvider<B extends Board>
{
    /**
     * Gets a move from the input provider. Do not assume the move is valid (i.e. you must check with your board if it is).
     * @param b The board which the move should be for.
     * @return An {x,y} of the move to be played by this input provider.
     */
    public abstract int[] getMove(B b); 
    /**
     * Send a textual message to this provider.
     * @param message The message to send.
     */
    public abstract void sendMessage(String message);
    /**
     * Sends the board to this provider.
     * @param b The board object to be sent.
     */
    public abstract void sendBoard(B b);
    
    /** Clears the input providers terminal screen (if the input provider is a user-operated terminal) */
    public void clearOutput() {}

    /**
     * Heuristic-based automated input provider using a MinMax tree with AlphaBeta pruning.
     */
    public static class AlphaBetaBot<B extends Board> extends InputProvider<B>
    {
        private int maxDepth;
        
        private AlphaBetaTree abTree;

        /**
         * Creates a new AlphaBetaBot.
         * @param heuristic The heuristic to use in evaluating leaf nodes in the tree.
         * @param maxDepth Depth limit for the tree.
         */
        public AlphaBetaBot(Board.Heuristic<B> heuristic, int maxDepth)
        {
            this.abTree = new AlphaBetaTree(heuristic);
            this.maxDepth = maxDepth;
        }
        
        @Override
        public int[] getMove(B b)
        {
            return abTree.evaluate(b,maxDepth);
        }

        @Override
        public void sendMessage(String message)
        {
            // this bot does not care
        }
        
        @Override
        public void sendBoard(B b)
        {
            // this bot does not care
        }
    }
    
    /**
     * A stream-based input provider for the TicTacToe game implementation.
     */
    public static class TTTPlayer extends InputProvider<TTTBoard>
    {
        private BufferedReader in;
        private PrintStream out;
        
        /**
         * Creates a new TTTPlayer
         * @param in The InputStream from which input will be obtained.
         * @param out The PrintStream to which output will be printed.
         */
        public TTTPlayer(InputStream in, PrintStream out)
        {
            this.in = new BufferedReader(new InputStreamReader(in));
            this.out = out;
        }

        @Override
        public int[] getMove(TTTBoard b)
        {
            int[] yourMove;
            
            try
            {
                out.print(" Input row col> ");
                String[] line = in.readLine().split(" ");
                if (line.length != 2) yourMove = null;
                else yourMove = new int[]{Integer.valueOf(line[0]), Integer.valueOf(line[1])};
            }
            catch (Exception e)
            {
                return null;
            }
                
            return yourMove;
        }

        @Override
        public void sendMessage(String message)
        {
            out.println(message);
        }
        
        @Override
        public void sendBoard(TTTBoard b)
        {
            out.println();
            b.printBoard(out);
            out.println();
        }
    }
}
