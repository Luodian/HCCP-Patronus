package sample.Controller.Group;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.Entity.DataNode;
import sample.SocketConnect.SocketHandler;
import sample.StartProcess;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GroupInformationController implements Initializable {

    @FXML
    private Label groupName;

    @FXML
    private JFXButton know_it;

    @FXML
    private JFXTreeTableView registerd_dataset;

    @FXML
    void close(MouseEvent event) {
        Stage stage = StartProcess.hashMap.remove("group_information");
        stage.close();
    }

    public static ArrayList<DataNode> groups_datas;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int index = GroupController.my_Groups_copy.getSelectionModel().getSelectedIndex();
        String selected_group_id = GroupController.myGroupsCopy.get(index).getGroup_id();
        groups_datas = SocketHandler.queryRegisterdDataNodesByID(selected_group_id);
        if (groups_datas != null){
            /**查询成功
             * 首先，需要构建表，共4项属性，每个长度为75**/

            /*数据集名字*/
            JFXTreeTableColumn<DataNode, String> data_name = new JFXTreeTableColumn<>("data_name");
            data_name.setPrefWidth(75);
            data_name.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DataNode, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<DataNode, String> param) {
                    return new SimpleStringProperty(param.getValue().getValue().getData_name());
                }
            });
            /*数据集元组数目*/
            JFXTreeTableColumn<DataNode, String> row_nums = new JFXTreeTableColumn<>("tuple_nums");
            row_nums.setPrefWidth(75);
            row_nums.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DataNode, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<DataNode, String> param) {
                    int value = param.getValue().getValue().getRow_nums();
                    return new SimpleStringProperty(String.valueOf(value));
                }
            });
            /*数据集属性数目*/
            JFXTreeTableColumn<DataNode, String> attr_nums = new JFXTreeTableColumn<>("attr_nums");
            attr_nums.setPrefWidth(75);
            attr_nums.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DataNode, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<DataNode, String> param) {
                    int value = param.getValue().getValue().getAttr_nums();
                    return new SimpleStringProperty(String.valueOf(value));
                }
            });
            /*数据集拥有者名字*/
            JFXTreeTableColumn<DataNode, String> owner = new JFXTreeTableColumn<>("owner");
            owner.setPrefWidth(75);
            owner.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DataNode, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<DataNode, String> param) {
                    return new SimpleStringProperty(param.getValue().getValue().getUser_name());
                }
            });
            /*向表格中添加column*/
            registerd_dataset.getColumns().add(data_name);
            registerd_dataset.getColumns().add(row_nums);
            registerd_dataset.getColumns().add(attr_nums);
            registerd_dataset.getColumns().add(owner);
            final ObservableList<DataNode> dataLists = FXCollections.observableArrayList();
            for (DataNode e: groups_datas) dataLists.add(e);
            final TreeItem<DataNode> root = new RecursiveTreeItem<DataNode>(dataLists, RecursiveTreeObject::getChildren);
            registerd_dataset.setRoot(root);
            registerd_dataset.setShowRoot(false);
        }else {
            try {
                throw new Throwable("QUERY FAILED");
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
}
