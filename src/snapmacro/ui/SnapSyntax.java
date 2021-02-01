package snapmacro.ui;

import java.util.regex.Pattern;

public class SnapSyntax {

    public static final String[] KEYWORDS = new String[]{
            "var", "func", "mouse", "keyboard", "screen", "capture",
            "point", "click", "press", "right", "left",

            "true", "false", "while", "if", "repeat", "exit", "delay", "echo",
            "or", "xor", "and"
    };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "#[0-9a-zA-Z ]*";
    private static final String OPERATION_PATTERN = "==|>|<|!=|>=|<=|=|>|<|%|-|\\+|\\-|\\-=|\\^|\\&|\\|::|\\?|\\*";
    private static final String NUMBERS_PATTERN = "[0-9]+";

    public static final Pattern PATTERN = Pattern.compile(
                     "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
                    + "|(?<OPERATION>" + OPERATION_PATTERN + ")"
                    + "|(?<NUMBER>" + NUMBERS_PATTERN + ")"
    );
}
