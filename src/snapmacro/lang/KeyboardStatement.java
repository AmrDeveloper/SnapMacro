package snapmacro.lang;

public class KeyboardStatement extends Statement {

    private Token  order;
    private Token  value;

    public KeyboardStatement(Token  order, Token  value){
        this.order = order;
        this.value = value;
    }

    public Token  getOrder() {
        return order;
    }

    public Token  getValue() {
        return value;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
