/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package httpgame;

import bot.TTTBoard;
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
            
            if (board != null)
            {
                if (board.isGameOver())
                {
                    //player.endGame();
                }
                else if (board.getCurrentPlayer() == player.getPlayerNum())
                {
                    if (board.valid(row, col))
                    {
                        if (board.applyMove(row, col) && !board.isGameOver())
                            player.botMove();
                    }
                }
            }

            response.setStatus(200);
            response.setContent(GameUtils.buildPage(player).getBytes());
        }
        catch (Exception ex)
        {
            println(ex.toString());
            ex.printStackTrace(System.err);
            response.setStatus(500);
            //response.setContent(ex.toString().getBytes());
        }
    }
    
}
