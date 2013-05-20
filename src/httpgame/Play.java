/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package httpgame;

import bot.TTTBoard;
import java.util.Date;
import java.util.UUID;
import static utils.ConsoleUtils.*;
import utils.GameUtils;
import static utils.HttpUtils.getPlayer;

/**
 *
 * @author daniel
 */
public class Play extends ClientHandler.Executor
{

    @Override
    public void run(HttpRequest request, HttpResponse response)
    {
        println("Hello World!");
        
        Player player = getPlayer(request, response);
        
        String rowStr = request.getParameterValue("row");
        String colStr = request.getParameterValue("col");
        
        try
        {
            int row = Integer.parseInt(rowStr);
            int col = Integer.parseInt(colStr);
            
            TTTBoard board = player.getBoard();
            
            if (board != null && board.getCurrentPlayer() == player.getPlayerNum())
            {
                board.applyMove(row, col);
                player.botMove();
            }

            response.setStatus(200);
            response.setContent(GameUtils.buildBoard(board).getBytes());
        }
        catch (Exception ex)
        {
            response.setStatus(500);
            //response.setContent(ex.toString().getBytes());
        }
    }
    
}
