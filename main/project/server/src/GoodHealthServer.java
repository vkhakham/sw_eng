import java.io.*;

import common.Message;
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
    protected void handleMessageFromClient(Object objMsg, ConnectionToClient client) {
        Message msg = Message.class.cast(objMsg);
        String uri = msg.uri;
        System.out.println(uri);
    }
}
