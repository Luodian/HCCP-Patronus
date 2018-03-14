package sample.Controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import sample.Entity.DataRead;
import sample.Entity.Item;
import sample.Main;

import java.io.File;
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

    public static ArrayList<File> files = new ArrayList<File>();

    public static AnchorPane menu_root;

    public static JFXListView listView_pane;

    public static Pane redundance;

    public static Pane func;

    public static Pane dataHeader;

    public static AnchorPane root_pane;

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


        for (int i = 0; i < files.size(); i++) {
            Label file = new Label(files.get(i).getName());
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
                DataRead dataRead = new DataRead(files.get(index));
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
                    JFXTreeTableColumn<Item, String> property = new JFXTreeTableColumn<>("Attr" + String.valueOf(i));
                    property.setPrefWidth(40);
                    final int idex = i;
                    property.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Item, String>, ObservableValue<String>>() {
                        @Override
                        public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Item, String> param) {
                            return param.getValue().getValue().getProperty(idex);
                        }
                    });
                    tree_table.getColumns().add(property);
                }
                final ObservableList<Item> items = FXCollections.observableArrayList();
                for (int i = 0; i< row;i++){
                    Item item = new Item();
                    String[] attrs = (String[]) list.get(i);
                    for (int j = 0;j < column;j++){
                        item.addAttr(attrs[j], Item.PROPERTY_TYPE_STRING);
                    }
                    items.add(item);
                }
                final TreeItem<Item> root = new RecursiveTreeItem<Item>(items, RecursiveTreeObject::getChildren);
                tree_table.setRoot(root);
                tree_table.setShowRoot(false);

                Image image = new Image("/sample/resources/correct.png");
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(50);
                imageView.setFitWidth(50);
                Notifications notificationBuilder = Notifications.create()
                        .title("SUCCESS")
                        .text("Load Successfully!")
                        .darkStyle()
                        .graphic(imageView)
                        .hideAfter(Duration.seconds(5))
                        .position(Pos.BOTTOM_RIGHT)
                        .onAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                System.out.println("Load Successfully");
                            }
                        });
                notificationBuilder.show();
            }
        });
    }

    @FXML
    private void load(MouseEvent event) {

    }

    @FXML
    private void importItem(MouseEvent event) throws IOException{
        Parent p = FXMLLoader.load(getClass().getResource("../FXML/data_setting.fxml"));
        Stage settingStage = new Stage();
        Scene scene = new Scene(p, 600, 400);
        settingStage.setScene(scene);
        settingStage.setResizable(false);
        settingStage.initStyle(StageStyle.UNDECORATED);
        Main.hashMap.put("setting", settingStage);
        Main.hashMap.get("primary").hide();
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
            System.out.println("0 ITEMS");
            return;
        }
        int index = listView.getSelectionModel().getSelectedIndex();
        if (index < 0) return;
        tree_table.getColumns().clear();
        listView.getItems().remove(index);
        Image image = new Image("/sample/resources/correct.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        Notifications notificationBuilder = Notifications.create()
                .title("SUCCESS")
                .text("Delete Successfully!")
                .darkStyle()
                .graphic(imageView)
                .hideAfter(Duration.seconds(5))
                .position(Pos.BOTTOM_RIGHT)
                .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("Delete Successfully");
                    }
                });
        notificationBuilder.show();
    }

    @FXML
    void colorPicker(MouseEvent event) {
        if (drawer1.isShown()) {
            drawer1.close();
        } else {
            drawer1.open();
        }
    }

    @FXML
    void exit(){
        Stage stage = Main.hashMap.get("primary");
        stage.hide();
    }
}
