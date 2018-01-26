package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.fxmisc.richtext.demo.JavaKeywordsAsync;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception
    {
	    FXMLLoader myEditPageLoader = new FXMLLoader (getClass ().getResource ("EditCodePage.fxml"));
	    Parent root = myEditPageLoader.load();
	    Controller EditPageController = myEditPageLoader.getController ();
	    primaryStage.setTitle ("Patronus");
	    try {
		    URL iconURL = Main.class.getResource("resources/patronus.png");
		    Image image = new ImageIcon (iconURL).getImage();
		    com.apple.eawt.Application.getApplication().setDockIconImage(image);
	    } catch (Exception e) {
		    e.printStackTrace ();
	    }
	    Scene scene = new Scene(root);
	    scene.getStylesheets().add(JavaKeywordsAsync.class.getResource("java-keywords.css").toExternalForm());
	    primaryStage.setScene (scene);
	    primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
