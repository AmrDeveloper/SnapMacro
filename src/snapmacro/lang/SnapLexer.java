package snapmacro.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static snapmacro.lang.TokenType.*;

public class SnapLexer {

    private final String source;
    private final DebuggerListener debuggerListener;

    private final List<Token> tokens = new ArrayList<>();
    private static final Map<String, TokenType> keywords;

    private int start = 0;
    private int current = 0;
    private int line = 1;

    static {
        keywords = new HashMap<>();
        keywords.put("mouse", MOUSE);
        keywords.put("screen", SCREEN);
        keywords.put("keyboard", KEYBOARD);

        keywords.put("func", FUNCTION);
        keywords.put("echo", ECHO);
        keywords.put("if", IF);
        keywords.put("true", TRUE);
        keywords.put("false", FALSE);
        keywords.put("var", VAR);
        keywords.put("while", WHILE);
        keywords.put("repeat", REPEAT);

        keywords.put("and", AND);
        keywords.put("or", OR);
        keywords.put("xor", XOR);

        keywords.put("delay", SLEEP);
        keywords.put("restart", RESTART);
        keywords.put("exit", EXIT);
    }

    public SnapLexer(String source, DebuggerListener debuggerListener) {
        this.source = source;
        this.debuggerListener = debuggerListener;
    }

    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private void scanToken() {
        char c = pointToNextChar();
        switch (c) {
            case '(':
                addToken(LEFT_PAREN);
                break;
            case ')':
                addToken(RIGHT_PAREN);
                break;
            case '{':
                addToken(LEFT_BRACE);
                break;
            case '}':
                addToken(RIGHT_BRACE);
                break;
            case '-':
                addToken(match('-') ? MINUS_MINUS : MINUS);
                break;
            case '+':
                addToken(match('+') ? PLUS_PLUS : PLUS);
                break;
            case '*':
                addToken(STAR);
                break;
            case '/':
                addToken(SLASH);
                break;
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace and comments.
                break;
            case '#':
                while (getCurrentChar() != '\n' && !isAtEnd()) pointToNextChar();
                break;
            case '\n':
                line++;
                break;
            case '\'':
                scanChar();
                break;
            case '"':
                scanString();
                break;
            default:
                if (c == '0' && match('x')) {
                    scanHexNumber();
                } else if (isDigit(c)) {
                    scanNumber();
                } else if (isAlpha(c)) {
                    scanIdentifier();
                } else {
                    debuggerListener.getDebugMessages("Un supported character at line " + line, DebugType.ERROR);
                    throw new ExitEvent();
                }
        }
    }

    private void scanNumber() {
        while (isDigit(getCurrentChar())) pointToNextChar();

        // Look for a fractional part.
        if (getCurrentChar() == '.' && isDigit(getNextChar())) {
            // Consume the "."
            pointToNextChar();
            while (isDigit(getCurrentChar())) pointToNextChar();
        }
        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private void scanHexNumber() {
        while (isHexDigit(getCurrentChar())) pointToNextChar();
        addToken(HEX_NUMBER, source.substring(start, current));
    }

    private void scanString() {
        while (getCurrentChar() != '"' && !isAtEnd()) {
            if (getCurrentChar() == '\n') line++;
            pointToNextChar();
        }

        // Unterminated scanString.
        if (isAtEnd()) {
            debuggerListener.getDebugMessages("Unterminated scanString " + line, DebugType.ERROR);
            throw new ExitEvent();
        }

        // The closing ".
        pointToNextChar();

        // Trim the surrounding quotes.
        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }

    private void scanChar() {
        String value = source.substring(start + 1, start + 2);
        pointToNextChar();

        if (getCurrentChar() != '\'') {
            debuggerListener.getDebugMessages("Unterminated char variable " + line, DebugType.ERROR);
            throw new ExitEvent();
        }

        pointToNextChar();
        addToken(CHAR, value.charAt(0));
    }

    private void scanIdentifier() {
        while (isAlphaNumeric(getCurrentChar())) pointToNextChar();

        // See if the scanIdentifier is a reserved word.
        String text = source.substring(start, current);

        TokenType type = keywords.get(text);
        if (type == null) type = IDENTIFIER;
        addToken(type);
    }

    private char pointToNextChar() {
        current++;
        return source.charAt(current - 1);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isHexDigit(char c) {
        if (isDigit(c)) return true;
        if (c >= 'a' && c <= 'f') return true;
        return (c >= 'A' && c <= 'F');
    }

    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;
        current++;
        return true;
    }

    private char getCurrentChar() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private char getNextChar() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }
}
