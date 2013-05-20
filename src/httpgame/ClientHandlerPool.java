package httpgame;

import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Daniel
 */
public class ClientHandlerPool
{
    private ClientHandler[] handlers;

    public ClientHandlerPool(int capacity)
    {
        handlers = new ClientHandler[capacity];
        for (int i = 0; i < capacity; i++)
            handlers[i] = new ClientHandler("T"+i);
    }
    
    public void handle(Socket sock)
            throws IOException
    {
        for (int i = 0; /* spin forever! */ ; i = (i+1)%handlers.length)
        {
            if (handlers[i].isAvailable())
            {
                handlers[i].setAvailable(false);
                if (!handlers[i].isAlive())
                    handlers[i].start();
                handlers[i].handle(sock);
                return;
            }
        }
    }
    
    public void shutDown()
            throws IOException
    {
        for (int i = 0; i < handlers.length; i++)
        {
            if (handlers[i].isAlive()) try
            {
                handlers[i].shutDown();
                handlers[i].join();
            } catch (InterruptedException ex) { }
        }
    }
}
