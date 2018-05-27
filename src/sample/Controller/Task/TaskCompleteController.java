package sample.Controller.Task;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.StartProcess;

public class TaskCompleteController {

    @FXML
    private JFXButton close;

    @FXML
    private JFXButton ok;

    @FXML
    private JFXButton back1;

    @FXML
    void backToPre(MouseEvent event) {
        Stage nnStage = StartProcess.hashMap.remove("task_complete");
        nnStage.close();
    }

    @FXML
    void close(MouseEvent event) {
        Stage nnStage = StartProcess.hashMap.remove("task_complete");
        nnStage.close();
    }

}

