package sample;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.demo.JavaKeywordsAsync;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller implements Initializable{
	
	@FXML private AnchorPane edit_page;
	@FXML private ListView<String> script_listView;
	ObservableList<String> items = FXCollections.observableArrayList();
	@FXML
	private void new_button_action(ActionEvent event)
	{
		System.out.println("lollolol");
		String currentSize = String.valueOf (items.size() + 1);
		items.add ("script " + currentSize);
	}
	@FXML
	private void edit_button_action(ActionEvent event) throws IOException {
		System.out.println ("lalala");
		Stage ViewPageStage = new Stage();
		ViewRemoteCodeController.start(ViewPageStage);
	}
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		script_listView.setItems (items);
		items.add ("luodian's script");
		items.add ("jackson's script");
		
		script_listView.getSelectionModel().selectedItemProperty().addListener(
				(ObservableValue<? extends String> observable, String oldValue, String newValue) ->{
					HighlightingCode highlighter = new HighlightingCode ();
					VirtualizedScrollPane vz = highlighter.HLCodeArea (edit_page.getPrefWidth (),edit_page.getHeight ());
					edit_page.getChildren ().add (vz);
				});
	}
}
