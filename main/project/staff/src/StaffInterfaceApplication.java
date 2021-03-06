package staff.src;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;


public class StaffInterfaceApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        String fxmlDocPath = "staff/src/login_pane.fxml";
        FileInputStream fxmlStream = new FileInputStream(fxmlDocPath);

        LoginController controller = new LoginController("localhost", 5555);
        loader.setController(controller);
        Scene login_scene = new Scene(loader.load(fxmlStream), 450, 200);
        primaryStage.setTitle("Login");
        primaryStage.setScene(login_scene);
        primaryStage.setResizable(true);
        primaryStage.setOnCloseRequest(e -> System.exit(0));
        primaryStage.show();
    }
}
