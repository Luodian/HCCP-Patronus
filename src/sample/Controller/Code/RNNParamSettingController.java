package sample.Controller.Code;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.StartProcess;

public class RNNParamSettingController {

    @FXML
    private JFXButton GRU;

    @FXML
    private JFXButton addLSTM;

    @FXML
    private JFXButton Delete;

    @FXML
    private JFXButton complete;

    @FXML
    private JFXButton reset;

    @FXML
    private Pane layer_parameter_board;

    @FXML
    private JFXTextField nu_num;

    @FXML
    private JFXRadioButton tanh;

    @FXML
    private JFXRadioButton relu;

    @FXML
    private JFXRadioButton sigmod;

    @FXML
    private JFXRadioButton softmax;

    @FXML
    private JFXTextField recurrent_activation;

    @FXML
    private JFXRadioButton use_bias;

    @FXML
    private JFXTextField kernel_initializer;

    @FXML
    private JFXTextField recurrent_initializer;

    @FXML
    private Label bias_initializer;

    @FXML
    private JFXButton back;

    @FXML
    void AddGRULayer(MouseEvent event) {

    }

    @FXML
    void AddLSTMLayer(MouseEvent event) {

    }

    @FXML
    void backToPre(MouseEvent event) {
        Stage nnStage = StartProcess.hashMap.remove("rnn_param_settings");
        Stage codeStage = StartProcess.hashMap.get("nn_choose_board");
        nnStage.close();
        codeStage.show();
    }

    @FXML
    void complete(MouseEvent event) {

    }

    @FXML
    void deleteLayer(MouseEvent event) {

    }

    @FXML
    void resetParameters(MouseEvent event) {

    }

}

