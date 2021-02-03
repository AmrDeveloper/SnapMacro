package snapmacro.lang;

import java.util.List;

public class CallExp extends Expression {

    private final Expression callee;
    private final Token closingParenthesis;
    private final List<Expression> arguments;

    public CallExp(Expression calle, Token paren, List<Expression> arguments) {
        this.callee = calle;
        this.closingParenthesis = paren;
        this.arguments = arguments;
    }

    public Expression getCallee() {
        return callee;
    }

    public Token getClosingParenthesis() {
        return closingParenthesis;
    }

    public List<Expression> getArguments() {
        return arguments;
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visit(this);
    }
}