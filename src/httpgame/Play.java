/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package httpgame;

import java.util.Date;
import java.util.UUID;
import static utils.ConsoleUtils.*;
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
                    
        UUID uuid;
        String uuidStr = request.getCookie("uuid");
        if (uuidStr != null) uuid = UUID.fromString(uuidStr);
        else uuid = UUID.randomUUID();

        Player player = getPlayer(uuid);
        
        response.setCookie("uuid", uuid.toString(), new Date(Long.MAX_VALUE));
        
        String rowStr = request.getParameterValue("row");
        String colStr = request.getParameterValue("col");
        
        try
        {
            int row = Integer.parseInt(rowStr);
            int col = Integer.parseInt(colStr);

            response.setStatus(200);
            response.setContent(String.format("You clicked on %d, %d", row, col).getBytes());
        }
        catch (Exception ex)
        {
            response.setStatus(500);
            //response.setContent(ex.toString().getBytes());
        }
    }
    
}
