package sample;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable{
	@FXML
	private void new_button_action(ActionEvent event)
	{
		System.out.println("lollolol");
	}
	@FXML
	private void edit_button_action(ActionEvent event)
	{
		System.out.println ("lalala");
	}
	
	@FXML private ListView<String> script_listView;
	ObservableList<String> items = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		script_listView.setItems (items);
		items.add("luodian's script");
		items.add ("jackson's script");
	}
	
}
