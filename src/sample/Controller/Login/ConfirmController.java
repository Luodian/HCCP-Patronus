package sample.Controller.Login;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import sample.StartProcess;

public class ConfirmController {
    @FXML
    public JFXButton click_return;

    @FXML
    public void returnToLogin(){
        Stage stage = StartProcess.hashMap.remove("confirm");
        stage.close();
        StartProcess.hashMap.get("login").show();
    }
}
