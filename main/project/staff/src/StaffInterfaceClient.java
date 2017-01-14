import java.io.*;
import java.util.ArrayList;

import common.Message;
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

    protected void login(String username, String password) {
        ArrayList<String> params = new ArrayList<>();
        params.add(username);
        params.add(password);
        try {
            sendToServer(new Message("login", params));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
