package HTTP;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * A helper class providing HTTP (HyperText Transfer Protocol) functionality.
 *
 * @author Patrick Kastelic <patrick.kastelic@gmail.com>
 */
public class GetHandler {

    private static final String CRLF = "\r\n";
    private static final String HTTP_VER = "HTTP/1.0";
    private static final int CHUNK_SIZE = 1024;

    /**
     * Responds to a HTTP GET command.
     *
     * @param address is the path to the file
     * @param output is the DataOutputStream to send the response message to
     * @throws IOException if unable to write to the output stream
     */
    public static void get(String address, DataOutputStream output)
            throws IOException {
        address = "." + address;
        boolean found = true;
        FileInputStream file = null;
        try
        {
            file = new FileInputStream(address);
        }
        catch (FileNotFoundException ex)
        {
            found = false;
        }
        String status = HTTP_VER + (found ? " 200 OK" : " 404 Not Found");
        status = status + CRLF;
        output.writeBytes(status);
        String header = "Content-Type: " + contentType(address);
        header = header + CRLF + "Connection: close";
        header = header + CRLF;
        output.writeBytes(header);
        if (found)
        {
            writeFile(file, output);
        }
        else
        {
            output.writeBytes("<HTML>" + "<HEAD><TITLE>Not Found</TITLE></HEAD>"
                              + "<BODY>Not Found</BODY></HTML>");
        }
    }

    /**
     * Writes a file to an output stream in small chunks.
     *
     * @param fileI the file input stream
     * @param out the output stream to write to
     * @throws IOException if the stream cannot be written
     */
    private static void writeFile(FileInputStream fileI, DataOutputStream out)
            throws IOException {
        out.writeBytes(CRLF);
        int bytesToRead = fileI.available();
        byte[] chunk = new byte[CHUNK_SIZE];
        while (bytesToRead != 0)
        {
            bytesToRead = bytesToRead < CHUNK_SIZE ? bytesToRead : CHUNK_SIZE;
            fileI.read(chunk, 0, bytesToRead);
            out.write(chunk, 0, bytesToRead);
            bytesToRead = fileI.available();
        }
    }

    /**
     * Determines the content type attribute of a request.
     *
     * @param fileName the file to examine
     * @return the content type of the file
     */
    private static String contentType(String fileName) {
        if (fileName.endsWith(".htm") || fileName.endsWith(".html"))
        {
            return "text/html";
        }
        if (fileName.endsWith(".gif"))
        {
            return "image/gif";
        }
        if (fileName.endsWith(".bmp"))
        {
            return "image/bmp";
        }
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg"))
        {
            return "image/jpeg";
        }
        return "application/octet-stream";
    }
}
