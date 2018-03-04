package sample;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

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
	
	
	private static final String[] KEYWORDS = new String[] {
			"abstract", "assert", "boolean", "break", "byte",
			"case", "catch", "char", "class", "const",
			"continue", "default", "do", "double", "else",
			"enum", "extends", "final", "finally", "float",
			"for", "goto", "if", "implements", "import",
			"instanceof", "int", "interface", "long", "native",
			"new", "package", "private", "protected", "public",
			"return", "short", "static", "strictfp", "super",
			"switch", "synchronized", "this", "throw", "throws",
			"transient", "try", "void", "volatile", "while"
	};
	
	private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
	private static final String PAREN_PATTERN = "\\(|\\)";
	private static final String BRACE_PATTERN = "\\{|\\}";
	private static final String BRACKET_PATTERN = "\\[|\\]";
	private static final String SEMICOLON_PATTERN = "\\;";
	private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
	private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";
	
	private static final Pattern PATTERN = Pattern.compile(
			"(?<KEYWORD>" + KEYWORD_PATTERN + ")"
					+ "|(?<PAREN>" + PAREN_PATTERN + ")"
					+ "|(?<BRACE>" + BRACE_PATTERN + ")"
					+ "|(?<BRACKET>" + BRACKET_PATTERN + ")"
					+ "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
					+ "|(?<STRING>" + STRING_PATTERN + ")"
					+ "|(?<COMMENT>" + COMMENT_PATTERN + ")"
	);
	
	private static final String sampleCode = String.join("\n", new String[] {
			"package com.example;",
			"",
			"import java.util.*;",
			"",
			"public class Foo extends Bar implements Baz {",
			"",
			"    /*",
			"     * multi-line comment",
			"     */",
			"    public static void main(String[] args) {",
			"        // single-line comment",
			"        for(String arg: args) {",
			"            if(arg.length() != 0)",
			"                System.out.println(arg);",
			"            else",
			"                System.err.println(\"Warning: empty string as argument\");",
			"        }",
			"    }",
			"",
			"}"
	});
	
	private CodeArea codeArea;
	private ExecutorService executor;
	
	private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
		String text = codeArea.getText();
		Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
			@Override
			protected StyleSpans<Collection<String>> call() throws Exception {
				return computeHighlighting(text);
			}
		};
		executor.execute(task);
		return task;
	}
	
	private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
		codeArea.setStyleSpans(0, highlighting);
	}
	
	private static StyleSpans<Collection<String>> computeHighlighting(String text)
	{
		Matcher matcher = PATTERN.matcher(text);
		int lastKwEnd = 0;
		StyleSpansBuilder<Collection<String>> spansBuilder
				= new StyleSpansBuilder<>();
		while(matcher.find()) {
			String styleClass =
					matcher.group("KEYWORD") != null ? "keyword" :
							matcher.group("PAREN") != null ? "paren" :
									matcher.group("BRACE") != null ? "brace" :
											matcher.group("BRACKET") != null ? "bracket" :
													matcher.group("SEMICOLON") != null ? "semicolon" :
															matcher.group("STRING") != null ? "string" :
																	matcher.group("COMMENT") != null ? "comment" :
																			null; /* never happens */ assert styleClass != null;
			spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
			spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
			lastKwEnd = matcher.end();
		}
		spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
		return spansBuilder.create();
	}
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
	private void edit_button_action(ActionEvent event)
	{
		System.out.println ("lalala");
		if (items.size() != 0)
		{
			items.remove (items.size () - 1);
		}
	}
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		script_listView.setItems (items);
		items.add ("luodian's script");
		items.add ("jackson's script");
		executor = Executors.newSingleThreadExecutor();
		codeArea = new CodeArea();
		codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
		codeArea.richChanges()
				.filter(ch -> !ch.getInserted().equals(ch.getRemoved()))
				.successionEnds(Duration.ofMillis(200))
				.supplyTask(this::computeHighlightingAsync)
				.awaitLatest(codeArea.richChanges())
				.filterMap(t -> {
					if(t.isSuccess())
					{
						return Optional.of(t.get());
					}
					else
					{
						t.getFailure().printStackTrace();
						return Optional.empty();
					}
				})
				.subscribe(this::applyHighlighting);
		codeArea.replaceText(0, 0, sampleCode);
		
		script_listView.getSelectionModel().selectedItemProperty().addListener(
				(ObservableValue<? extends String> observable, String oldValue, String newValue) ->{
					VirtualizedScrollPane vz = new VirtualizedScrollPane<>(codeArea);
					vz.setPrefWidth (edit_page.getPrefWidth ());
					vz.setPrefHeight (edit_page.getPrefHeight ());
					edit_page.getChildren ().add (vz);
				});
	}
}
