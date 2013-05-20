/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Daniel
 */
public class ConsoleUtils
{
    public static final String PROMPT = "> ";
    
    public static void printPrompt()
    {
        System.out.print("> ");
    }
    
    public static void println(String message)
    {
        System.out.println(message);
        printPrompt();
    }
}
