package sample.Controller.Group;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import sample.AppException.UserIdNotFindException;
import sample.Entity.GroupNode;
import sample.Entity.UserNode;
import sample.SocketConnect.SocketHandler;
import sample.StartProcess;

import java.sql.Date;
import java.time.LocalDate;

public class GroupSettingController {

    @FXML
    private JFXButton import_data;

    @FXML
    private JFXButton submit;

    @FXML
    private JFXButton reset_data;

    @FXML
    private JFXButton back_to_preview;

    @FXML
    private JFXTextField data_name;

    @FXML
    private ChoiceBox<?> type_choice;

    @FXML
    private JFXTextField user_name_field;

    @FXML
    private JFXPasswordField password_field;

    @FXML
    private JFXTextArea description_area;

    @FXML
    void backToPrevious(MouseEvent event) {
        Stage stage = StartProcess.hashMap.remove("group_setting");
        stage.close();
        StartProcess.hashMap.get("groups").show();
    }

    @FXML
    void importData(MouseEvent event) {

    }

    @FXML
    void resetData(MouseEvent event) {

    }

    @FXML
    void submit(MouseEvent event) throws Throwable {
        /**提交数据库登记
         * 首先根据email和密码向数据库查询是否存在该用户，
         * 若存在，则返回其用户id，否则返回NOTFIND
         **/
        String group_name = data_name.getText();
        String user_name = user_name_field.getText();//实际上email
        String password = password_field.getText();
        String description = description_area.getText();
        UserNode userNode = new UserNode();
        userNode.setUser_name(user_name);
        userNode.setEmail(user_name);
        userNode.setPassword(password);

        String user_id = SocketHandler.isUserExistedByUserNode(userNode.getEmail(), userNode.getPassword());
        if (!(user_id).equals("NOTFIND")){
            /**
             *  若得到ID，正常建立
             *  type暂时设置为unknown
             *  **/
            userNode.setUser_id(user_id);

            GroupNode group = new GroupNode();
            group.setGroup_name(group_name);//设置群组的名字
            group.setOwner_id(user_id);//登录时维护一个UserNode
            group.setDescription(description);//设置群组描述
            group.setMember_num(1);//只有创建者自己

            group.setType("unknown");
            group.setCreat_date(Date.valueOf(LocalDate.now()));//设置日期为当前日期


            /**向数据库提交，需要向GROUPS和BELONGS_TO两张表添加元组**/
            group.setGroup_id(SocketHandler.insertGroup(group));
            if (!group.getGroup_id().equals("EXCEPTION") && !group.getGroup_id().equals("NOTFIND")) {
                Stage stage = StartProcess.hashMap.remove("group_setting");
                stage.close();
                StartProcess.hashMap.get("groups").show();

                /**添加owner和该group之间的联系,
                 * 其实我觉得如果这里插入失败，需要将上面的添加操作回滚，但是算了懒得弄了**/
                if (!SocketHandler.insertGroupUserRelation(user_id, group.getGroup_id()))
                    throw new Throwable("GROUP_USER_RELATION INSERT FAILED");

                /**向myGroup列表添加元素
                 * 别忘了获取owner**/
                UserNode owner = SocketHandler.queryUserByID(user_id);
                group.setOwner(owner);
                GroupController.myGroupsCopy.add(group);
                Label append_group = new Label(group.getGroup_name());
                append_group.setTextFill(Paint.valueOf("#ffffff"));
                GroupController.my_Groups_copy.getItems().add(append_group);
            }
            else throw new Throwable("GROUP INSERT FAILED");
        }
        else{
            throw new UserIdNotFindException();
        }
    }

}
