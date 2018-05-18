package sample.Controller.Code;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.fxmisc.flowless.VirtualizedScrollPane;
import sample.StartProcess;
import sample.Utils.HighlightingCode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CodeController implements Initializable {

    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();
    @FXML
    private JFXButton back_to_main;
    @FXML
    private JFXListView my_programs;
    @FXML
    private AnchorPane console_area;
    @FXML
    private AnchorPane code_area;
    private String code;
    private StringBuilder file_name;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Label label1 = new Label("luodian's script");
        label1.setTextFill(Paint.valueOf("#ffffff"));
        Label label2 = new Label("jackson's script");
        label2.setTextFill(Paint.valueOf("#ffffff"));
        my_programs.getItems().add(label1);
        my_programs.getItems().add(label2);

        my_programs.setExpanded(true);
        my_programs.setVerticalGap(Double.valueOf(15.0));
        my_programs.depthProperty().set(5);
        HighlightingCode highlighter = new HighlightingCode();
        my_programs.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue observable, Object oldValue, Object newValue) ->
                {
                    VirtualizedScrollPane vz = highlighter.HLCodeArea(code_area.getPrefWidth(), code_area.getHeight());
                    code_area.getChildren().add(vz);
                    code = highlighter.getSampleCode();
                    int class_name_index = code.indexOf("public  class ");
                    file_name = new StringBuilder("");
                    for (int i = class_name_index + 13; i < code.length() && code.charAt(i) != ' '; ++i) {
                        file_name.append(String.valueOf(code.charAt(i)));
                    }
                    File file = new File("src/sample/resources/resources/" + file_name + ".java");
                    if (!file.exists()) {
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        FileWriter flwt = new FileWriter(file);
                        flwt.write(code);
                        flwt.flush();
                        flwt.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        webEngine.loadContent("Java command line is waiting.");
    }


    @FXML
    void backToMain(MouseEvent event) {
        Stage codeStage = StartProcess.hashMap.remove("coding");
        Stage mainStage = StartProcess.hashMap.get("main_page");
        mainStage.show();
        codeStage.close();
    }

    @FXML
    void deleteCode(MouseEvent event) {

    }

    @FXML
    void newCode(MouseEvent event) {

    }

    @FXML
    void saveCode(MouseEvent event) {

    }

}
