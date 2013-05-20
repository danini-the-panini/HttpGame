/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package httpgame;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static utils.ConsoleUtils.*;
import static utils.HttpUtils.*;

/**
 *
 * @author Daniel
 */
public class HttpResponse
{
    
    private int status = 0;
    
    private HashMap<String,List<String>> headers = new HashMap<String, List<String>>();
    
    private byte[] content = null;

    public void setStatus(int status)
    {
        this.status = status;
    }
    
    public void addHeader(String name, String value)
    {
        List<String> list = headers.get(name);
        if (list == null)
        {
            list = new ArrayList<String>();
            headers.put(name,list);
        }
        list.add(value);
    }
    
    public void setHeader(String name, String value)
    {
        List<String> list = headers.get(name);
        if (list == null)
        {
            list = new ArrayList<String>();
            headers.put(name,list);
        }
        else
        {
            list.clear();
        }
        list.add(value);
    }
    
    public void setContentType(String value)
    {
        setHeader("Content-Type",value);
    }

    public void setContent(byte[] content)
    {
        this.content = content;
        setHeader("Content-Length", ""+content.length);
    }
    
    public void setCookie(String name, String value, Date expires)
    {
        addHeader("Set-Cookie",
                name + "=" + value + "; "
                + "path=/; "
                + "expres="+ HTTP_DATE_FORMAT.format(expires));
    }
    
    public void deleteCookie(String name)
    {
        setCookie(name," ",new Date(0));
    }
    
    public void send(OutputStream out)
            throws IOException
    {
        if (status == 0)
            throw new IOException("HTTP status not set");
        
        String Tn = Thread.currentThread().getName();
        
        println(Tn+"+=====~~~~------");
        
        String statusMessage = STATUS_CODES.get(status);
        if (statusMessage == null) statusMessage = DEFAULT_MESSAGE;
        String line = HTTP_VERSION + SP + status + SP + statusMessage;
        println(Tn + "| -> " + line);
        out.write(line.getBytes());
        out.write(CRLF.getBytes());
        
        for (Map.Entry<String,List<String>> e :headers.entrySet())
        {
            String key = e.getKey();
            for (String value :e.getValue())
            {
                line = key + ": " + value;
                println(Tn + "| -> " + line);
                out.write(line.getBytes());
                out.write(CRLF.getBytes());
            }
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
