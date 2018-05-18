package sample.Controller.Task;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXMasonryPane;
import com.jfoenix.controls.JFXProgressBar;
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
import org.json.JSONException;
import sample.Controller.Login.LoginController;
import sample.Entity.ComputeTask;
import sample.Entity.UserNode;
import sample.SocketConnect.SocketHandler;
import sample.StartProcess;
import sample.Utils.HintFrame;

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

    public static ArrayList<ComputeTask> workingTasks;

    public static JFXListView my_task_list_copy;

    private ArrayList<Pane> slaves = new ArrayList<Pane>();

    private ArrayList<Pane> masters = new ArrayList<Pane>();

    @FXML
    private JFXMasonryPane masonry_pane_2;//此为活动在我这台主机上的任务

    @FXML
    void runTask(MouseEvent event) {
        int index = my_task_list.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            HintFrame.showFailFrame("Please choose one of your tasks!");
        } else {
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        taskPane = task_pane;
        my_task_list_copy = my_task_list;

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
                    try
                    {
                        initiatoNode = SocketHandler.queryUserByID (temp.getInitiator_id ());
                    } catch (JSONException e)
                    {
                        e.printStackTrace ();
                    } catch (IOException e)
                    {
                        e.printStackTrace ();
                    }
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
	    try
	    {
		    myTasks = SocketHandler.queryComputeTaskByInitiatorIDAndState (LoginController.current_user_id, -1);
	    } catch (JSONException e)
	    {
		    e.printStackTrace ();
	    } catch (IOException e)
	    {
		    e.printStackTrace ();
	    }
    
        if (myTasks != null && myTasks.size() != 0){
            for (int i = 0; i < myTasks.size(); i++) {
                Label task = new Label(myTasks.get(i).getTask_name());
                task.setTextFill(Paint.valueOf("#ffffff"));
                my_task_list.getItems().add(task);
            }
        }

        /**工作的节点进度卡片信息最多在这个页面显示3个
         * 更多的信息可以进入"see all cards..."界面看**/

        for (int i = 0; i < MAX_SHOW_CARD; i++) {
            Pane pane = newWorkingTask("initiator", "data name");
            slaves.add(pane);
            masonry_pane_1.getChildren().add(pane);
        }

        Label label1 = new Label("more...");
        label1.setPrefSize(50, 150);
        label1.setAlignment(Pos.CENTER);
        label1.setFont(new Font("Chalkboard SE Light", 15.0));
        label1.setTextFill(Paint.valueOf("#ffffff"));
        masonry_pane_1.getChildren().add(label1);


        for (int i = 0; i < MAX_SHOW_CARD; i++) {
            Pane pane = newWorkingTask("initiator", "data name");
            masonry_pane_2.getChildren().add(pane);
        }

        Label label2 = new Label("more...");
        label2.setPrefSize(50, 150);
        label2.setAlignment(Pos.CENTER);
        label2.setFont(new Font("Chalkboard SE Light", 15.0));
        label2.setTextFill(Paint.valueOf("#ffffff"));
        masonry_pane_2.getChildren().add(label2);
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
    void showCode(MouseEvent event) {
        int index = my_task_list.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            HintFrame.showFailFrame("Please choose one of your tasks!");
        } else {

        }
    }

//    Thread rand_drop = new Thread(new Runnable() {
//        @Override
//        public void run() {
//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
//                    boolean[] isfinish = new boolean[4];
//                    for (int i = 0; i < isfinish.length; i++) isfinish[i] = false;
//                    int running_task_num = 4;
//                    while (running_task_num > 0){
//                        masonry_pane_1.getChildren().clear();
//                        for (int i = 0; i < 4; i++) {
//                            Pane temp = slaves.get(i);
//                            if (isfinish[i]) {//若已经完成，则直接跳过
//                                masonry_pane_1.getChildren().add(temp);
//                                continue;
//                            }
//
//                            double rand = Math.random();
//                            if (rand < 0.0005){
//                                temp.getChildren().remove(4);//第四个是ProgressBar
//                                HintFrame.showSuccessFrame("slave: " + i + " complete!");
//
//                                ImageView initiator_image = new ImageView("/sample/resources/MDicon/check.png");
//                                initiator_image.setFitWidth(50);
//                                initiator_image.setFitHeight(50);
//                                initiator_image.setLayoutX(50);
//                                initiator_image.setLayoutY(98);
//                                initiator_image.setPreserveRatio(true);
//                                initiator_image.setPickOnBounds(true);
//                                temp.getChildren().add(initiator_image);
//                                isfinish[i] = true;
//                                running_task_num--;
//                            }
//                            masonry_pane_1.getChildren().add(temp);
//                        }
//                    }
//                    HintFrame.showSuccessFrame("Task finish!");
//                }
//            });
//        }
//    });
//            rand_drop.start();
}
