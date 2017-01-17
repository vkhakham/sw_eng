package staff;

import common.ClientType;
import common.Message;
import common.Uri;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import ocsf.client.AbstractClient;

import java.io.IOException;


public class StaffInterfaceController extends AbstractClient {
    private Integer sessionId;
    private Integer user;

    @FXML
    private Button pickToday;

    /**
     * Constructs the client.
     *
     * @param host the server's host name.
     * @param port the port number.
     */
    StaffInterfaceController(String host, int port, Integer sessionId, Integer user) {
        super(host, port);
        this.user = user;
        this.sessionId = sessionId;
        try {
            openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void GetPatientList(MouseEvent event) throws Exception {
        try {
            sendToServer(new Message(Uri.EmployeeGetQueue, this.user, this.sessionId, ClientType.Employee, "2017-01-01"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleMessageFromServer(Object objMsg){
        Message msg = Message.class.cast(objMsg);
        switch (msg.uri) {
            case EmployeeGetQueue:
                handleNewPatientList(msg);
                break;
        }
    }

    private void handleNewPatientList(Message msg) {
        System.out.println(msg);
    }
}
