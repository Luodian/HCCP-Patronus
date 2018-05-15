package sample.Controller.Task;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXMasonryPane;
import com.jfoenix.controls.JFXProgressBar;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.StartProcess;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class TaskController implements Initializable {

    @FXML
    private JFXButton new_task;

    @FXML
    private JFXButton deleteTask;

    @FXML
    private JFXButton back_to_main;

    @FXML
    private JFXListView<?> worked_task_list;

    @FXML
    private JFXMasonryPane masonry_pane;

    @FXML
    private JFXListView<?> my_task_list;

    @FXML
    private Label data_type;

    @FXML
    private Label initiator;

    @FXML
    private Label cost;

    @FXML
    private Label start_time;

    @FXML
    private Label end_time;

    @FXML
    private Label score;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (int i = 0; i < 8; i++) {
             Pane pane = newWorkingTask("initiator", "data name");
             masonry_pane.getChildren().add(pane);
        }
    }

    @FXML
    void backToMain(MouseEvent event) {
        Stage main_stage = StartProcess.hashMap.get("main_page");
        Stage group_stage = StartProcess.hashMap.remove("tasks");
        group_stage.close();
        main_stage.show();
    }

    @FXML
    void deleteTask(MouseEvent event) {

    }

    @FXML
    void newTask(MouseEvent event) {

    }

    public Pane newWorkingTask(String initiator_name, String data_name_1){
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: #7213a0; -fx-background-radius: 2em;");
        pane.setPrefSize(130, 140);
        pane.effectProperty().setValue(new DropShadow());

        Label initiator = new Label(initiator_name);
        initiator.setLayoutX(49);
        initiator.setLayoutY(36);
        initiator.setFont(new Font("Chalkboard SE Light", 12.0));
        initiator.setTextFill(Paint.valueOf("#ffffff"));

        Label data_name = new Label(data_name_1);
        data_name.setLayoutX(50);
        data_name.setLayoutY(71);
        data_name.setFont(new Font("Chalkboard SE Light", 12.0));
        data_name.setTextFill(Paint.valueOf("#ffffff"));


        ImageView initiator_image = new ImageView("/sample/resources/MDicon/person.png");
        initiator_image.setFitWidth(33);
        initiator_image.setFitHeight(31);
        initiator_image.setLayoutX(20);
        initiator_image.setLayoutY(30);
        initiator_image.setPreserveRatio(true);
        initiator_image.setPickOnBounds(true);

        ImageView data_image = new ImageView("/sample/resources/MDicon/bookmark.png");
        data_image.setFitWidth(33);
        data_image.setFitHeight(31);
        data_image.setLayoutX(20);
        data_image.setLayoutY(65);
        data_image.setPreserveRatio(true);
        data_image.setPickOnBounds(true);

        JFXProgressBar progressBar = new JFXProgressBar();
        progressBar.setPrefSize(118, 7);
        progressBar.setLayoutX(12);
        progressBar.setLayoutY(123);

        pane.getChildren().add(initiator);
        pane.getChildren().add(data_name);
        pane.getChildren().add(initiator_image);
        pane.getChildren().add(data_image);
        pane.getChildren().add(progressBar);

        return pane;
    }

}
