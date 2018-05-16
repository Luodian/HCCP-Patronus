package sample.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.StartProcess;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MainPageController implements Initializable {
    @FXML
    private JFXButton user_information;

    @FXML
    private JFXButton task;

    @FXML
    private JFXButton data_set;

    @FXML
    private JFXButton groups;

    @FXML
    private JFXButton coding;

    @FXML
    private JFXButton quit;

    @FXML
    private AnchorPane main_pane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        JFXButton func_module = new JFXButton("+");
        func_module.setButtonType(JFXButton.ButtonType.RAISED);
        func_module.getStyleClass().addAll("animated-option-button", "animated-option-sub-button1");

        ImageView user = new ImageView("/sample/resources/MDicon/creator.png");
        user.setFitHeight(25.0);
        user.setFitWidth(25.0);
        JFXButton module_user_info = new JFXButton("", user);
        module_user_info.setButtonType(JFXButton.ButtonType.RAISED);
        module_user_info.getStyleClass().addAll("animated-option-button", "animated-option-sub-button2");

        ImageView dataset = new ImageView("/sample/resources/MDicon/insert_data.png");
        dataset.setFitHeight(25.0);
        dataset.setFitWidth(25.0);
        JFXButton dataload = new JFXButton("", dataset);
        dataload.setButtonType(JFXButton.ButtonType.RAISED);
        dataload.getStyleClass().addAll("animated-option-button", "animated-option-sub-button2");

        ImageView group = new ImageView("/sample/resources/MDicon/groups.png");
        group.setFitHeight(25.0);
        group.setFitWidth(25.0);
        JFXButton groupButton = new JFXButton("", group);
        groupButton.setButtonType(JFXButton.ButtonType.RAISED);
        groupButton.getStyleClass().addAll("animated-option-button", "animated-option-sub-button2");

        ImageView tasks = new ImageView("/sample/resources/MDicon/task_icon.png");
        tasks.setFitHeight(25.0);
        tasks.setFitWidth(25.0);
        JFXButton taskButton = new JFXButton("", tasks);
        taskButton.setButtonType(JFXButton.ButtonType.RAISED);
        taskButton.getStyleClass().addAll("animated-option-button", "animated-option-sub-button2");

        ImageView coding = new ImageView("/sample/resources/MDicon/coding_white.png");
        coding.setFitHeight(25.0);
        coding.setFitWidth(25.0);
        JFXButton codingButton = new JFXButton("", coding);
        codingButton.setButtonType(JFXButton.ButtonType.RAISED);
        codingButton.getStyleClass().addAll("animated-option-button", "animated-option-sub-button2");


        JFXNodesList modules = new JFXNodesList();

        modules.setRotate(180.0);
        modules.setSpacing(10);
        modules.addAnimatedNode(func_module);
        modules.addAnimatedNode(module_user_info);
        modules.addAnimatedNode(taskButton);
        modules.addAnimatedNode(dataload);
        modules.addAnimatedNode(groupButton);
        modules.addAnimatedNode(codingButton);

        main_pane.getChildren().add(modules);
        AnchorPane.setRightAnchor(modules, 30.0);
        AnchorPane.setBottomAnchor(modules, 20.0);
    }

    @FXML
    void showUserInfo(MouseEvent event){

    }

    @FXML
    void showTasks(MouseEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../FXML/task_page.fxml"));
        root.getStylesheets().add(getClass().getResource("../FXML/task_list_style.css").toExternalForm());
        stage.setScene(new Scene(root));
        StartProcess.hashMap.put("tasks", stage);
        StartProcess.hashMap.get("main_page").hide();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void showGroups(MouseEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../FXML/group.fxml"));
        root.getStylesheets().add(getClass().getResource("../FXML/list_view.css").toExternalForm());
        stage.setScene(new Scene(root));
        StartProcess.hashMap.put("groups", stage);
        StartProcess.hashMap.get("main_page").hide();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void showCodingPage(MouseEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../FXML/EditCodePage.fxml"));
        root.getStylesheets().add(getClass().getResource("../FXML/list_view.css").toExternalForm());
        stage.setScene(new Scene(root));
        StartProcess.hashMap.put("coding", stage);
        StartProcess.hashMap.get("main_page").hide();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void showDatas(MouseEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../FXML/data_load.fxml"));
        root.getStylesheets().add(getClass().getResource("../FXML/list_view.css").toExternalForm());
        stage.setScene(new Scene(root));
        StartProcess.hashMap.put("data_load", stage);
        StartProcess.hashMap.get("main_page").hide();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void quit(MouseEvent event){
        Stage main_stage = StartProcess.hashMap.remove("main_page");
        Stage login_stage = StartProcess.hashMap.remove("login");
        main_stage.close();
        login_stage.close();
        System.exit(0);
    }
}
