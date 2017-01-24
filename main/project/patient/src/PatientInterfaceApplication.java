package patient.src;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.InputStream;


public class PatientInterfaceApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        String cp = System.getenv("CLASSPATH");
        System.out.println(cp);
        FXMLLoader loader = new FXMLLoader();
        InputStream fxmlStream = getClass().getResourceAsStream("/login_pane.fxml");
        LoginController controller = new LoginController("localhost", 5555);
        loader.setController(controller);
        Scene login_scene = new Scene(loader.load(fxmlStream), 450, 200);
        primaryStage.setTitle("Login");
        primaryStage.setScene(login_scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }
}
