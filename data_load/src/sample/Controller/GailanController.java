package sample.Controller;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXListView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.Main;
import sample.Entity.SmartNode;

import java.net.URL;
import java.util.ResourceBundle;

import static sample.Main.allNodes;

public class GailanController implements Initializable{

    @FXML
    public ImageView color_picker1;
    @FXML
    public JFXDrawer drawer1;
    @FXML
    public Pane network_pane;
    @FXML
    public JFXListView selectedData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        ObservableList<String> list = FXCollections.observableArrayList("黑龙江中医药","哈医大一附院","哈工大校医院","哈医大二附院");
        selectedData.setCellFactory(tv -> new PicCell());
        selectedData.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                StackPane stackPane = new StackPane();
                stackPane.getChildren().add(new Text(newValue.toString()));
                Stage stage = new Stage();
                stage.setScene(new Scene(stackPane, 400,300));
                stage.show();
            }
        });
        ObservableList<String> list = FXCollections.observableArrayList();
        selectedData.setItems(list);

        for(SmartNode node : allNodes) {
            node.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    list.add(node.tip.getText());
                    node.st.play();
                    node.ft.play();
                }
            });
        }
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
class PicCell extends ListCell<String> {
    @Override
    protected void updateItem(String item, boolean empty){
        super.updateItem(item, empty);
        if (!empty) {
            BorderPane cell = new BorderPane();
            Circle circle = new Circle(20);
            circle.setFill(new ImagePattern(new Image(Main.class.getResourceAsStream(item+".jpg"))));
            cell.setLeft(circle);
            cell.setCenter(new Text(item));
            setGraphic(cell);
//            setGraphic(circle);
//            setText(item);
//            System.out.println(item);
        }else{
            setGraphic(null);
        }
    }
}