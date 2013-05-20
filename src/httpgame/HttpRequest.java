package httpgame;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import static utils.ConsoleUtils.*;

public class HttpRequest
{
    private HashMap<String,String> headers = new HashMap<String,String>();
    
    private String method, url, httpVersion;
    
    private BufferedInputStream in;
    
    private String readLine()
            throws IOException
    {
        int input;
        int state = 0;
        
        String line = new String();
        
        while (state != -1)
        {
            input = in.read();
            if (input == -1) state = 0;
            else if (input == '\r')
            {
                input = in.read();
                if (input == '\n')
                    state = -1;
                else
                    line += (char)input;
            }
            else
                line += (char)input;
        }
        return line;
    }
    
    private String read(int numBytes)
            throws IOException
    {
        byte[] buffer = new byte[numBytes];
        int numRead = 0;
        int n = 0;
        
        while (numRead < numBytes && n > 0)
        {
            n = in.read(buffer, numRead, Math.min(4096, numBytes-numRead));
            if (n != -1) numRead += n;
        }
        return new String(buffer, 0, numRead);
    }
    
    private void parseHeader(String line)
    {
        int colon = line.indexOf(":");
        String name = line.substring(0, colon).trim();
        String value = line.substring(colon+1).trim();
        headers.put(name, value);
    }
    
    public HttpRequest(InputStream in)
            throws IOException
    {
        this.in = new BufferedInputStream(in);
        
        String Tn = Thread.currentThread().getName();
        
        println(Tn + "+=====~~~~------");
        
        String line = readLine();
        if (line != null && !line.isEmpty())
        {
            println(Tn + "| <- " + line);

            while ((line = readLine()) != null && !line.isEmpty())
            {
                parseHeader(line);
                println(Tn + "| <- " + line);
            }

            println(Tn + "+=====~~~~------");
        }
        else
        {
            throw new IOException(Tn + " -! Empty request!");
        }
        
    }
    
}
