package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.HashMap;

public class Main extends Application {

    public static HashMap<String, Stage> hashMap = new HashMap<String, Stage>();
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("FXML/data_load.fxml"));
        primaryStage.setScene(new Scene(root, 1006, 700));
//        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        root.getStylesheets().add(getClass().getResource("FXML/list_view.css").toExternalForm());
        hashMap.put("primary", primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
