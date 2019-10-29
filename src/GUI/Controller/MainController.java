package GUI.Controller;

import Util.ApplicationSettings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private MenuItem openFile;

    @FXML
    private MenuItem newFile;

    @FXML
    private MenuItem saveFile;

    @FXML
    private MenuItem closeFile;

    @FXML
    private MenuItem settings;

    @FXML
    private MenuItem about;

    @FXML
    private TextArea editor;

    @FXML
    private TextArea vhdlView;

    @FXML
    private TextArea watchesView;

    @FXML
    private TextArea logView;

    @FXML
    private Button run;

    @FXML
    private Button stop;

    @FXML
    private Button build;

    @FXML
    private Tab watchesTab;

    @FXML
    private Tab outputTab;

    private ApplicationSettings applicationSettings = new ApplicationSettings();
    private Stage primaryStage;

    @FXML
    void aboutAction(ActionEvent event) {

    }

    @FXML
    void closeFileAction(ActionEvent event) {

    }

    @FXML
    void newFileAction(ActionEvent event) {

    }

    @FXML
    void openFileAction(ActionEvent event) {

    }

    @FXML
    void runAction(ActionEvent event) {

    }

    @FXML
    void saveFileAction(ActionEvent event) {

    }

    @FXML
    void settingsAction(ActionEvent event) {
        SettingsController controller = new SettingsController();
        try {
            controller.setPrimaryScene(run.getScene());
            controller.setApplicationSettings(applicationSettings);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/SettingsPage.fxml"));
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("../" + applicationSettings.getApplicationStyle()).toExternalForm());
            Stage stage = new Stage();
            stage.setTitle("Settings");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initOwner(primaryStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void stopAction(ActionEvent event) {

    }

    @FXML
    void buildAction(ActionEvent event) {

    }

    public void setApplicationSettings(ApplicationSettings applicationSettings) {
        this.applicationSettings = applicationSettings;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        outputTab.setText("output" + applicationSettings.getOutputFileExtension());
    }
}
