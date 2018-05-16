package sample.Controller.Data;
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
import sample.Controller.Data.DataLoadController;
import sample.Controller.Login.LoginController;
import sample.Datebase.SQLHandler;
import sample.Entity.DataNode;
import sample.Entity.DataRead;
import sample.Entity.DataItem;
import sample.StartProcess;

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

    private int columns;
    private int tuples;

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
            property.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DataItem, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<DataItem, String> param) {
                    return param.getValue().getValue().getProperty(idex);
                }
            });
            attr_view.getColumns().add(property);
        }
        DataItem dataItem = new DataItem();
        for (String s:attrs) {
            dataItem.addAttr(s, DataItem.PROPERTY_TYPE_STRING);
        }
        final ObservableList<DataItem> dataItems = FXCollections.observableArrayList();
        dataItems.add(dataItem);
        final TreeItem<DataItem> r = new RecursiveTreeItem<DataItem>(dataItems, RecursiveTreeObject::getChildren);

        attr_view.setRoot(r);
        attr_view.setShowRoot(false);
        columns = colum;
        tuples = row;
        attr_num.setText(String.valueOf(colum));
        tuple_num.setText(String.valueOf(row));

        opened_file = file;

    }

    @FXML
    void resetData(MouseEvent event) {

    }

    @FXML
    void submit(MouseEvent event) {

        DataNode dataNode = new DataNode(LoginController.current_user_id, data_name.getText(),
                "unknown", tuples, columns, opened_file.getPath());
        if (SQLHandler.insertDataNode(dataNode)){
            /**需要更新data_load页面中list的信息**/
            DataLoadController.data_sets.add(dataNode);
            Label filelable = new Label(opened_file.getName());
            filelable.setTextFill(Paint.valueOf("#ffffff"));
            DataLoadController.listView_pane.getItems().add(filelable);


            Stage stage = StartProcess.hashMap.remove("data_setting");
            stage.close();
            StartProcess.hashMap.get("data_load").show();
        }
        else {
            try {
                throw new Throwable("DATANODE INSERT FAILED");
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    @FXML
    void back(){
        Stage stage = StartProcess.hashMap.remove("data_setting");
        stage.close();
        StartProcess.hashMap.get("data_load").show();
    }
}
