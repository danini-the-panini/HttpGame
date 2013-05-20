package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 *
 * @author daniel
 */
public class FileUtils
{
    public static byte[] read(InputStream in)
            throws IOException
    {
        byte[] result = new byte[0];
        
        int n, oldLen;
        do
        {
            byte[] buffer = new byte[4096];
            n = in.read(buffer);
            if (n > 0)
            {
                oldLen = result.length;
                result = Arrays.copyOf(result, n+oldLen);
                System.arraycopy(buffer, 0, result, oldLen, n);
            }
        }
        while (n > 0);
        return result;
    }
    
    public static int read(InputStream in, byte[] buffer)
            throws IOException
    {
        int numBytes = buffer.length;
        int numRead = 0;
        int n = 0;
        
        while (numRead < numBytes && n > 0)
        {
            n = in.read(buffer, numRead, Math.min(4096, numBytes-numRead));
            if (n != -1) numRead += n;
        }
        return numRead;
    }
    
    
    public static String read(InputStream in, int numBytes)
            throws IOException
    {
        byte[] buffer = new byte[numBytes];
        int numRead = read(in, buffer);
        return new String(buffer, 0, numRead);
    }
    
    public static String readLine(InputStream in)
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
}
