package sample.Controller.Login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONException;
import sample.Datebase.SQLHandler;
import sample.Entity.UserNode;
import sample.StartProcess;

import java.io.IOException;
import java.sql.SQLException;


public class LoginController {
    @FXML
    public JFXButton sign_up;
    @FXML
    public JFXButton sign_in;
    @FXML
    public JFXTextField user_email;
    @FXML
    public JFXPasswordField user_password;

    public static String current_user_id;

    @FXML
    public void SignUp() throws IOException {//注册
            String email = user_email.getText();
            String password = user_password.getText();
            String user_id = String.valueOf((int)(Math.random()* 1000000000));
            String user_name = email;
            try {
                SQLHandler.insertUser(new UserNode(user_name, password, email, user_id));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Parent p = FXMLLoader.load(getClass().getResource("../FXML/confirm.fxml"));
            Stage confirmStage = new Stage();
            Scene scene = new Scene(p, 421, 244);
            confirmStage.setScene(scene);
            confirmStage.setResizable(false);
            confirmStage.initStyle(StageStyle.UNDECORATED);
            StartProcess.hashMap.put("confirm", confirmStage);
            StartProcess.hashMap.get("login").hide();
            confirmStage.show();
    }
    @FXML
    public void SignIn () throws IOException, SQLException, JSONException
    {
        String email = user_email.getText();
        String password = user_password.getText();
        Parent root = null;


        if (!(current_user_id = SQLHandler.isUserExistedByUserNode(new UserNode(email, password))).equals("NOTFIND"))
//	    if (!(current_user_id = SocketHandler.sign_in (email, password)).equals ("NOTFIND"))
	    {
            Stage stage = new Stage();
            root = FXMLLoader.load(getClass().getResource("../../FXML/main.fxml"));
            root.getStylesheets().add(getClass().getResource("../../FXML/floating_button.css").toExternalForm());
            stage.setScene(new Scene(root));
            StartProcess.hashMap.put("main_page", stage);
            StartProcess.hashMap.get("login").hide();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setResizable(false);
            stage.show();
        }
    }
}
