package snapmacro.lang;

public enum TokenType {
    MOUSE, KEYBOARD, SCREEN, POINT, CLICK, LEFT, RIGHT,

    EQUAL,EQUAL_EQUAL, GREATER, LESS, GREATER_EQUAL, LESS_EQUAL,
    BANG_EQUAL, BANG,

    FUNCTION,

    IDENTIFIER, STRING, NUMBER, TRUE, FALSE, CHAR,
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,

    IF, REPEAT, WHILE, VAR, ECHO,

    RESTART, SLEEP, EXIT,

    PLUS, MINUS, MINUS_MINUS, PLUS_PLUS, SLASH, STAR,

    AND, OR, XOR,

    EOF
}
