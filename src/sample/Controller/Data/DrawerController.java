package sample.Controller.Data;
import com.jfoenix.controls.JFXColorPicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import sample.Controller.Data.DataLoadController;

import java.net.URL;
import java.util.ResourceBundle;

public class DrawerController implements Initializable{

        @FXML
        private VBox vbox;

        @FXML
        private JFXColorPicker color_picker_1;

        @FXML
        private JFXColorPicker color_picker_2;

        @FXML
        private JFXColorPicker color_picker_3;

        @FXML
        private JFXColorPicker color_picker_4;

        @FXML
        void color_picker_1(ActionEvent event) {
            Color color = color_picker_1.getValue();
            DataLoadController.menu_root.setBackground(new Background(new BackgroundFill(Paint.valueOf(color.toString()), CornerRadii.EMPTY, Insets.EMPTY)));
            vbox.setBackground(new Background(new BackgroundFill(Paint.valueOf(color.toString()), CornerRadii.EMPTY, Insets.EMPTY)));
        }

        @FXML
        void color_picker_2(ActionEvent event) {
            Color color = color_picker_2.getValue();
            DataLoadController.listView_pane.setBackground(new Background(new BackgroundFill(Paint.valueOf(color.toString()), CornerRadii.EMPTY, Insets.EMPTY)));
            DataLoadController.redundance.setBackground(new Background(new BackgroundFill(Paint.valueOf(color.toString()), CornerRadii.EMPTY, Insets.EMPTY)));
        }

        @FXML
        void color_picker_3(ActionEvent event) {
            Color color = color_picker_3.getValue();
            DataLoadController.dataHeader.setBackground(new Background(new BackgroundFill(Paint.valueOf(color.toString()), CornerRadii.EMPTY, Insets.EMPTY)));
        }

        @FXML
        void color_picker_4(ActionEvent event) {
            Color color = color_picker_4.getValue();
            DataLoadController.func.setBackground(new Background(new BackgroundFill(Paint.valueOf(color.toString()), CornerRadii.EMPTY, Insets.EMPTY)));
        }

    public VBox getVbox() {
        return vbox;
    }

    public JFXColorPicker getColor_picker_1() {
        return color_picker_1;
    }

    public JFXColorPicker getColor_picker_2() {
        return color_picker_2;
    }

    public JFXColorPicker getColor_picker_3() {
        return color_picker_3;
    }

    public JFXColorPicker getColor_picker_4() {
        return color_picker_4;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        color_picker_1.setValue(Color.valueOf("#BA2E49"));
        color_picker_2.setValue(Color.valueOf("#C0324C"));
        color_picker_3.setValue(Color.valueOf("#E34F43"));
        color_picker_4.setValue(Color.valueOf("#BA2E49"));
    }
}
