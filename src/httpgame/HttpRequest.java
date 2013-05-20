package httpgame;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import static utils.ConsoleUtils.*;
import static utils.FileUtils.*;

public class HttpRequest
{
    private HashMap<String,String> headers = new HashMap<String,String>();
    
    private String method, url, httpVersion;
    
    private BufferedInputStream in;
    
    private void parseHeader(String line)
    {
        int colon = line.indexOf(":");
        String name = line.substring(0, colon).trim();
        String value = line.substring(colon+1).trim();
        headers.put(name, value);
    }

    public String getMethod()
    {
        return method;
    }

    public String getUrl()
    {
        return url;
    }

    public String getHttpVersion()
    {
        return httpVersion;
    }
    
    public String getHeader(String name)
    {
        return headers.get(name);
    }
    
    public HttpRequest(InputStream in)
            throws IOException
    {
        this.in = new BufferedInputStream(in);
        
        String Tn = Thread.currentThread().getName();
        
        println(Tn + "+=====~~~~------");
        
        String line = readLine(this.in);
        println(Tn + "| <- " + line);
        
        String[] splits = line.split(" ");
        method = splits[0];
        url = splits[1];
        httpVersion = splits[2];

        while ((line = readLine(this.in)) != null && !line.isEmpty())
        {
            parseHeader(line);
            println(Tn + "| <- " + line);
        }

        println(Tn + "+=====~~~~------");
        
    }
    
}
