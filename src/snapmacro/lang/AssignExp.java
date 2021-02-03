package snapmacro.lang;

public class AssignExp extends Expression {

    private final Token name;
    private final Expression value;

    public AssignExp(Token name, Expression value) {
        this.name = name;
        this.value = value;
    }

    public Token getName() {
        return name;
    }

    public Expression getValue() {
        return value;
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visit(this);
    }
}