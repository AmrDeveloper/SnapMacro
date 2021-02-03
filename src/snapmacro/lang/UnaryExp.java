package snapmacro.lang;

public class UnaryExp extends Expression {

    private final Token operator;
    private final Expression rightExp;

    public UnaryExp(Token operator, Expression rightExp) {
        this.operator = operator;
        this.rightExp = rightExp;
    }

    public Token getOperator() {
        return operator;
    }

    public Expression getRightExp() {
        return rightExp;
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
