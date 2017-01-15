package staff.src;

import java.io.*;

import common.ClientType;
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
        System.out.println(msg.data);
    }

    void login(int username, int password) {
        try {
            sendToServer(new Message("login", username, null, ClientType.Employee, password));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
