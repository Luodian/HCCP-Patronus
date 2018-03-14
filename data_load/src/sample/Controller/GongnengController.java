package sample.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class GongnengController implements Initializable{
    @FXML
    public JFXDrawer drawer1;
    @FXML
    public JFXButton select_func_btn;
    @FXML
    public JFXButton select_node_btn;
    @FXML
    public JFXButton write_code_btn;
    @FXML
    public ImageView color_picker1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    @FXML
    public void selectFunc(MouseEvent mouseEvent) {
    }
    @FXML
    public void selectNode(MouseEvent mouseEvent) {
    }
    @FXML
    public void writeCode(MouseEvent mouseEvent) {
    }

    @FXML
    void colorPicker(MouseEvent event) {
        if (drawer1.isShown()) {
            drawer1.close();
        } else {
            drawer1.open();
        }
    }

}
