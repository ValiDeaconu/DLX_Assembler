package GUI.Controller;

import Assembler.AssemblerManager;
import Assembler.ProcessState;
import CodeReflection.CodeParser;
import CodeReflection.CodeParserState;
import FileManager.FileManager;
import GUI.Main;
import Util.ApplicationSettings;
import Util.Observable;
import Util.Observer;
import javafx.concurrent.Task;
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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainController implements Initializable, Observer {

    @FXML
    private MenuItem openFile;

    @FXML
    private MenuItem newFile;

    @FXML
    private MenuItem saveFile;

    @FXML
    private MenuItem settings;

    @FXML
    private MenuItem about;

    @FXML
    private CodeArea editor;

    @FXML
    private TextArea vhdlView;

    @FXML
    private TextArea watchesView;

    @FXML
    private TextArea logView;

    @FXML
    private Button run;

    @FXML
    private Tab outputTab;

    private ApplicationSettings applicationSettings = new ApplicationSettings();
    private Stage primaryStage;

    private static final String[] KEYWORDS = new String[] {
            "SPECIAL", "FPARITH", "ADDI", "ADDUI", "ANDI", "BEQZ", "BFPF", "BFPT", "BNEZ", "J", "JAL",
            "JALR", "JR", "LB", "LBU", "LD", "LF", "LH", "LHI", "LHU", "LW", "ORI", "RFE", "SB", "SD",
            "SEQI", "SF", "SGEI", "SGEUI", "SGTI", "SGTUI", "SH", "SLEI", "SLEUI", "SLLI", "SLTI", "SLTUI",
            "SNEI", "SRAI", "SRLI", "SUBI", "SUBUI", "SW", "TRAP", "XORI", "LA", "NOP", "ADD", "ADDU",
            "AND", "MOVD", "MOVF", "MOVFP2I", "MOVI2FP", "MOVI2S", "MOVS2I", "OR", "SEQ", "SGE", "SGEU",
            "SGT", "SGTU", "SLE", "SLEU", "SLL", "SLT", "SLTU", "SNE", "SRA", "SRL", "SUB", "SUBU", "XOR",
            "ADDD", "ADDF", "CVTD2F", "CVTD2I", "CVTF2D", "CVTF2I", "CVTI2D", "CVTI2F", "DIV", "DIVD",
            "DIVF", "DIVU", "EQD", "EQF", "GED", "GEF", "GTD", "GTF", "LED", "LEF", "LTD", "LTF", "MULT",
            "MULTD", "MULTF", "MULTU", "NED", "NEF", "SUBD", "SUBF"
    };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String COLON_PATTERN = "\\,";
    private static final String COMMENT_PATTERN = ";[^\n]*" + "|";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<COLON>" + COLON_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );

    private static final String sampleCode = String.join("\n", new String[] {
            ";Check array for value zero\n",
            "ADDI R31, R0, #1 ; R31 = 9",
            "loop: LW R15, 0(R1); put integer in location R1 into R15",
            "BEQZ R15, done; if R15 == 0 we need to exit the loop now",
            "ADDI R1, R1, #4; make R1 point to the next element in the array",
            "SUBI R31, R31, #1; decrement R31 by 1",
            "BNEZ R15, loop; if R15 != 0, we need to check the next value",
            "done: SW 256(R0), R31; R31 == 0 only if we FAILED to find a 0 in the array\n"
    });

    private ExecutorService executor;

    @FXML
    void aboutAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AboutPage.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("About");
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
    void newFileAction(ActionEvent event) {
        editor.replaceText("");
    }

    @FXML
    void openFileAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.dlx*"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            try {
                editor.replaceText(Files.readString(Paths.get(selectedFile.getPath())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void saveFileAction(ActionEvent event) {
        File file = new File(editor.getText());
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.dlx*"));
        File selectedFile = fileChooser.showSaveDialog(primaryStage);
        if (selectedFile != null) {
            saveTextToFile(editor.getText(), selectedFile);
        }
    }

    private void saveTextToFile(String content, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void runAction(ActionEvent event) {
        logView.setText("");
        CodeParser parser = CodeParser.getInstance(editor.getText());
        parser.subscribe(MainController.this);
        Thread t = new Thread(parser);
        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (parser.getState() == CodeParserState.SUCCEEDED) {
            AssemblerManager assembler = AssemblerManager.getInstance(parser.getInstructionList());
            assembler.subscribe(MainController.this);
            assembler.assemble();

            if (assembler.getAssemblerManagerState() == ProcessState.SUCCEEDED) {
                String vhdlCode = "";
                for (String instruction : assembler.getAssembledCode().split("\n")) {
                    vhdlCode += "\t\"" + instruction + "\",\n";
                }

                try {
                    if (applicationSettings.getOutputFileExtension().equals(".bin")){
                        FileManager.getBinWriter().performTask("output.bin", vhdlCode);
                        vhdlView.setText(new String(Files.readAllBytes(Paths.get("output.bin"))));
                    }
                    if (applicationSettings.getOutputFileExtension().equals(".VHDL")){
                        FileManager.getVhdlWriter().performTask("output.vhdl", vhdlCode);
                        vhdlView.setText(new String(Files.readAllBytes(Paths.get("output.vhdl"))));
                    }
                    System.out.println("File Manager worked");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("File Manager failed");
                }
            } else {
                System.out.println("File Manager failed");
            }
        }
    }

    @FXML
    void settingsAction(ActionEvent event) {
        SettingsController controller = new SettingsController();
        try {
            controller.setPrimaryScene(run.getScene());
            controller.setApplicationSettings(applicationSettings);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/SettingsPage.fxml"));
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource(applicationSettings.getApplicationStyle()).toExternalForm());
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

    public void setApplicationSettings(ApplicationSettings applicationSettings) {
        this.applicationSettings = applicationSettings;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
        String text = editor.getText();
        Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
            @Override
            protected StyleSpans<Collection<String>> call() throws Exception {
                return computeHighlighting(text);
            }
        };
        executor.execute(task);
        return task;
    }

    private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        editor.setStyleSpans(0, highlighting);
    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text.toUpperCase());
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("PAREN") != null ? "paren" :
                                    matcher.group("COLON") != null ? "colon" :
                                            matcher.group("COMMENT") != null ? "comment" :
                                                    null; /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    private void logPrintln(String string){
        logView.setText(logView.getText() + string + "\n");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        outputTab.setText("output" + applicationSettings.getOutputFileExtension());
        vhdlView.setEditable(false);
        executor = Executors.newSingleThreadExecutor();
        editor.setParagraphGraphicFactory(LineNumberFactory.get(editor));
        Subscription cleanupWhenDone = editor.multiPlainChanges()
                .successionEnds(Duration.ofMillis(500))
                .supplyTask(this::computeHighlightingAsync)
                .awaitLatest(editor.multiPlainChanges())
                .filterMap(t -> {
                    if(t.isSuccess()) {
                        return Optional.of(t.get());
                    } else {
                        t.getFailure().printStackTrace();
                        return Optional.empty();
                    }
                })
                .subscribe(this::applyHighlighting);
    }

    @Override
    public void update(Observable observable, String notification) {
        if (observable instanceof CodeParser) {
            logPrintln(notification);
        }
        if (observable instanceof AssemblerManager) {
            logView.setText(notification);
        }
    }
}
