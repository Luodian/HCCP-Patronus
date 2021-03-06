package sample.Controller.Code;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXScrollPane;
import com.jfoenix.svg.SVGGlyph;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.fxmisc.flowless.VirtualizedScrollPane;
import sample.Utils.HighlightingCode;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewRemoteCodeController implements Initializable
{
	@FXML
	private static JFXScrollPane ViewCodePage;
	@FXML
	private JFXButton VRC_apply_button;
	@FXML
	private JFXButton VRC_cancel_button;
	@FXML
	private Pane ViewPagePane;
	
	@Override
	public void initialize (URL location, ResourceBundle resources)
	{
	
	}
	
	@FXML
	private void apply_button_action (ActionEvent event)
	{
		System.out.println ("apply pressed");
	}
	
	@FXML
	private void cancel_button_action (ActionEvent event)
	{
		System.out.println ("cancel pressed");
	}
	
	public static void start (Stage stage)
	{
		HighlightingCode highlighter = new HighlightingCode ();
		VirtualizedScrollPane vz = highlighter.HLCodeArea (800, 600);
		StackPane container = new StackPane (vz);
		container.setPadding (new Insets (0));
		
		ViewCodePage = new JFXScrollPane ();
		ViewCodePage.setContent (container);
		
		JFXButton button = new JFXButton ();
		SVGGlyph arrow = new SVGGlyph (0,
				"FULLSCREEN",
				"M402.746 877.254l-320-320c-24.994-24.992-24.994-65.516 0-90.51l320-320c24.994-24.992 65.516-24.992 90.51 0 24.994 24.994 "
						+ "24.994 65.516 0 90.51l-210.746 210.746h613.49c35.346 0 64 28.654 64 64s-28.654 64-64 64h-613.49l210.746 210.746c12.496 "
						+ "12.496 18.744 28.876 18.744 45.254s-6.248 32.758-18.744 45.254c-24.994 24.994-65.516 24.994-90.51 0z",
				Color.WHITE);
		arrow.setSize (20, 16);
		button.setGraphic (arrow);
		button.setRipplerFill (Color.WHITE);
		button.setOnAction ((ActionEvent e) ->
		{
			System.out.println ("button pressed");
			((Node) e.getSource ()).getScene ().getWindow ().hide ();
		});
		ViewCodePage.getTopBar ().getChildren ().add (button);
		
		Label title = new Label ("Luodian's script");
		ViewCodePage.getBottomBar ().getChildren ().add (title);
		title.setStyle ("-fx-text-fill:WHITE; -fx-font-size: 40;");
		JFXScrollPane.smoothScrolling ((ScrollPane) ViewCodePage.getChildren ().get (0));
		
		StackPane.setMargin (title, new Insets (0, 0, 20, 80));
		StackPane.setAlignment (title, Pos.CENTER_LEFT);
		StackPane.setAlignment (button, Pos.CENTER_LEFT);
		StackPane.setMargin (button, new Insets (0, 0, 0, 20));
		
		final Scene scene = new Scene (new StackPane (ViewCodePage), 800, 600, Color.WHITE);
		
		stage.setTitle ("View Code");
		stage.setScene (scene);
		stage.initStyle (StageStyle.UNDECORATED);
		stage.show ();
	}
}
