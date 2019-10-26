package GUI;
import GUI.Controller.MainController;
import Util.ApplicationSerializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private ApplicationSerializer serializer = new ApplicationSerializer();
    private MainController controller = new MainController();

    @Override
    public void start(Stage primaryStage) throws Exception{
        controller.setApplicationSettings(serializer.load());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("View/MainPage.fxml"));
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource(serializer.load().getApplicationStyle()).toExternalForm());
        primaryStage.setTitle("DLX Assembler");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
