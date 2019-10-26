package GUI.Controller;

import Util.ApplicationSettings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

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

    private ApplicationSettings applicationSettings = new ApplicationSettings();

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
}