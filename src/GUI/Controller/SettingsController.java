package GUI.Controller;

import Misc.ApplicationSerializer;
import Misc.ApplicationSettings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @FXML
    private ComboBox<String> outputList;

    @FXML
    private Button save;

    @FXML
    private Button close;

    @FXML
    private Label note;

    private Scene primaryScene;
    private Boolean changeTheme = false;
    private Boolean dark = false;
    private ApplicationSettings applicationSettings = new ApplicationSettings();
    private ApplicationSerializer serializer = new ApplicationSerializer();

    @FXML
    void closeAction(ActionEvent event) {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }

    @FXML
    void saveAction(ActionEvent event) {
        try{
            if (changeTheme == true) {
                if (dark == true) applicationSettings.setApplicationStyle("Style/darkstyle.css");
                else applicationSettings.setApplicationStyle("Style/lightstyle.css");
            }
            applicationSettings.setOutputFileExtension(outputList.getValue());
            serializer.save(applicationSettings);
            save.setDisable(true);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    void selectDarkTheme(ActionEvent event) {
        changeTheme = true;
        dark = true;
        save.setDisable(false);
    }

    @FXML
    void selectLightTheme(ActionEvent event) {
        changeTheme = true;
        dark = false;
        save.setDisable(false);
    }

    @FXML
    void outputChange(ActionEvent event) {
        save.setDisable(false);
    }

    public void setPrimaryScene(Scene primaryScene) {
        this.primaryScene = primaryScene;
    }

    public void setApplicationSettings(ApplicationSettings applicationSettings) {
        this.applicationSettings = applicationSettings;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> outputOptions = FXCollections.observableArrayList(".VHDL", ".bin");
        outputList.setItems(outputOptions);
        outputList.setValue(applicationSettings.getOutputFileExtension());
        save.setDisable(true);
    }
}
