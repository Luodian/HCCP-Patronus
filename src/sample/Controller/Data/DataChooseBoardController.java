package sample.Controller.Data;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXMasonryPane;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.Controller.Login.LoginController;
import sample.Datebase.SQLHandler;
import sample.Entity.DataNode;
import sample.StartProcess;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DataChooseBoardController implements Initializable {
    @FXML
    private JFXButton choose;

    @FXML
    private JFXButton back;

    @FXML
    private JFXMasonryPane masonry_pane;

    private ArrayList<DataNode> datasets;

    private Pane current_pane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        datasets = SQLHandler.queryDataNodesByID(LoginController.current_user_id);
        if (datasets != null) {
            for (int i = 0; i < datasets.size(); i++) {
                Pane temp = createNewDataInfoPane(datasets.get(i));
                masonry_pane.getChildren().add(temp);
            }
        }
    }

    @FXML
    void backToPreview(MouseEvent event) {
        Stage dataChooseBoardStage = StartProcess.hashMap.remove("data_choose_board");
        Stage groupStage = StartProcess.hashMap.get("groups");
        dataChooseBoardStage.close();
        groupStage.show();
    }

    @FXML
    void chooseData(MouseEvent event) {

    }

    private Pane createNewDataInfoPane(DataNode temp) {
        Pane temp_info = new Pane();
        temp_info.setPrefSize(150, 150);
        temp_info.setStyle("-fx-background-color: #104E8B;-fx-background-radius: 1em");
        temp_info.setEffect(new DropShadow(2, 2, 2, Color.BLACK));

        JFXButton temp_button = new JFXButton("");
        temp_button.setPrefSize(150, 150);
        temp_button.setStyle("-fx-background-radius: 1em;");
        /**
         * 这个ripper效果满分
         **/
        List<Stop> stop_list = new ArrayList<Stop>();
        stop_list.add(new Stop(1.0, Color.valueOf("#0ea5d6")));
        stop_list.add(new Stop(0, Color.BLACK));
        temp_button.setRipplerFill(new RadialGradient(0, 0, 0.5112044817927172,
                0.4568345323741007, 0.5, true, CycleMethod.NO_CYCLE, stop_list));
        temp_button.setLayoutX(0);
        temp_button.setLayoutY(0);
        temp_button.onMouseClickedProperty().setValue(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (current_pane != null)
                    current_pane.setStyle("-fx-background-color: #104E8B;-fx-background-radius: 1em;");
                current_pane = (Pane) temp_button.getParent();
                current_pane.setStyle("-fx-background-color: #0ea5d6;-fx-background-radius: 1em");
            }
        });

        Label data_name = new Label("data name: " + temp.getData_name());
        data_name.setFont(new Font("Chalkboard SE Light", 13.0));
        data_name.setTextFill(Paint.valueOf("#ffffff"));
        data_name.setPrefSize(150, 20);
        data_name.setLayoutX(30);
        data_name.setLayoutY(30);
        data_name.setAlignment(Pos.CENTER_LEFT);

        ImageView data_name_image = new ImageView("/sample/resources/MDicon/score.png");
        data_name_image.setFitHeight(25);
        data_name_image.setFitWidth(25);
        data_name_image.setLayoutX(5);
        data_name_image.setLayoutY(27);

        Label data_type = new Label("data type: " + temp.getData_type());
        data_type.setFont(new Font("Chalkboard SE Light", 13.0));
        data_type.setTextFill(Paint.valueOf("#ffffff"));
        data_type.setPrefSize(150, 20);
        data_type.setLayoutX(30);
        data_type.setLayoutY(60);
        data_type.setAlignment(Pos.CENTER_LEFT);

        ImageView data_type_image = new ImageView("/sample/resources/MDicon/types.png");
        data_type_image.setFitHeight(25);
        data_type_image.setFitWidth(25);
        data_type_image.setLayoutX(5);
        data_type_image.setLayoutY(57);

        Label row_num = new Label("row num: " + temp.getRow_nums());
        row_num.setFont(new Font("Chalkboard SE Light", 13.0));
        row_num.setTextFill(Paint.valueOf("#ffffff"));
        row_num.setPrefSize(150, 20);
        row_num.setLayoutX(30);
        row_num.setLayoutY(90);
        row_num.setAlignment(Pos.CENTER_LEFT);

        ImageView row_num_image = new ImageView("/sample/resources/MDicon/icon_1.png");
        row_num_image.setFitHeight(25);
        row_num_image.setFitWidth(25);
        row_num_image.setLayoutX(5);
        row_num_image.setLayoutY(87);

        Label attr_num = new Label("attr num: " + temp.getAttr_nums());
        attr_num.setFont(new Font("Chalkboard SE Light", 13.0));
        attr_num.setTextFill(Paint.valueOf("#ffffff"));
        attr_num.setPrefSize(150, 20);
        attr_num.setLayoutX(30);
        attr_num.setLayoutY(120);
        attr_num.setAlignment(Pos.CENTER_LEFT);

        ImageView attr_num_image = new ImageView("/sample/resources/MDicon/attr.png");
        attr_num_image.setFitHeight(25);
        attr_num_image.setFitWidth(25);
        attr_num_image.setLayoutX(5);
        attr_num_image.setLayoutY(117);

        ImageView mark = new ImageView("/sample/resources/MDicon/bookmark.png");
        mark.setFitHeight(25);
        mark.setFitWidth(25);
        mark.setLayoutX(120);
        mark.setLayoutY(5);

        temp_info.getChildren().add(data_name);
        temp_info.getChildren().add(data_name_image);
        temp_info.getChildren().add(data_type);
        temp_info.getChildren().add(data_type_image);
        temp_info.getChildren().add(row_num);
        temp_info.getChildren().add(row_num_image);
        temp_info.getChildren().add(attr_num);
        temp_info.getChildren().add(attr_num_image);
        temp_info.getChildren().add(mark);
        temp_info.getChildren().add(temp_button);
        return temp_info;
    }
}
