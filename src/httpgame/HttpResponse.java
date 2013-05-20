/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package httpgame;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import static utils.ConsoleUtils.*;
import static utils.HttpUtils.*;

/**
 *
 * @author Daniel
 */
public class HttpResponse
{
    
    private int status;
    
    private HashMap<String,String> headers = new HashMap<String, String>();
    
    private byte[] content = null;

    public HttpResponse(int status)
    {
        this.status = status;
    }
    
    public void setHeader(String name, String value)
    {
        headers.put(name, value);
    }
    
    public void setContentType(String value)
    {
        setHeader("content-type",value);
    }

    public void setContent(byte[] content)
    {
        this.content = content;
        setHeader("content-length", ""+content.length);
    }
    
    public void send(OutputStream out)
            throws IOException
    {
        String Tn = Thread.currentThread().getName();
        
        println(Tn+"+=====~~~~------");
        
        String statusMessage = STATUS_CODES.get(status);
        if (statusMessage == null) statusMessage = DEFAULT_MESSAGE;
        String line = HTTP_VERSION + SP + status + SP + statusMessage;
        println(Tn + "| -> " + line);
        out.write(line.getBytes());
        out.write(CRLF.getBytes());
        
        for (Map.Entry<String,String> e :headers.entrySet())
        {
            line = e.getKey() + ": " + e.getValue();
            println(Tn + "| -> " + line);
            out.write(line.getBytes());
            out.write(CRLF.getBytes());
        }
        
        out.write(CRLF.getBytes());
        
        if (content != null)
        {
            println(Tn + "| -> [content]");
            out.write(content);
        }
        
        out.flush();
        println(Tn+"+=====~~~~------");
    }
}
