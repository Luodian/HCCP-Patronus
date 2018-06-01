package sample.Utils;

import javafx.concurrent.Task;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.demo.JavaKeywordsAsync;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HighlightingCode {
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
            "def build_model():",
            "\tmodel = Sequential()",
            "\tmodel.add(GRU(layers[1], input_shape=(None, layers[0]), return_sequences=True))",
            "\tmodel.add(Dropout(0.2))",

            "\tmodel.add(GRU(layers[2], return_sequences=False))",
            "\tmodel.add(Dropout(0.2))",

            "\tmodel.add(Dense(layers[3]))",
            "\tmodel.add(Activation(\"linear\"))",
            "\tstart = time.time()",
            "\tmodel.compile(loss=\"mse\", optimizer=\"rmsprop\", metrics=['mae', 'mape'])",
            "\treturn model",
	});
	
	public static String getSampleCode () {
		return sampleCode;
	}
	
	private static CodeArea codeArea;
	private static ExecutorService executor;
	
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
	
	private StyleSpans<Collection<String>> getStyleSpans (String text, Pattern pattern) {
		Matcher matcher = pattern.matcher(text);
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
	
	private StyleSpans<Collection<String>> computeHighlighting (String text)
	{
		return getStyleSpans (text, PATTERN);
	}
	
	public VirtualizedScrollPane HLCodeArea (double prf_width,double prf_height) {
		return HLCodeArea (prf_width,prf_height,sampleCode);
	}
	
	public VirtualizedScrollPane HLCodeArea (double prf_width,double prf_height, String code)
	{
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
		codeArea.replaceText(0, 0, code);
		VirtualizedScrollPane vz = new VirtualizedScrollPane<>(codeArea);
		vz.setPrefWidth (prf_width);
		vz.setPrefHeight (prf_height);
		vz.getStylesheets().add(JavaKeywordsAsync.class.getResource("java-keywords.css").toExternalForm());
		return vz;
	}
}