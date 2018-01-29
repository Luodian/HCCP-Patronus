package sample;

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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

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
    private JFXButton import_button;

    @FXML
    private JFXButton delete_button;

    @FXML
    private AnchorPane root;

    public static ArrayList<File> files = new ArrayList<File>();



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /**
         *  get the file paths
         * */

        for (int i = 0; i < files.size(); i++) {
            Label file = new Label(files.get(i).getName());
            file.setTextFill(Paint.valueOf("#ffffff"));
            listView.getItems().add(file);
        }

//        for (int i = 0; i < 4; i++) {
//            try {
////            Pane pane = FXMLLoader.load(getClass().getResource("item_pane.fxml"));
////            listView.getItems().add(pane);
////            listView.getItems().add(pane);
//                Label label = new Label("Item " + i);
//                label.setTextFill(Paint.valueOf("#ffffff"));
//                listView.getItems().add(label);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        listView.setExpanded(true);
        listView.setVerticalGap(Double.valueOf(15.0));
        listView.depthProperty().set(5);
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                int index = listView.getSelectionModel().getSelectedIndex();
                if (index<0 ) return;
                DataRead dataRead = new DataRead(files.get(index));
                List list = dataRead.readLines(10, DataRead.SEPARATOR_COMMA);
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

                Image image = new Image("/sample/correct.png");
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
    private void importItem(MouseEvent event){
        Window window = root.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(window);
        if (file == null) return;
        files.add(file);
        Label filelable = new Label(file.getName());
        filelable.setTextFill(Paint.valueOf("#ffffff"));
        listView.getItems().add(filelable);
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
        Image image = new Image("/sample/correct.png");
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
}
