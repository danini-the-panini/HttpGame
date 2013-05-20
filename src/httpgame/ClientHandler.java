package httpgame;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import static utils.HttpUtils.*;
import static utils.ConsoleUtils.*;
import static utils.FileUtils.*;
import utils.GameUtils;

/**
 *
 * @author Daniel
 */
public class ClientHandler extends Thread
{
    private AtomicBoolean running = new AtomicBoolean(true);
    
    private AtomicReference<Socket> socket = new AtomicReference<Socket>(null);
    private InputStream in;
    private OutputStream out;

    public ClientHandler(String name)
    {
        super(name);
    }
    
    public void handle(Socket socket)
            throws IOException
    {
        this.socket.set(socket);
    }
    
    public static abstract class Executor
    {
        public abstract void run(HttpRequest request, HttpResponse response)
                throws IOException;
    }
    
    @Override
    public void run()
    {
        while (running.get())
        {
            while (running.get() && socket.get() == null) { /* spin some more! */ }
            
            Socket currentSocket = null;
            
            if (running.get())
            {
                println(getName() + " running.");
                try
                {
                    currentSocket = socket.get();
                    
                    in = currentSocket.getInputStream();
                    out = currentSocket.getOutputStream();

                    println(getName() + " READING REQUEST");
                    HttpRequest request = new HttpRequest(in);
                    HttpResponse response = new HttpResponse();
                    
                    String url = request.getUrl();
                    if (url.endsWith("/")) url += "index.html";
                    
                    if (url.endsWith(".x"))
                    {
                        
                        if (url.startsWith("/")) url = url.substring(1);
                        
                        String exName = url.substring(0, url.indexOf(".x"));
                        println("EXECUTOR NAME: " + exName);
                        Executor ex = null;
                        try
                        {
                            ex = (Executor)(Class.forName("httpgame."+exName).newInstance());
                        } catch (Exception ex1)
                        { println(ex1.toString()); }
                        if (ex != null)
                            ex.run(request, response);
                    }
                    else
                    {
                        new FileFetcher().run(request, response);
                    }
                    
                    println(getName() + " SENDING RESPONSE");
                        
                    response.send(out);
                    
                    currentSocket.close();
                }
                catch (SocketException ex)
                {
                    // ?
                    if (ex.getMessage().trim().equals("socket closed"))
                        println(getName() + " Socket closed");
                }
                catch (IOException ex)
                {
                    System.out.println(ex);
                }
                println(getName() + " done.");
            }
                    
            socket.compareAndSet(currentSocket, null);
            
        }
        println(Thread.currentThread().getName() + " shutting down.");
    }

    public boolean hasSocket()
    {
        return socket.get() != null;
    }
    
    public void shutDown()
            throws IOException
    {
        running.set(false);
        Socket sock = this.socket.get();
        if (sock != null) sock.close();
    }
    
}
