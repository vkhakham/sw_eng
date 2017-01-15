package staff.src;

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
        try {
            openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleMessageFromServer(Object objMsg) {
        Message msg = Message.class.cast(objMsg);
        String uri = msg.uri;
        System.out.println(msg.obj);
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
