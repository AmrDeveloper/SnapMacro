package snapmacro.lang;

public class KeyboardStatement extends Statement {

    private final Token order;
    private final KeyboardKey keyboardKey;

    public KeyboardStatement(Token order, KeyboardKey keyboardKey) {
        this.order = order;
        this.keyboardKey = keyboardKey;
    }

    public Token getOrder() {
        return order;
    }

    public KeyboardKey getKeyboardKey() {
        return keyboardKey;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
