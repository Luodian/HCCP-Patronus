package sample.Controller.Code;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.Entity.LayerItem;
import sample.StartProcess;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RNNParamSettingController implements Initializable {

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

    public static ArrayList<LayerItem> layerItems;
    @FXML
    private JFXListView layer_list;

    @FXML
    void AddGRULayer(MouseEvent event) {
        Label layer = new Label("GRU");
        LayerItem layerItem = new LayerItem();
        layerItems.add(layerItem);
        layer.setTextFill(Paint.valueOf("#ffffff"));
        layer_list.getItems().add(layer);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        layerItems = new ArrayList<LayerItem>();
        layer_parameter_board.setVisible(false);
        layer_list.setExpanded(true);
        layer_list.setVerticalGap(Double.valueOf(15.0));
        layer_list.depthProperty().set(5);
        layer_list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (!layer_parameter_board.isVisible()) layer_parameter_board.setVisible(true);

            }
        });
    }

    @FXML
    void AddLSTMLayer(MouseEvent event) {
        Label layer = new Label("LSTM");
        LayerItem layerItem = new LayerItem();
        layerItems.add(layerItem);
        layer.setTextFill(Paint.valueOf("#ffffff"));
        layer_list.getItems().add(layer);
    }

    @FXML
    void backToPre(MouseEvent event) {
        Stage nnStage = StartProcess.hashMap.remove("rnn_param_settings");
        Stage codeStage = StartProcess.hashMap.get("nn_choose_board");
        nnStage.close();
        codeStage.show();
    }

    @FXML
    void complete(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../FXML/nn_graph.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        StartProcess.hashMap.put("nn_graph", stage);
        StartProcess.hashMap.get("rnn_param_settings").hide();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void deleteLayer(MouseEvent event) {

    }

    @FXML
    void resetParameters(MouseEvent event) {

    }

}

