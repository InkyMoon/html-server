package HTTPServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The HTTP server thread of a HTTP/2.0 server.
 *
 * @author Patrick Kastelic <patrick.kastelic@gmail.com>
 */
public class HTTPServer extends Thread {

    private final int HTTP_PORT = 80;

    /**
     * Runs the server.
     *
     * @param args is unused
     */
    public static void main(String args[]) {
        new HTTPServer().run();
    }

    /**
     * Waits for incoming connections indefinitely. Each new connection is
     * handled by a new HTTPRequest thread. Each request must be handled by a
     * separate thread. The server only supports the close connection type.
     */
    public void run() {
        ServerSocket httpServSock;
        try
        {
            httpServSock = new ServerSocket(HTTP_PORT);
        }
        catch (IOException ex)
        {
            System.out.println("Could not open a connection on port " + HTTP_PORT);
            return;
        }
        while (true)
        {
            Socket sock;
            try
            {
                sock = httpServSock.accept();
                HTTPRequest servThread = new HTTPRequest(sock);
                servThread.start();
            }
            catch (IOException ex)
            {
                System.out.println(
                        "Could not connect to client. Waiting for new client.");
            }
        }
    }
}
