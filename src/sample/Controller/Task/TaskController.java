package sample.Controller.Task;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXMasonryPane;
import com.jfoenix.controls.JFXProgressBar;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.Controller.Login.LoginController;
import sample.Datebase.SQLHandler;
import sample.Entity.ComputeTask;
import sample.Entity.GroupNode;
import sample.Entity.UserNode;
import sample.StartProcess;
import sample.Utils.HintFrame;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
    private JFXListView worked_task_list;

    @FXML
    private JFXMasonryPane masonry_pane;

    @FXML
    private JFXListView my_task_list;

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

    @FXML
    private AnchorPane task_pane;

    public static AnchorPane taskPane;

    public static ArrayList<ComputeTask> myTasks;

    public static ArrayList<ComputeTask> workingTasks;

    public static JFXListView my_task_list_copy;
    public static JFXListView worked_task_list_copy;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        taskPane = task_pane;
        my_task_list_copy = my_task_list;
        worked_task_list_copy = worked_task_list;

        /**设置my_task_list及worked_task_list属性**/
        my_task_list.setExpanded(true);
        my_task_list.setVerticalGap(Double.valueOf(15.0));
        my_task_list.depthProperty().set(5);
        my_task_list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                int index = my_task_list.getSelectionModel().getSelectedIndex();
                if (index >= 0){
                    ComputeTask temp = myTasks.get(index);
                    /**获取发起者信息**/
                    UserNode initiatoNode = SQLHandler.queryUserByID(temp.getInitiator_id());
                    if (initiatoNode != null) temp.setInitiator(initiatoNode);
                    else HintFrame.showFailFrame("Can't find the initiator!");
                    initiator.setText(temp.getInitiator().getUser_name());
                    data_type.setText(temp.getData_type());
                    cost.setText(String.valueOf(temp.getCost()));
                    score.setText(String.valueOf(temp.getSecurity_score()));
                    /**根据状态值进行判断
                     * 若state = 0，则表示任务未启动，则其start_time和end_time字段都为"task not start yet!"
                     * 若state = 1，则表示任务启动但是未完成，则其start_time有值，但end_time字段值为"task is running!"
                     * 若state = 2，则表示任务已完成，则其start_time和end_time字段均有值**/
                    int state = temp.getState();
                    switch (state){
                        case 0:{
                            start_time.setText("task not start yet!");
                            end_time.setText("task not start yet!");
                            break;
                        }
                        case 1:{
                            start_time.setText(temp.getStart_time());
                            end_time.setText("task is running!");
                            break;
                        }
                        case 2:{
                            start_time.setText(temp.getStart_time());
                            end_time.setText(temp.getEnd_time());
                            break;
                        }
                        default:HintFrame.showFailFrame("Unknown Error!");
                    }
                }
            }
        });

        worked_task_list.setExpanded(true);
        worked_task_list.setVerticalGap(Double.valueOf(15.0));
        worked_task_list.depthProperty().set(5);
        worked_task_list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {

            }
        });


        /**先获得当前用户的所有状态的任务**/
        myTasks = SQLHandler.queryComputeTaskByInitiatorIDAndState(LoginController.current_user_id,-1);

        if (myTasks != null && myTasks.size() != 0){
            for (int i = 0; i < myTasks.size(); i++) {
                Label task = new Label(myTasks.get(i).getTask_name());
                task.setTextFill(Paint.valueOf("#ffffff"));
                my_task_list.getItems().add(task);
            }
        }


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
        Parent p = null;
        try {
            p = FXMLLoader.load(getClass().getResource("../../FXML/task_setting.fxml"));
            Stage settingStage = new Stage();
            Scene scene = new Scene(p, 600, 400);
            settingStage.setScene(scene);
            settingStage.setResizable(false);
            settingStage.initStyle(StageStyle.UNDECORATED);
            StartProcess.hashMap.put("task_setting", settingStage);
            StartProcess.hashMap.get("tasks").hide();
            settingStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Pane newWorkingTask(String initiator_name, String data_name_1){
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: #63B8FF; -fx-background-radius: 2em;");
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
