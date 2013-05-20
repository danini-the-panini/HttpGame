package utils;

import bot.Board;
import bot.TTTBoard;
import httpgame.Player;

/**
 *
 * @author daniel
 */
public class GameUtils
{
    public static final int GAME_SIZE = 8, GAME_GOAL = 4, PLY_DEPTH = 4;
    
    public static String buildPage(Player player)
    {
        
        return getTitle(player) + "\r\n" + buildBoard(player.getBoard());
    }
    
    public static String buildBoard(TTTBoard board)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("<table>");
        for (int i = 0; i < GAME_SIZE; i++)
        {
            sb.append("<tr>");
            for (int j = 0; j < GAME_SIZE; j++)
            {
                String divClass = "open";
                
                if (board != null)
                {
                    switch (board.get(i, j))
                    {
                        case 0:
                            divClass = "black";
                            break;
                        case 1:
                            divClass = "white";
                            break;
                        default:
                            divClass = "open" + (board.getCurrentPlayer()==0 ? "Black" : "White");
                            break;
                    }
                }
                
                sb.append(String.format("<td><div class=\"%s\" id=\"%d,%d\""
                        + " onclick=\"board(%d,%d)\"></div></td>",
                        divClass, i, j, i, j));
            }
            sb.append("</tr>").append(HttpUtils.CRLF);
        }
        sb.append("</table>");
        return sb.toString();
    }
    
    public static String getTitle(Player player)
    {
        TTTBoard board = player.getBoard();
        if (board != null && board.isGameOver())
        {
            String h1;
            if (board.getWinner() == Board.DRAW)
                h1 = "<h1 class=\"draw\">Game was a draw</h1>";
            else if (board.getWinner() == player.getPlayerNum())
                h1 = "<h1 class=\"win\">You won!</h1>";
            else
                h1 = "<h1 class=\"lose\">You failed!</h1>";
            return h1;
        }
        return "<h1 class=\"title\">Daniel and J's: Gomoku -1</h1>";
    }
}
