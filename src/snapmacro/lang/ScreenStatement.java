package snapmacro.lang;

public class ScreenStatement extends Statement {

    private final Token order;
    private final Token value;

    public ScreenStatement(Token order, Token value) {
        this.order = order;
        this.value = value;
    }

    public Token getOrder() {
        return order;
    }

    public Token getValue() {
        return value;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
