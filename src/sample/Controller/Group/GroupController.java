package sample.Controller.Group;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.Controller.Login.LoginController;
import sample.Datebase.SQLHandler;
import sample.Entity.GroupNode;
import sample.StartProcess;
import sample.Utils.HintFrame;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GroupController implements Initializable {

    @FXML
    private JFXButton group_search;

    @FXML
    private JFXButton new_group;

    @FXML
    private JFXButton quit_group;

    @FXML
    private Label theme_change;

    @FXML
    private JFXButton learn_more;

    @FXML
    private Pane group_info_pane;

    @FXML
    private Label members_num;

    @FXML
    private Label creator;

    @FXML
    private Label group_name;

    @FXML
    private Label create_date;

    @FXML
    private Label type;

    @FXML
    private Label description;

    @FXML
    private JFXListView my_Groups;

    private ArrayList<GroupNode> myGroupNodes;
    /**该静态成员是为了提供给其他类使用列表用的**/
    public static ArrayList<GroupNode> myGroupsCopy;

    public static JFXListView my_Groups_copy;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        myGroupNodes = SQLHandler.queryGroupsByUserID(LoginController.current_user_id);
        myGroupsCopy = myGroupNodes;
        my_Groups_copy = my_Groups;

        if (myGroupNodes != null && myGroupNodes.size() != 0){
            for (int i = 0; i < myGroupNodes.size(); i++) {
                Label group = new Label(myGroupNodes.get(i).getGroup_name());
                group.setTextFill(Paint.valueOf("#ffffff"));
                my_Groups.getItems().add(group);
            }
            my_Groups.setExpanded(true);
            my_Groups.setVerticalGap(Double.valueOf(15.0));
            my_Groups.depthProperty().set(5);
            my_Groups.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    int index = my_Groups.getSelectionModel().getSelectedIndex();
                    if (index < 0) return;
                    else {
                        /**选中列表中某个群组后，在右侧现实群组信息**/
                        GroupNode temp = myGroupNodes.get(index);
                        group_name.setText(temp.getGroup_name());
                        type.setText(temp.getType());
                        create_date.setText(temp.getCreat_date().toString());
                        description.setText(temp.getDescription());
                        members_num.setText(String.valueOf(temp.getMember_num()));
                        creator.setText(temp.getOwner().getUser_name());
                    }
                }
            });
        }
    }

    @FXML
    void learnMore(MouseEvent event) {
        try {
            int index = my_Groups.getSelectionModel().getSelectedIndex();
            if (index >= 0){
                Parent parent = FXMLLoader.load(getClass().getResource("../../FXML/more_information.fxml"));
                Stage group_information = new Stage();
                Scene scene = new Scene(parent);
                group_information.setScene(scene);
                group_information.setResizable(false);
                group_information.initStyle(StageStyle.UNDECORATED);
                StartProcess.hashMap.put("group_information", group_information);
                group_information.show();
            }
            else {
                HintFrame.showFailFrame("Please choose one group!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            HintFrame.showFailFrame("Open Failed!");
        }

    }

    @FXML
    void newGroup(MouseEvent event) throws IOException {
        Parent p = FXMLLoader.load(getClass().getResource("../../FXML/group_setting.fxml"));
        Stage settingStage = new Stage();
        Scene scene = new Scene(p, 600, 400);
        settingStage.setScene(scene);
        settingStage.setResizable(false);
        settingStage.initStyle(StageStyle.UNDECORATED);
        StartProcess.hashMap.put("group_setting", settingStage);
        StartProcess.hashMap.get("groups").hide();
        settingStage.show();
    }

    @FXML
    void quitGroup(MouseEvent event) {

    }

    @FXML
    void searchGroup(MouseEvent event) {

    }

    @FXML
    void themeChange(MouseEvent event) {

    }

    @FXML
    void registerData(MouseEvent event){
        try {
            int index = my_Groups.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                Parent root = FXMLLoader.load(getClass().getResource("../../FXML/data_load.fxml"));
                root.getStylesheets().add(getClass().getResource("../../FXML/list_view.css").toExternalForm());
                Stage dataStage = new Stage();
                Scene scene = new Scene(root, 1006, 740);
                dataStage.setScene(scene);
                dataStage.setResizable(false);
                dataStage.initStyle(StageStyle.UNDECORATED);
                StartProcess.hashMap.put("data_load", dataStage);
                StartProcess.hashMap.get("groups").hide();
                dataStage.show();
            }
            else {
                HintFrame.showFailFrame("Please choose one group!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void backToMain(MouseEvent event){
        Stage main_stage = StartProcess.hashMap.get("main_page");
        Stage group_stage = StartProcess.hashMap.remove("groups");
        group_stage.close();
        main_stage.show();
    }
}
