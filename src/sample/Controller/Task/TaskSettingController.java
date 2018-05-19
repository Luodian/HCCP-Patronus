package sample.Controller.Task;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Controller.Login.LoginController;
import sample.Entity.ComputeTask;
import sample.SocketConnect.SocketHandler;
import sample.StartProcess;
import sample.Utils.HintFrame;

import java.io.File;

public class TaskSettingController {

    @FXML
    private JFXButton import_data;

    @FXML
    private JFXButton complete;

    @FXML
    private JFXButton reset_data;

    @FXML
    private JFXButton back_to_preview;

    @FXML
    private JFXTextField task_name;

    @FXML
    private ChoiceBox<?> type_choice;

    @FXML
    private JFXTextField file_path;

    @FXML
    private Button file_chooser;

    @FXML
    void backToPrevious(MouseEvent event) {
        Stage stage = StartProcess.hashMap.remove("task_setting");
        stage.close();
        StartProcess.hashMap.get("tasks").show();
    }

    @FXML
    void importData(MouseEvent event) {

    }

    @FXML
    void resetData(MouseEvent event) {

    }

    @FXML
    void task(MouseEvent event) {
        String name = task_name.getText();
        /**这个地方有个问题
         * 就是具体代码在数据库里怎么存**/
        String filePath = file_path.getText();

        if (name == null||name.equals("")){
            HintFrame.showFailFrame("Empty task name!");
        }
        else {
            ComputeTask computeTask = new ComputeTask();
            computeTask.setTask_id(String.valueOf((int)(Math.random()* 1000000000)));
            computeTask.setData_type("unknown");
            computeTask.setCost(0);
            computeTask.setSecurity_score(0);
            computeTask.setInitiator_id(LoginController.current_user_id);
            computeTask.setState(0);
            computeTask.setTask_name(name);
            if (SocketHandler.insertComputeTask(computeTask)) {
                HintFrame.showSuccessFrame("ComputeTask insert successfully!");
                /**需要更新tasks页面中list的信息**/
                Label newTask = new Label(name);
                newTask.setTextFill(Paint.valueOf("#ffffff"));
                TaskController.my_task_list_copy.getItems().add(newTask);
                TaskController.myTasks.add(computeTask);
                /**返回之前的页面**/
                Stage stage = StartProcess.hashMap.remove("task_setting");
                stage.close();
                StartProcess.hashMap.get("tasks").show();
            }
            else HintFrame.showFailFrame("ComputeTask insert failed!");
        }
    }

    @FXML
    void chooseFile(MouseEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(TaskController.taskPane.getScene().getWindow());
        if (file != null){
            file_path.setText(file.getPath());
        }
        else {
            HintFrame.showFailFrame("File open failed!");
        }
    }

}
