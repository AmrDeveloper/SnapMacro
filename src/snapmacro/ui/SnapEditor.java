package snapmacro.ui;

import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.io.File;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.IntFunction;
import java.util.regex.Matcher;

public class SnapEditor {

    private CodeArea codeTextArea;
    private TextArea resultTextArea;

    private File sourceFile;
    private ExecutorService taskExecutor;

    public SnapEditor(CodeArea codeArea, TextArea resultArea){
        taskExecutor = Executors.newSingleThreadExecutor();
        codeTextArea = codeArea;
        resultTextArea = resultArea;
    }

    public void editorSettings(){
        codeAreaHighlighter();
        codeAreaAddLineNumber();
    }

    private void codeAreaHighlighter() {
        codeTextArea.multiPlainChanges()
                .successionEnds(Duration.ofMillis(500))
                .supplyTask(this::computeHighlightingAsync)
                .awaitLatest(codeTextArea.multiPlainChanges())
                .filterMap(tryTask -> {
                    if (tryTask.isSuccess()) {
                        return Optional.of(tryTask.get());
                    } else {
                        tryTask.getFailure().printStackTrace();
                        return Optional.empty();
                    }
                })
                .subscribe(this::applyHighlighting);
    }

    private void codeAreaAddLineNumber() {
        IntFunction<Node> lineNumberFactory = LineNumberFactory.get(codeTextArea);
        codeTextArea.setParagraphGraphicFactory(lineNumberFactory);
    }

    private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        codeTextArea.setStyleSpans(0, highlighting);
    }

    private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
        String text = codeTextArea.getText();
        Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
            @Override
            protected StyleSpans<Collection<String>> call() {
                return computeHighlighting(text);
            }
        };
        taskExecutor.execute(task);
        return task;
    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = SnapSyntax.PATTERN.matcher(text);
        int lastKeywordEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        while (matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                    matcher.group("PAREN") != null ? "paren" :
                    matcher.group("BRACE") != null ? "brace" :
                    matcher.group("BRACKET") != null ? "bracket" :
                    matcher.group("KEYS") != null ? "keys" :
                    matcher.group("OPERATION") != null ? "operation" :
                    matcher.group("NUMBER") != null ? "number" :
                    matcher.group("COMMENT") != null ? "comment" :
                    null; /* never happens */
            assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKeywordEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKeywordEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKeywordEnd);
        return spansBuilder.create();
    }

    public void updateSourceFile(File source) {
        sourceFile = source;
    }
}
