import java.io.*;
import ocsf.client.*;


public class StaffInterfaceClient extends AbstractClient {

    /**
     * Constructs the client.
     *
     * @param host the server's host name.
     * @param port the port number.
     */
    public StaffInterfaceClient(String host, int port) {
        super(host, port);
    }

    @Override
    protected void handleMessageFromServer(Object msg) {

    }
}
