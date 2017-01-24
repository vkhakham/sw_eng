package staff.src;

import common.ClientType;
import common.Message;
import common.Uri;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ocsf.client.AbstractClient;

import java.io.FileInputStream;
import java.io.IOException;


public class LoginController extends AbstractClient {
    private Integer user;
    private Integer sessionId;

    @FXML
    private TextField user_name_field;

    @FXML
    private TextField password_field;

    @FXML
    private Button login_btn;

    @FXML
    private Text errorText;

    /**
     * Constructs the client.
     *
     * @param host the server's host name.
     * @param port the port number.
     */
    LoginController(String host, int port) {
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
            sendToServer(new Message(Uri.Login, new Integer(user_name_field.getText()), null, ClientType.Employee, new Integer(password_field.getText())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleMessageFromServer(Object objMsg){
        Message msg = Message.class.cast(objMsg);
        switch (msg.uri) {
            case Login:
                if (msg.data.equals(Boolean.TRUE)) {
                    handleLoginSuccess(msg);
                } else {
                    errorText.setText(msg.error.toString());
                }
                break;
        }
    }

    private void handleLoginSuccess(Message msg) {
        StaffInterfaceController controller = new StaffInterfaceController(this.getHost(), this.getPort(),
                msg.sessionId, msg.id);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FXMLLoader loader = new FXMLLoader();
                Stage primaryStage = (Stage) login_btn.getScene().getWindow();
                String fxmlDocPath = "staff/src/patients_list.fxml";
                try {
                    FileInputStream fxmlStream = new FileInputStream(fxmlDocPath);
                    loader.setController(controller);
                    primaryStage.setScene(new Scene(loader.load(fxmlStream), 522, 618));
                    primaryStage.setTitle("Patients List");
                    primaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Integer getSessionId() {
        return sessionId;
    }

    private Integer getUserId() {
        return user;
    }
}
