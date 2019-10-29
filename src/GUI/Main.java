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
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        controller.setApplicationSettings(serializer.load());
        controller.setPrimaryStage(this.primaryStage);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("View/MainPage.fxml"));
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource(serializer.load().getApplicationStyle()).toExternalForm());
        this.primaryStage.setTitle("DLX Assembler");
        this.primaryStage.setScene(scene);
        this.primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
