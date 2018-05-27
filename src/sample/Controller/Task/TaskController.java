package sample.Controller.Task;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXMasonryPane;
import com.jfoenix.controls.JFXProgressBar;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.Controller.Login.LoginController;
import sample.Entity.ComputeTask;
import sample.Entity.TaskPaneItem;
import sample.Entity.UserNode;
import sample.SocketConnect.SocketHandler;
import sample.StartProcess;
import sample.Utils.HintFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TaskController implements Initializable {

    @FXML
    private JFXButton new_task;

    @FXML
    private JFXButton deleteTask;

    @FXML
    private JFXButton back_to_main;

    @FXML
    private JFXListView my_master_list;

    @FXML
    private JFXButton bind_group;

    public static int MAX_SHOW_CARD = 4;
    @FXML
    private JFXMasonryPane masonry_pane_1;//此为我发动的活动在其他主机上的任务

    @FXML
    private JFXListView my_task_list;

    @FXML
    private Label data_type;

    @FXML
    private Label initiator;

    @FXML
    private JFXButton play;

    @FXML
    private JFXButton show_code;

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

    public static ArrayList<ComputeTask> workingTasks = new ArrayList<ComputeTask>();

    public static JFXListView my_task_list_copy;

    public static JFXListView my_master_list_copy;

    public static ArrayList<TaskPaneItem> slave_item_panes = new ArrayList<TaskPaneItem>();

    public static ArrayList<TaskPaneItem> master_item_panes = new ArrayList<TaskPaneItem>();

    public static JFXMasonryPane slave_masonry;

    public static JFXMasonryPane master_masonry;

    @FXML
    private JFXMasonryPane masonry_pane_2;//此为活动在我这台主机上的任务

    public static Pane newWorkingTask(TaskPaneItem taskPaneItem) {
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: #63B8FF; -fx-background-radius: 2em;");
        pane.setPrefSize(130, 140);
        pane.effectProperty().setValue(new DropShadow());

        Label initiator = new Label("master: " + taskPaneItem.getUser_name());
        initiator.setLayoutX(49);
        initiator.setLayoutY(36);
        initiator.setFont(new Font("Chalkboard SE Light", 12.0));
        initiator.setTextFill(Paint.valueOf("#ffffff"));

        Label data_name = new Label("data: " + taskPaneItem.getData_name());
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

    @FXML
    void runTask(MouseEvent event) {
        int index = my_task_list.getSelectionModel().getSelectedIndex();
        if (slave_item_panes.size() > 0) {
            HintFrame.showFailFrame("Please wait util current task finish!");
            return;
        }
        if (index < 0) {
            HintFrame.showFailFrame("Please choose one of your tasks!");
            return;
        } else {
            ComputeTask computeTask = myTasks.get(index);
            if (SocketHandler.runTask(computeTask.getTask_id())) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        /**每个user同一时刻只允许启动一个任务，只有在启动时刻会加载my slaves部分UI**/
                        if (slave_item_panes.size() > 0) {
                            HintFrame.showFailFrame("There is a task running currently!");
                            return;
                        } else {
                            slave_item_panes = SocketHandler.getWorkingSlaves(LoginController.current_user_id);
                            for (TaskPaneItem e : slave_item_panes) {
                                Pane slave = newWorkingTask(e);
                                e.setShowPane(slave);
                            }
                        }
                        int len = (MAX_SHOW_CARD > slave_item_panes.size()) ? slave_item_panes.size() : MAX_SHOW_CARD;
                        for (int i = 0; i < len; i++)
                            masonry_pane_1.getChildren().add(slave_item_panes.get(i).getShowPane());
                        if (slave_item_panes.size() > MAX_SHOW_CARD) showMoreLabel(1);
                    }
                });
                Stage stage = new Stage();
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("../../FXML/task_complete.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stage.setScene(new Scene(root));
                StartProcess.hashMap.put("task_complete", stage);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setResizable(false);
                Timer timer = new Timer(20000, new TimerAction(stage));
                timer.start();
            }
        }
    }

    @FXML
    void bindGroup(MouseEvent event) throws IOException {
        int index = my_task_list.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            HintFrame.showFailFrame("Please choose one of your tasks!");
        } else {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../../FXML/group_choose_board.fxml"));
            stage.setScene(new Scene(root));
            StartProcess.hashMap.put("group_choose_board", stage);
            StartProcess.hashMap.get("tasks").hide();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setResizable(false);
            stage.show();
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
        int index = my_task_list.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            HintFrame.showFailFrame("Please choose one of your tasks!");
        } else {

        }
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        taskPane = task_pane;
        my_task_list_copy = my_task_list;
        master_masonry = masonry_pane_2;
        my_master_list_copy = my_master_list;
        slave_masonry = masonry_pane_1;

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
                    UserNode initiatoNode = null;

                    initiatoNode = SocketHandler.queryUserByID(temp.getInitiator_id());
                    if (initiatoNode != null) temp.setInitiator (initiatoNode);
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

        /**先获得当前用户的所有状态的任务**/
        myTasks = SocketHandler.queryComputeTaskByInitiatorIDAndState(LoginController.current_user_id, -1);
        if (myTasks != null && myTasks.size() != 0){
            for (int i = 0; i < myTasks.size(); i++) {
                Label task = new Label(myTasks.get(i).getTask_name());
                task.setTextFill(Paint.valueOf("#ffffff"));
                my_task_list.getItems().add(task);
            }
        }
        /**服务的master任务信息**/
        for (int i = 0; i < workingTasks.size(); i++) {
            Label task = new Label(workingTasks.get(i).getTask_name());
            task.setTextFill(Paint.valueOf("#ffffff"));
            my_master_list.getItems().add(task);
        }

        int len1 = (MAX_SHOW_CARD > slave_item_panes.size()) ? slave_item_panes.size() : MAX_SHOW_CARD;
        for (int i = 0; i < len1; i++) masonry_pane_1.getChildren().add(slave_item_panes.get(i).getShowPane());
        if (slave_item_panes.size() > MAX_SHOW_CARD) showMoreLabel(1);

        int len2 = (MAX_SHOW_CARD > slave_item_panes.size()) ? slave_item_panes.size() : MAX_SHOW_CARD;
        for (int i = 0; i < len2; i++) masonry_pane_2.getChildren().add(slave_item_panes.get(i).getShowPane());
        if (slave_item_panes.size() > MAX_SHOW_CARD) showMoreLabel(2);


        /**工作的节点进度卡片信息最多在这个页面显示3个
         * 更多的信息可以进入"see all cards..."界面看**/
    }

    public void showMoreLabel(int i) {
        if (i == 1) {
            Label label1 = new Label("more...");
            label1.setPrefSize(50, 150);
            label1.setAlignment(Pos.CENTER);
            label1.setFont(new Font("Chalkboard SE Light", 15.0));
            label1.setTextFill(Paint.valueOf("#ffffff"));
            masonry_pane_1.getChildren().add(label1);
        } else if (i == 2) {
            Label label1 = new Label("more...");
            label1.setPrefSize(50, 150);
            label1.setAlignment(Pos.CENTER);
            label1.setFont(new Font("Chalkboard SE Light", 15.0));
            label1.setTextFill(Paint.valueOf("#ffffff"));
            masonry_pane_2.getChildren().add(label1);
        }
    }

    public Pane createSeeAllCard(EventHandler eventHandler) {
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: #63B8FF; -fx-background-radius: 2em;");
        pane.setPrefSize(130, 140);
        pane.effectProperty().setValue(new DropShadow());

        Label initiator = new Label("see all cards...");
        initiator.setLayoutX(25);
        initiator.setLayoutY(75);
        initiator.setFont(new Font("Chalkboard SE Light", 12.0));
        initiator.setTextFill(Paint.valueOf("#ffffff"));

        pane.getChildren().add(initiator);
        return pane;
    }

    @FXML
    void showCode(MouseEvent event) throws InterruptedException {

    }
}


class TimerAction implements ActionListener {
    Stage stage;

    public TimerAction(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        int index = TaskController.my_task_list_copy.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            HintFrame.showFailFrame("Please choose one of your tasks!");
        } else {
            for (TaskPaneItem e : TaskController.master_item_panes
                    ) {
                if (SocketHandler.sendToMasterTaskEndMsg(e.getTask_id(), e.getUser_id(), LoginController.current_user_id,
                        e.getData_name())) {
                    /**若正确结束，则去除pane**/
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            TaskController.master_masonry.getChildren().remove(e.getShowPane());
                        }
                    });

                }
            }
            TaskController.master_item_panes.clear();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    stage.show();
                }
            });
        }
    }
}