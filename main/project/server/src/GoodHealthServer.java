package server.src;

import java.io.*;
import java.util.List;
import java.util.Objects;

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
        if (Objects.equals(msg.uri, "login")) {
            List<String> list = List.class.cast(msg.obj);
            if(sqlConnection.login(list.get(0), list.get(1))) {
                System.out.println("Registered user");
            } else {
                System.out.println("Unknown user");
            }
            System.out.println(uri);
        }
    }
}
