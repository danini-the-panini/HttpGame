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
    public static final int GAME_SIZE = 8;
    
    public static String buildBoard(TTTBoard board)
    {
        StringBuilder sb = new StringBuilder();
        if (board == null)
            sb.append("<div>Board is Null</div>");
        sb.append("<table>");
        for (int i = 0; i < GAME_SIZE; i++)
        {
            sb.append("<tr>");
            for (int j = 0; j < GAME_SIZE; j++)
            {
                String inside = " ";
                String divClass = "open";
                
                if (board != null)
                {
                    switch (board.get(i, j))
                    {
                        case 0:
                            divClass = "black";
                            inside = "X";
                            break;
                        case 1:
                            divClass = "white";
                            inside = "O";
                            break;
                        default:
                            divClass = "open";
                            inside = "-";
                            break;
                    }
                }
                
                sb.append(String.format("<td><button class=\"%s\" onclick=\"board(%d,%d)\">%s</button></td>",
                        divClass, i,j, inside));
            }
            sb.append("</tr>").append(HttpUtils.CRLF);
        }
        sb.append("</table>");
        return sb.toString();
    }
}
