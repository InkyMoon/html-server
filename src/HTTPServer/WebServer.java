package HTTPServer;

/**
 * A HTTP 2.0 server.
 *
 * Waits for incoming connections and handles each on a separate thread.
 *
 * @author Patrick Kastelic <patrick.kastelic@gmail.com>
 */
public class WebServer {

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
        new HTTPServer().start();
        new HTTPSServer().start();
    }
}
