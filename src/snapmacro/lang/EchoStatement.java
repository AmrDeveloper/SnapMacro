package snapmacro.lang;

public class EchoStatement extends Statement {

    private final Expression expression;

    public EchoStatement(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
