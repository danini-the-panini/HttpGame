package httpgame;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import static utils.ConsoleUtils.*;

/**
 *
 * @author Daniel
 */
public class ClientHandler extends Thread
{
    private AtomicBoolean running = new AtomicBoolean(true);
    private AtomicBoolean available = new AtomicBoolean(true);
    
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
    
    @Override
    public void run()
    {
        while (running.get())
        {
            while (running.get() && available.get()) { /* spin! */ }
            if (running.get()) println(getName() + " grabbed from pool.");
            while (running.get() && socket.get() == null) { /* spin some more! */ }
            
            if (running.get())
            {
                println(getName() + " running.");
                try
                {
                    Socket socket = this.socket.get();
                    
                    in = socket.getInputStream();
                    out = socket.getOutputStream();

                    println(getName() + " READING REQUEST");
                    HttpRequest request = new HttpRequest(in);
                    
                    // TODO: blah
                    println(getName() + " SENDING RESPONSE");
                    HttpResponse response = new HttpResponse(200);
                    response.setContentType("text/plain");
                    response.setContent("Hello World!".getBytes());
                    response.send(out);
                    
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
            
            setAvailable(true);
            socket.set(null);
        }
        println(Thread.currentThread().getName() + " shutting down.");
    }

    public boolean isAvailable()
    {
        return available.get();
    }

    public void setAvailable(boolean available)
    {
        this.available.set(available);
    }
    
    public void shutDown()
            throws IOException
    {
        running.set(false);
        Socket sock = this.socket.get();
        if (sock != null) sock.close();
    }
    
}
