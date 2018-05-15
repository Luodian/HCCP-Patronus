package sample.Controller.Code;

import com.jfoenix.controls.JFXListView;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.fxmisc.flowless.VirtualizedScrollPane;
import sample.Utils.HighlightingCode;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ScriptController implements Initializable
{
	private String code;
	private StringBuilder file_name;
	private List<String> process_output_List = new ArrayList<> ();
	final WebView browser = new WebView ();
	final WebEngine webEngine = browser.getEngine ();
	@FXML
	private AnchorPane edit_page;
	@FXML
	private AnchorPane cmd_page;
	@FXML
	private JFXListView script_listView;
	private static int current_size;
	ObservableList<Label> items = FXCollections.observableArrayList ();
	
	@FXML
	private void run_script_button_action (ActionEvent event)
	{
		System.out.println ("run button pressed");
		System.out.println (file_name);
		process_output_List.clear();
		String compile_cmd = "javac " + file_name + ".java";
		String run_cmd = "java " + file_name;
		Process process = null;
		try
		{
			Process compile_process = Runtime.getRuntime ().exec (compile_cmd);
			Process run_proccess = Runtime.getRuntime ().exec (run_cmd);
			BufferedReader compile_error_info = new BufferedReader (new InputStreamReader (compile_process.getErrorStream ()));
			BufferedReader compile_cmd_info = new BufferedReader(new InputStreamReader (compile_process.getInputStream ()));
			BufferedReader run_error_info = new BufferedReader (new InputStreamReader (run_proccess.getErrorStream ()));
			BufferedReader run_cmd_info = new BufferedReader(new InputStreamReader (run_proccess.getInputStream ()));
			
			String line = "";
			
			while ((line = compile_error_info.readLine ()) != null)
			{
				process_output_List.add (line);
			}
			
			while ((line = compile_cmd_info.readLine ()) != null)
			{
				process_output_List.add (line);
			}
			
			while ((line = run_error_info.readLine ()) != null)
			{
				process_output_List.add (line);
			}
			
			while ((line = run_cmd_info.readLine ()) != null)
			{
				process_output_List.add (line);
			}
			webEngine.reload ();
			StringBuilder output_info = new StringBuilder ();
			for (String s : process_output_List)
			{
				output_info.append (s + "\n");
			}
			
			webEngine.loadContent (String.valueOf (output_info));
			
			compile_error_info.close ();
			compile_cmd_info.close();
			
		} catch (IOException e)
		{
			e.printStackTrace ();
		}
		
//		items.add("new script");
	}
	
	@FXML
	private void edit_button_action (ActionEvent event)
	{
		System.out.println ("edit button pressed");
		Stage ViewPageStage = new Stage ();
		ViewRemoteCodeController.start (ViewPageStage);
	}
	
	@Override
	public void initialize (URL location, ResourceBundle resources)
	{
		current_size = 0;
		script_listView.setItems (items);

        Label label1 = new Label("luodian's script");
        label1.setTextFill(Paint.valueOf("#ffffff"));
        Label label2 = new Label("jackson's script");
        label2.setTextFill(Paint.valueOf("#ffffff"));
		items.add (label1);
		items.add (label2);



		current_size += 2;
		script_listView.setExpanded(true);
		script_listView.setVerticalGap(Double.valueOf(15.0));
		script_listView.depthProperty().set(5);
		HighlightingCode highlighter = new HighlightingCode ();
		script_listView.getSelectionModel ().selectedItemProperty ().addListener (
				(ObservableValue observable, Object oldValue, Object newValue) ->
				{
					VirtualizedScrollPane vz = highlighter.HLCodeArea (edit_page.getPrefWidth (), edit_page.getHeight ());
					edit_page.getChildren ().add (vz);
					code = highlighter.getSampleCode ();
					
					int class_name_index = code.indexOf ("public class ");
					file_name = new StringBuilder ("");
					for (int i = class_name_index + 13; i < code.length () && code.charAt (i) != ' '; ++i)
					{
						file_name.append (String.valueOf (code.charAt (i)));
					}
					
					File file = new File ("src/sample/resources/resources/" + file_name + ".java");
					if (!file.exists ())
					{
						try
						{
							file.createNewFile ();
						} catch (IOException e)
						{
							e.printStackTrace ();
						}
					}
					try
					{
						FileWriter flwt = new FileWriter (file);
						flwt.write (code);
						flwt.flush ();
						flwt.close ();
					} catch (IOException e)
					{
						e.printStackTrace ();
					}
				});
		ScrollPane srp = new ScrollPane ();
		
		webEngine.loadContent ("Java command line is waiting.");
//		srp.setPrefSize (cmd_page.getWidth (),cmd_page.getHeight ());
		srp.setContent (browser);
		cmd_page.getChildren ().add (srp);
	}
	
}
