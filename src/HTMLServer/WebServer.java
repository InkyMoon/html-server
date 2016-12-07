package HTMLServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A HTTP 1.0 server.
 *
 * Waits for incoming connections and handles each on a separate thread.
 *
 * @author Patrick Kastelic <patrick.kastelic@gmail.com>
 */
public class WebServer {

    private final int PORT = 5764;

    /**
     * Runs the server.
     *
     * @param args is unused
     */
    public static void main(String args[]) {
        new WebServer().run();
    }

    /**
     * Waits for incoming connections indefinitely. Each new connection is
     * handled by a new HTTPRequest thread. Each request must be handled by a
     * separate thread. The server only supports the close connection type.
     */
    public void run() {
        ServerSocket servSock;
        try
        {
            servSock = new ServerSocket(PORT);
        }
        catch (IOException ex)
        {
            System.out.println("Could not open a connection on port " + PORT);
            return;
        }
        while (true)
        {
            Socket sock;
            try
            {
                sock = servSock.accept();
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
