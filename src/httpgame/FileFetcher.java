/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package httpgame;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import static utils.FileUtils.read;
import utils.GameUtils;

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
                Danml danml = new Danml(new String(content));
                danml.addReplacement("board", GameUtils.buildBoard());
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
