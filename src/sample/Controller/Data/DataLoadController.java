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
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import sample.Controller.Login.LoginController;
import sample.Entity.DataItem;
import sample.Entity.DataNode;
import sample.Entity.DataRead;
import sample.SocketConnect.SocketHandler;
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
    private AnchorPane an_context;

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

    private Label type;

    private Label row_num;

    private Label attr_num;


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

        showMetaInfo();

        try {
            VBox vBox = FXMLLoader.load(getClass().getResource("/sample/FXML/drawer.fxml"));
            drawer1.setSidePane(vBox);
        } catch (IOException e) {
            e.printStackTrace();
        }

        data_sets = SocketHandler.queryDataNodesByID(LoginController.current_user_id);
        if (data_sets != null) {
            for (int i = 0; i < data_sets.size(); i++) {
                Label file = new Label(data_sets.get(i).getData_name());
                file.setTextFill(Paint.valueOf("#ffffff"));
                listView.getItems().add(file);
            }
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

                type.setText("type: " + data_sets.get(index).getData_type());
                attr_num.setText("attr_num: " + data_sets.get(index).getAttr_nums());
                row_num.setText("attr_num: " + data_sets.get(index).getRow_nums());
                HintFrame.showSuccessFrame("Load Successfully!");
            }
        });
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

//    @FXML
//    void themeChange(MouseEvent event) {
//        if (drawer1.isShown()) {
//            drawer1.close();
//        } else {
//            drawer1.open();
//        }
//    }

    @FXML
    void backToMain(){
        Stage stage = StartProcess.hashMap.remove("data_load");
        stage.close();
        Stage main_stage = StartProcess.hashMap.get("main_page");
        main_stage.show();
    }

    private void showMetaInfo() {

        type = new Label("type: unknown");
        type.setFont(new Font("System", 13));
        type.setStyle("-fx-background-color: #104E8B;-fx-background-radius: 1.5em");
        type.setEffect(new DropShadow(10, 1, 1, Color.BLACK));
        type.setPrefSize(135, 24);
        type.setAlignment(Pos.CENTER);
        type.setTextFill(Paint.valueOf("#ffffff"));
        type.setLayoutX(40);
        type.setLayoutY(670);

        row_num = new Label("row_num: unknown");
        row_num.setFont(new Font("System", 13));
        row_num.setStyle("-fx-background-color: #104E8B;-fx-background-radius: 1.5em");
        row_num.setEffect(new DropShadow(10, 1, 1, Color.BLACK));
        row_num.setPrefSize(135, 24);
        row_num.setAlignment(Pos.CENTER);
        row_num.setTextFill(Paint.valueOf("#ffffff"));
        row_num.setLayoutX(230);
        row_num.setLayoutY(670);

        attr_num = new Label("attr_num: unknown");
        attr_num.setFont(new Font("System", 13));
        attr_num.setStyle("-fx-background-color: #104E8B;-fx-background-radius: 1.5em");
        attr_num.setEffect(new DropShadow(10, 1, 1, Color.BLACK));
        attr_num.setPrefSize(135, 24);
        attr_num.setAlignment(Pos.CENTER);
        attr_num.setTextFill(Paint.valueOf("#ffffff"));
        attr_num.setLayoutX(420);
        attr_num.setLayoutY(670);


        an_context.getChildren().add(type);
        an_context.getChildren().add(row_num);
        an_context.getChildren().add(attr_num);

    }

    @FXML
    void themeChange(MouseEvent event) {

    }
}
