package sample.Controller.User;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.StartProcess;

public class UserInfoController {
    @FXML
    private JFXButton close;

    @FXML
    private Label name;

    @FXML
    private Label email;

    @FXML
    private Label group_num;

    @FXML
    private Label local_data_num;

    @FXML
    void closeCard(MouseEvent event) {
        Stage user_info_stage = StartProcess.hashMap.remove("user_info");
        Stage main_stage = StartProcess.hashMap.get("main_page");
        user_info_stage.close();
        main_stage.show();
    }
}
