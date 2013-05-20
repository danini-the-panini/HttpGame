package httpgame;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
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
    private HashMap<String,Executor> executors = new HashMap<String, Executor>();
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
    
    public abstract class Executor
    {
        public abstract byte[] run(HttpRequest request);
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
                    
                    String url = request.getUrl();
                    if (url.startsWith("/")) url = "."+url;
                    else url = "./"+url;
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
                    else if ("x".equalsIgnoreCase(ext))
                        type = "text/plain"; // TODO: jason maybe?
                    else
                        type = "text/plain";
                    
                    println("URL == " + url);
                    File res = new File(url);
                    
                    UUID uuid;
                    String uuidStr = request.getCookie("uuid");
                    if (uuidStr != null) uuid = UUID.fromString(uuidStr);
                    else uuid = UUID.randomUUID();
                    
                    Player player = getPlayer(uuid);
                    
                    String stateStr = request.getParameterValue("state");
                    int newState = -1;
                    if (stateStr != null)
                    {
                        newState =  Integer.parseInt(stateStr);
                        println("NEW STATE IS " + newState + "!!!");
                    }
                    
                    println(getName() + " SENDING RESPONSE");
                    HttpResponse response = new HttpResponse();
                    if (res.exists())
                    {
                        BufferedInputStream resIn =
                                new BufferedInputStream(new FileInputStream(res));
                        
                        byte[] content = null;
                        
                        if ("x".equals(ext))
                        {
                            Executor ex = executors.get(url.substring(0, url.indexOf(".x")));
                            if (ex != null)
                                content = ex.run(request);
                        }
                        else 
                        {
                            content = read(resIn);
                        }
                        
                        if ("danml".equals(ext))
                        {
                            Danml danml = new Danml(new String(content));
                            danml.addReplacement("board", GameUtils.buildBoard());
                            content = danml.parse().getBytes();
                        }
                        
                        if (content == null)
                        {
                            response.setStatus(404);
                        }
                        else
                        {
                            response.setStatus(200);
                            response.setContentType(type);
                            response.setContent(content);
                        }
                    }
                    else
                    {
                        response.setStatus(404);
                    }
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
