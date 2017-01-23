package patient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;


public class PatientInterfaceApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        String fxmlDocPath = "patient/login_pane.fxml";
        FileInputStream fxmlStream = new FileInputStream(fxmlDocPath);

        patient.LoginController controller = new patient.LoginController("localhost", 5555);
        loader.setController(controller);
        Scene login_scene = new Scene(loader.load(fxmlStream), 450, 200);
        primaryStage.setTitle("Login");
        primaryStage.setScene(login_scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }
}
