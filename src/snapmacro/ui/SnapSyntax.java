package snapmacro.ui;

import java.util.regex.Pattern;

public class SnapSyntax {

    public static final String[] KEYWORDS = {
            "var", "func", "mouse", "keyboard", "screen", "capture",
            "point", "click", "press", "wheel", "release", "right", "left",

            "true", "false", "while", "if", "repeat", "exit", "delay", "echo",
            "or", "xor", "and"
    };

    public static final String[] KEYBOARD_KEYS = {
            // Characters and digits
            "CHAR_[A-Z]", "DIGIT_[0-9]",

            // Virtual Keys
            "ENTER", "BACK_SPACE", "BACK_SPACE", "TAB",
            "SPACE", "ESCAPE", "ALT", "SHIFT", "CONTROL",
            "PAGE_UP", "PAGE_DOWN", "HOME", "END",

            // Arrows
            "UP", "DOWN", "RIGHT", "LEFT",

            // F 1 to 24
            "F[0-9]", "F1[0-9]", "F2[0-4]"
    };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String KEYS_PATTERN = "\\b(" + String.join("|", KEYBOARD_KEYS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String COMMENT_PATTERN = "#[0-9a-zA-Z ]*";
    private static final String OPERATION_PATTERN = "==|>|<|!=|>=|<=|=|>|<|%|-|\\+|\\-|\\-=|\\^|\\&|\\|::|\\?|\\*";
    private static final String HEX_NUMBERS_PATTERN = "0x[0-9a-fA-F]+";
    private static final String NUMBERS_PATTERN = "[0-9]+";

    public static final Pattern PATTERN = Pattern.compile(
                      "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<KEYS>" + KEYS_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
                    + "|(?<OPERATION>" + OPERATION_PATTERN + ")"
                    + "|(?<HEXNUMBER>" + HEX_NUMBERS_PATTERN + ")"
                    + "|(?<NUMBER>" + NUMBERS_PATTERN + ")"

    );
}
