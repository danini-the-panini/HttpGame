/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package httpgame;

import java.io.IOException;
import java.util.Scanner;
import static utils.ConsoleUtils.*;

/**
 *
 * @author Daniel
 */
public class Main
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        try
        {
            System.out.println("  +------------------+");
            System.out.println("~<| HTTP Game Server |>~");
            System.out.println("  +------------------+");
            System.out.println();
            Server server = new Server(55555,20);
            
            server.start();
            
            Scanner scan = new Scanner(System.in);
            boolean running = true;
            
            while (running)
            {
                String command = scan.nextLine().trim().toUpperCase();
                if ("EXIT".equals(command) || "QUIT".equals(command) || "BYE".equals(command))
                {
                    running = false;
                }
                else
                {
                    println("Bad command or file name.");
                }
            }
            
            System.out.println("Shutting down...");
            server.shutDown();
            try
            {
                server.join();
            } catch (InterruptedException ex) { }
            
        } catch (IOException ex)
        {
            System.out.println(ex);
        }
        
    }
}
