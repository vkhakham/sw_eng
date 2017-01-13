import java.io.*;
import ocsf.server.*;

public class GoodHealthServer extends AbstractServer {
    SqlConnection sqlConnection;

    /**
     * Constructs a new server.
     *
     * @param port the port number on which to listen.
     */
    public GoodHealthServer(int port) {
        super(port);
        sqlConnection = new SqlConnection();
    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {

    }
}
