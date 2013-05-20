package httpgame;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import static utils.ConsoleUtils.*;

/**
 *
 * @author Daniel
 */
public class Server extends Thread
{
    private ServerSocket socket;
    private boolean running = true;
    private ClientHandlerPool pool;

    public Server(int port, int capacity)
            throws IOException
    {
        println("Creating Server...");
        socket = new ServerSocket(port);
        pool = new ClientHandlerPool(capacity);
    }

    @Override
    public void run()
    {
        println("Starting server...");
        while (running)
        {
            try
            {
                Socket sock = socket.accept();
                println("Connection from " + sock.getInetAddress().getHostName());
                
                pool.handle(sock);
            }
            catch (SocketException ex)
            {
                // ?
                if (ex.getMessage().trim().equals("socket closed"))
                    println("Socket closed");
            }
            catch (IOException ex)
            {
                println(ex.toString());
            }
        }
        
        try
        {
        pool.shutDown();
        }
        catch (IOException ex)
        {
            println(ex.toString());
        }
        
    }
    
    
    public void shutDown()
            throws IOException
    {
        running = false;
        socket.close();
    }
    
}
