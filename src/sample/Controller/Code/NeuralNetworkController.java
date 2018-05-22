package sample.Controller.Code;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.StartProcess;

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
    void buildRNNPat(MouseEvent event) {

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
