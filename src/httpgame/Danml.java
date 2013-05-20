package httpgame;

import java.util.HashMap;

/**
 *
 * @author daniel
 */
public class Danml
{
    private HashMap<String,String> replacements = new HashMap<String, String>();
    
    private char[] danml;

    public Danml(String danml)
    {
        this.danml = danml.toCharArray();
    }
    
    public void addReplacement(String tag, String replacement)
    {
        replacements.put(tag, replacement);
    }
    
    public String parse()
    {
        StringBuilder sb = new StringBuilder();
        StringBuilder inside = null;
        
        for (int i = 0; i < danml.length; i++)
        {
            if (inside == null)
            {
                if (danml[i] == '<' && i < danml.length-1 && danml[i+1] == '?')
                {
                    i++;
                    inside = new StringBuilder();
                }
                else sb.append(danml[i]);
            }
            else
            {
                if (danml[i] == '?' && i < danml.length-1 && danml[i+1] == '>')
                {
                    i++;
                    String replacement = replacements.get(inside.toString());
                    if (replacement == null) replacement = new String();
                    sb.append(replacement);
                    inside = null;
                }
                else
                {
                    if (!Character.isWhitespace(danml[i]))
                        inside.append(danml[i]);
                }
            }
        }
        
        return sb.toString();
    }
    
}
