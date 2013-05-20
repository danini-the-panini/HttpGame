/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package httpgame;

/**
 *
 * @author daniel
 */
public class Play extends ClientHandler.Executor
{

    @Override
    public byte[] run(HttpRequest request)
    {
        String rowStr = request.getParameterValue("row");
        String colStr = request.getParameterValue("col");
        
        try
        {
            int row = Integer.parseInt(rowStr);
            int col = Integer.parseInt(colStr);

            return String.format("You clicked on %d, %d", row, col).getBytes();
        }
        catch (Exception ex)
        {
            return ex.toString().getBytes();
        }
    }
    
}
