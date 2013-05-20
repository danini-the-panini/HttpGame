package bot;


import java.util.Scanner;

/**
 * Main entry point for testing TicTacToe. Command line interface. Plays against "bot" of varying difficulty on a specified board.
 * @author Daniel
 */
public class Main
{
    
    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        
        System.out.print("Input width height: ");
        int width = in.nextInt(), height = in.nextInt();
        System.out.print("Input goal: ");
        int goal = in.nextInt();
        
        int result, youWon = 0, iWon = 0, draw = 0;
        do
        {
            Board board = new TTTBoard(width, height, goal);

            do
            {
                System.out.println("Choose Difficulty");
                System.out.println("1. Easy");
                System.out.println("2. Medium");
                System.out.println("3. Hard");
                System.out.println("4. V. Hard");
                System.out.print("> ");

                result = -1;
                try {
                    result = in.nextInt();
                } catch (NumberFormatException e) { }

            }
            while (result <= 0 || result > 4);

            InputProvider[] players = new InputProvider[2];

            int you = (int)(Math.random()*2);

            int plyDepth = result+1;
            
            players[you] = new InputProvider.TTTPlayer(System.in,System.out);
            players[1-you] = new InputProvider.AlphaBetaBot(new MyTTTHeuristic(), plyDepth);

            System.out.println((you == 0 ? "You" : "I") + " start first.");

            // COMMENCE GAME LOOP!
            while (!board.isGameOver())
            {
                int current = board.getCurrentPlayer();

                int[] move;
                
                    players[you].sendBoard(board);

                    if (current != you)
                        System.out.println("Waiting for other player...");

                do
                {
                    move = players[current].getMove(board);
                    if (move == null || !board.valid(move[0], move[1]))
                        System.out.println("Invalid move!");
                }
                while (move == null || !board.valid(move[0], move[1]));

                board.applyMove(move[0], move[1]);
            }

            players[you].sendBoard(board);

            result = board.getWinner();
            if (result == -1) {
                System.out.println("GAME WAS A DRAW.");
                draw++;
            } else if (result == you) {
                System.out.println("YOU WON!");
                youWon++;
            } else {
                System.out.println("YOU LOST...");
                iWon++;
            }

            do
            {
                System.out.print("Would you like to play again? [y/n] ");

                result = Character.toUpperCase(in.next().charAt(0));
            }
            while (result != 'N' && result != 'Y');
        }
        while (result != 'N');

        System.out.println("TOTAL : " + (youWon+iWon+draw));
        System.out.println("WINS  : " + youWon);
        System.out.println("LOSSES: " + iWon);
        System.out.println("DRAWS : " + draw);
    }
}
