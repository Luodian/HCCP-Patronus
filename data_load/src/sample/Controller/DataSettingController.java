package sample.Controller;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import sample.Entity.DataRead;
import sample.Entity.Item;
import sample.Main;

import java.io.File;

public class DataSettingController{

    private DataRead dataRead;

    private File opened_file;

    @FXML
    private JFXButton import_data;

    @FXML
    private JFXButton submit;

    @FXML
    private JFXButton reset_data;

    @FXML
    private JFXButton backe_to_preview;

    @FXML
    private JFXTextField data_name;

    @FXML
    private ChoiceBox<?> type_choice;

    @FXML
    private JFXTreeTableView attr_view;

    @FXML
    private Label attr_num;

    @FXML
    private Label tuple_num;

    @FXML
    private Label date;

    @FXML
    void importData(MouseEvent event) throws Throwable{
        Window window = DataLoadController.root_pane.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(window);
        if (file == null) return;

        dataRead = new DataRead(file);
        dataRead.readLines(-1, DataRead.SEPARATOR_COMMA);
        int colum = dataRead.getColumn();
        int row = dataRead.getRow();
        String[] attrs = dataRead.getAttrs();
        for (int i = 0; i < colum; i++) {
            JFXTreeTableColumn property = new JFXTreeTableColumn(String.valueOf(i + 1));
            property.setPrefWidth(40);
            final int idex = i;
            property.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Item, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Item, String> param) {
                    return param.getValue().getValue().getProperty(idex);
                }
            });
            attr_view.getColumns().add(property);
        }
        Item item = new Item();
        for (String s:attrs) {
            item.addAttr(s, Item.PROPERTY_TYPE_STRING);
        }
        final ObservableList<Item> items = FXCollections.observableArrayList();
        items.add(item);
        final TreeItem<Item> r = new RecursiveTreeItem<Item>(items, RecursiveTreeObject::getChildren);

        attr_view.setRoot(r);
        attr_view.setShowRoot(false);
        attr_num.setText(String.valueOf(colum));
        tuple_num.setText(String.valueOf(row));

        opened_file = file;

    }

    @FXML
    void resetData(MouseEvent event) {

    }

    @FXML
    void submit(MouseEvent event) {
        DataLoadController.files.add(opened_file);
        Label filelable = new Label(opened_file.getName());
        filelable.setTextFill(Paint.valueOf("#ffffff"));
        DataLoadController.listView_pane.getItems().add(filelable);
        Stage stage = Main.hashMap.remove("setting");
        stage.close();
        Main.hashMap.get("primary").show();
    }

    @FXML
    void back(){
        Stage stage = Main.hashMap.remove("setting");
        stage.close();
        Main.hashMap.get("primary").show();
    }
}
