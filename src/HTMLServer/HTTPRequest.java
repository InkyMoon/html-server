package HTMLServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * A HTTP 1.0 server thread. Handles a single HTTP request. Currently supports
 * only the GET command, all other commands will print an error and disconnect
 * from the client.
 *
 * @author Patrick Kastelic <patrick.kastelic@gmail.com>
 */
public class HTTPRequest extends Thread {

    private Socket sock;
    private DataOutputStream writeSock;
    private BufferedReader readSock;

    /**
     * HTTPRequest constructor.
     *
     * @param clientSock the socket representing the client connection
     * @throws IOException if I/O cannot be established with the client
     */
    public HTTPRequest(Socket clientSock)
            throws IOException {
        sock = clientSock;
        writeSock = new DataOutputStream(sock.getOutputStream());
        readSock = new BufferedReader(
                new InputStreamReader(sock.getInputStream()));
    }

    /**
     * Runs the thread. Prints errors to the system console.
     */
    public void run() {
        String request = null;
        boolean disconnect = false;
        try
        {
            request = readSock.readLine();
            if (request == null)
            {
                disconnect = true;
            }
        }
        catch (IOException ex)
        {
            System.out.println("Could not read request from client.");
            disconnect = true;
        }
        if (!disconnect)
        {
            System.out.println("Request " + request + " on port " + sock.getPort());
            StringTokenizer st = new StringTokenizer(request);
            String command = st.nextToken();
            switch (command)
            {
                case "GET":
                    Get(st.nextToken());
                    break;
                default:
                    System.out.println("Unsupported command received: " + command);
            }
        }
        try
        {
            sock.close();
        }
        catch (IOException ex)
        {
            System.out.println("Could not close client socket. Already closed.");
        }
    }

    private void Get(String filepath) {
        try
        {
            HTTP.get(filepath, writeSock);
        }
        catch (IOException ex)
        {
            System.out.println("Could not send file to client.");
        }
    }
}
