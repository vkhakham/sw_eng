package staff;

import java.io.*;
import java.util.ArrayList;

import common.ClientType;
import common.Message;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ocsf.client.*;


public class StaffInterfaceController extends AbstractClient {

    @FXML
    private TextField user_name_field;

    @FXML
    private TextField password_field;

    @FXML
    private Button login_btn;

    /**
     * Constructs the client.
     *
     * @param host the server's host name.
     * @param port the port number.
     */
    public StaffInterfaceController(String host, int port) {
        super(host, port);
        try {
            openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void login_pressed(MouseEvent event) throws Exception {
        try {
            sendToServer(new Message("login", new Integer(user_name_field.getText()), null, ClientType.Employee, new Integer(password_field.getText())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene PatientsListScene = new Scene(FXMLLoader.load(getClass().getResource("patients_list.fxml")), 500,500);
        Stage currentStage = (Stage) login_btn.getScene().getWindow();
        currentStage.setScene(PatientsListScene);
    }

    @Override
    protected void handleMessageFromServer(Object objMsg) {
        Message msg = Message.class.cast(objMsg);
        System.out.println(msg.data);
    }
}
