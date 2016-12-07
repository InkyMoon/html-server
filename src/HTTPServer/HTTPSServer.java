package HTTPServer;

import java.io.IOException;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

/**
 * The HTTPS server thread of a HTTP/2.0 server.
 *
 * @author Patrick Kastelic <patrick.kastelic@gmail.com>
 */
public class HTTPSServer extends Thread {

    private final int HTTPS_PORT = 443;

    /**
     * Runs the server.
     *
     * @param args is unused
     */
    public static void main(String args[]) {
        new HTTPSServer().run();
    }

    /**
     * Waits for incoming connections indefinitely. Each new connection is
     * handled by a new HTTPRequest thread. Each request must be handled by a
     * separate thread. The server only supports the close connection type.
     */
    public void run() {
        SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        SSLServerSocket httpsServSock;
        try
        {
            httpsServSock = (SSLServerSocket) ssf.createServerSocket(HTTPS_PORT);
        }
        catch (IOException ex)
        {
            System.out.println("Could not open a connection on port " + HTTPS_PORT);
            return;
        }
        while (true)
        {
            SSLSocket sock;
            try
            {
                sock = (SSLSocket) httpsServSock.accept();
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
