package sample.Controller.Code;

import com.jfoenix.controls.JFXListView;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.fxmisc.flowless.VirtualizedScrollPane;
import sample.StartProcess;
import sample.Utils.HighlightingCode;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CodeController implements Initializable {

    final WebView browser = new WebView();

    final WebEngine webEngine = browser.getEngine();

    @FXML
    private JFXListView my_programs;
    @FXML
    private AnchorPane console_area;
    @FXML
    private AnchorPane code_area;
    private String code;
    private StringBuilder file_name;

    public static JFXListView programs;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        programs = my_programs;

//        Label label1 = new Label("LSTM");
//        label1.setTextFill(Paint.valueOf("#ffffff"));
//        Label label2 = new Label("cnn");
//        label2.setTextFill(Paint.valueOf("#ffffff"));
//        my_programs.getItems().add(label1);
//        my_programs.getItems().add(label2);

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
    void newCode(MouseEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../../FXML/nn_choose_board.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        StartProcess.hashMap.put("nn_choose_board", stage);
        StartProcess.hashMap.get("coding").hide();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void saveCode(MouseEvent event) {

    }

}
