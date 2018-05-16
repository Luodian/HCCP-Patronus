package sample.Controller.Data;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import sample.Controller.Group.GroupController;
import sample.Controller.Login.LoginController;
import sample.Datebase.SQLHandler;
import sample.Entity.DataNode;
import sample.Entity.DataRead;
import sample.Entity.DataItem;
import sample.Entity.GroupNode;
import sample.StartProcess;
import sample.Utils.HintFrame;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DataLoadController implements Initializable{
    @FXML
    private JFXListView listView;

    @FXML
    private ImageView item_style_change;

    @FXML
    private JFXTreeTableView tree_table;

    @FXML
    private ImageView color_picker1;

    @FXML
    private JFXDrawer drawer1;

    @FXML
    private JFXButton import_button;

    @FXML
    private JFXButton delete_button;

    @FXML
    private Pane redundancy;

    @FXML
    private AnchorPane an_menu;

    @FXML
    private AnchorPane root;

    @FXML
    private Pane function;

    @FXML
    private Pane data_header;

    public static AnchorPane menu_root;

    public static JFXListView listView_pane;

    public static Pane redundance;

    public static Pane func;

    public static Pane dataHeader;

    public static AnchorPane root_pane;

    public static  ArrayList<DataNode> data_sets;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /**
         *  get the file paths
         * */

        menu_root = an_menu;
        listView_pane = listView;
        redundance = redundancy;
        func = function;
        dataHeader = data_header;
        root_pane = root;

        try {
            VBox vBox = FXMLLoader.load(getClass().getResource("/sample/FXML/drawer.fxml"));
            drawer1.setSidePane(vBox);
        } catch (IOException e) {
            e.printStackTrace();
        }

        data_sets = SQLHandler.queryDataNodesByID(LoginController.current_user_id);

        for (int i = 0; i < data_sets.size(); i++) {
            Label file = new Label(data_sets.get(i).getData_name());
            file.setTextFill(Paint.valueOf("#ffffff"));
            listView.getItems().add(file);
        }
        listView.setExpanded(true);
        listView.setVerticalGap(Double.valueOf(15.0));
        listView.depthProperty().set(5);
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                int index = listView.getSelectionModel().getSelectedIndex();
                if (index < 0 ) return;
                DataRead dataRead = new DataRead(data_sets.get(index).getFile_path());
                List list = dataRead.readLines(11, DataRead.SEPARATOR_COMMA);
                int column = 0;
                int row = 0;
                try {
                    column = dataRead.getColumn();
                    row = dataRead.getRow();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

                for(int i = 0; i< column;i++){
                    JFXTreeTableColumn<DataItem, String> property = new JFXTreeTableColumn<>("Attr" + String.valueOf(i));
                    property.setPrefWidth(40);
                    final int idex = i;
                    property.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DataItem, String>, ObservableValue<String>>() {
                        @Override
                        public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<DataItem, String> param) {
                            return param.getValue().getValue().getProperty(idex);
                        }
                    });
                    tree_table.getColumns().add(property);
                }
                final ObservableList<DataItem> dataItems = FXCollections.observableArrayList();
                for (int i = 0; i< row;i++){
                    DataItem dataItem = new DataItem();
                    String[] attrs = (String[]) list.get(i);
                    for (int j = 0;j < column;j++){
                        dataItem.addAttr(attrs[j], DataItem.PROPERTY_TYPE_STRING);
                    }
                    dataItems.add(dataItem);
                }
                final TreeItem<DataItem> root = new RecursiveTreeItem<DataItem>(dataItems, RecursiveTreeObject::getChildren);
                tree_table.setRoot(root);
                tree_table.setShowRoot(false);
                HintFrame.showSuccessFrame("Load Successfully!");
            }
        });
    }

    @FXML
    private void choose(MouseEvent event) {
        /**选中该数据，将其交给群组注册**/
        int index_datasets = listView_pane.getSelectionModel().getSelectedIndex();
        int index_group = GroupController.my_Groups_copy.getSelectionModel().getSelectedIndex();
        if (index_datasets < 0 || index_group < 0) return;
        else {
            DataNode choosed_data = data_sets.get(index_datasets);
            GroupNode choosed_group = GroupController.myGroupsCopy.get(index_group);
            if (!SQLHandler.insertGroupDataRegisterRelation(choosed_group, choosed_data)){
                /**若插入失败,弹出失败提示框**/
                HintFrame.showFailFrame("Wrong type match or register duplicately!");
            }
            else {
                /**插入成功**/
                HintFrame.showSuccessFrame("Data has been registered successfully!");

                Stage primary = StartProcess.hashMap.get("primary");
                Stage data_load = StartProcess.hashMap.remove("data_load");
                primary.show();
                data_load.close();
            }
        }
    }

    @FXML
    private void importItem(MouseEvent event) throws IOException{
        Parent p = FXMLLoader.load(getClass().getResource("../../FXML/data_setting.fxml"));
        Stage settingStage = new Stage();
        Scene scene = new Scene(p, 600, 400);
        settingStage.setScene(scene);
        settingStage.setResizable(false);
        settingStage.initStyle(StageStyle.UNDECORATED);
        StartProcess.hashMap.put("data_setting", settingStage);
        StartProcess.hashMap.get("data_load").hide();
        settingStage.show();
    }

    @FXML
    private void itemStyleChange(MouseEvent event) {
        if (!listView.isExpanded()){
            listView.setExpanded(true);
            listView.setVerticalGap(Double.valueOf(15.0));
            listView.depthProperty().set(5);
        }
        else{
            listView.setExpanded(false);
            listView.setVerticalGap(Double.valueOf(0));
            listView.depthProperty().set(0);
        }
    }

    @FXML
    private void deleteItem(){
        if (listView.getItems().isEmpty()){
            HintFrame.showFailFrame("0 items, nothing to delete!");
            System.out.println("0 ITEMS");
            return;
        }
        int index = listView.getSelectionModel().getSelectedIndex();
        if (index < 0) return;
        tree_table.getColumns().clear();
        listView.getItems().remove(index);
        HintFrame.showSuccessFrame("Delete successfully!");
    }

    @FXML
    void colorPicker(MouseEvent event) {
//        if (drawer1.isShown()) {
//            drawer1.close();
//        } else {
//            drawer1.open();
//        }
    }

    @FXML
    void backToMain(){
        Stage stage = StartProcess.hashMap.remove("data_load");
        stage.close();
        Stage main_stage = StartProcess.hashMap.get("main_page");
        main_stage.show();
    }
}
