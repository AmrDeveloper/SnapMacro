package snapmacro.lang;

public class LiteralExp extends Expression{

    private final Object value;

    public LiteralExp(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visit(this);
    }
}