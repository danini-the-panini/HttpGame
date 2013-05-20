/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package httpgame;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import static utils.FileUtils.*;
import static utils.GameUtils.*;
import static utils.HttpUtils.*;
import static utils.ConsoleUtils.*;

/**
 *
 * @author daniel
 */
public class FileFetcher extends ClientHandler.Executor
{

    @Override
    public void run(HttpRequest request, HttpResponse response)
            throws IOException
    {
        String url = request.getUrl();
        if (url.endsWith("/")) url += "index.html";
        
        String ext = url.substring(url.lastIndexOf(".")+1);
        String type;
        if ("html".equalsIgnoreCase(ext) ||
                "htm".equalsIgnoreCase(ext) ||
                "danml".equalsIgnoreCase(ext))
            type = "text/html";
        else if ("js".equalsIgnoreCase(ext))
            type = "application/javascript";
        else if ("css".equalsIgnoreCase(ext))
            type = "text/css";
        else if ("png".equalsIgnoreCase(ext))
            type = "image/png";
        else
            type = "text/plain";
        
        
        if (url.startsWith("/")) url = "."+url;
        else url = "./"+url;
        
        File res = new File(url);
        
        byte[] content = null;
        
        if (res.exists())
        {
            BufferedInputStream resIn =
                    new BufferedInputStream(new FileInputStream(res));

            content = read(resIn);
            
            // TODO: depends on player I think...
            if ("danml".equals(ext))
            {
                Player player = getPlayer(request, response);
                
                String newGameString = request.getParameterValue("newGame");
                if (newGameString != null && newGameString.equalsIgnoreCase("true"))
                {
                    println("Starting New Game!!! :) :) :) :) :) :)");
                    player.startGame(GAME_SIZE, GAME_SIZE, 5);
                }
                
                Danml danml = new Danml(new String(content));
                danml.addReplacement("board", buildBoard(player.getBoard()));
                content = danml.parse().getBytes();
            }
        }
        
        if (content == null)
        {
            response.setStatus("ico".equalsIgnoreCase(ext) ? 204 : 404);
        }
        else
        {
            response.setStatus(200);
            response.setContentType(type);
            response.setContent(content);
        }
    }
}
