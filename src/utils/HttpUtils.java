/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

/**
 *
 * @author Daniel
 */
public class HttpUtils
{
    public static final SimpleDateFormat HTTP_DATE_FORMAT = new SimpleDateFormat(
        "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
    
    public static final String HTTP_VERSION = "HTTP/1.1";
    
    public static final HashMap<Integer, String> STATUS_CODES = new HashMap<Integer, String>();
    
    static
    {
          STATUS_CODES.put(100, "Continue");
          STATUS_CODES.put(101, "Switching Protocols");
          STATUS_CODES.put(200, "OK");
          STATUS_CODES.put(201, "Created");
          STATUS_CODES.put(202, "Accepted");
          STATUS_CODES.put(203, "Non-Authoritative Information");
          STATUS_CODES.put(204, "No Content");
          STATUS_CODES.put(205, "Reset Content");
          STATUS_CODES.put(206, "Partial Content");
          STATUS_CODES.put(300, "Multiple Choices");
          STATUS_CODES.put(301, "Moved Permanently");
          STATUS_CODES.put(302, "Found");
          STATUS_CODES.put(303, "See Other");
          STATUS_CODES.put(304, "Not Modified");
          STATUS_CODES.put(305, "Use Proxy");
          STATUS_CODES.put(307, "Temporary Redirect");
          STATUS_CODES.put(400, "Bad Request");
          STATUS_CODES.put(401, "Unauthorized");
          STATUS_CODES.put(402, "Payment Required");
          STATUS_CODES.put(403, "Forbidden");
          STATUS_CODES.put(404, "Not Found");
          STATUS_CODES.put(405, "Method Not Allowed");
          STATUS_CODES.put(406, "Not Acceptable");
          STATUS_CODES.put(407, "Proxy Authentication Required");
          STATUS_CODES.put(408, "Request Time-out");
          STATUS_CODES.put(409, "Conflict");
          STATUS_CODES.put(410, "Gone");
          STATUS_CODES.put(411, "Length Required");
          STATUS_CODES.put(412, "Precondition Failed");
          STATUS_CODES.put(413, "Request Entity Too Large");
          STATUS_CODES.put(414, "Request-URI Too Large");
          STATUS_CODES.put(415, "Unsupported Media Type");
          STATUS_CODES.put(416, "Requested range not satisfiable");
          STATUS_CODES.put(417, "Expectation Failed");
          STATUS_CODES.put(500, "Internal Server Error");
          STATUS_CODES.put(501, "Not Implemented");
          STATUS_CODES.put(502, "Bad Gateway");
          STATUS_CODES.put(503, "Service Unavailable");
          STATUS_CODES.put(504, "Gateway Time-out");
          STATUS_CODES.put(505, "HTTP Version not supported");
    }
    
    public static final String DEFAULT_MESSAGE = "I'm a teapot";
    
    public static final String CRLF = "\r\n", SP = " ";
}
