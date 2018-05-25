package sample.Controller.Code;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.StartProcess;

import java.io.IOException;

public class NeuralNetworkController {

    @FXML
    private JFXButton cancel;

    @FXML
    private JFXButton CNN;

    @FXML
    private JFXButton RNN;

    @FXML
    private JFXButton GAN;

    @FXML
    private JFXButton user_define;

    @FXML
    void buildCnnPat(MouseEvent event) {

    }

    @FXML
    void buildGANPat(MouseEvent event) {

    }

    @FXML
    void buildRNNPat(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../FXML/rnn_build.fxml"));
        root.getStylesheets().add(getClass().getResource("../../FXML/layer_list.css").toExternalForm());
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        StartProcess.hashMap.put("rnn_param_settings", stage);
        StartProcess.hashMap.get("nn_choose_board").hide();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void buildUserDefinePat(MouseEvent event) {

    }

    @FXML
    void frameCancel(MouseEvent event) {
        Stage nnStage = StartProcess.hashMap.remove("nn_choose_board");
        Stage codeStage = StartProcess.hashMap.get("coding");
        nnStage.close();
        codeStage.show();
    }

}
