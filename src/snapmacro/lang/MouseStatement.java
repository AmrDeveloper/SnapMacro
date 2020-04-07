package snapmacro.lang;

import java.util.List;

public class MouseStatement extends Statement {

    private Token order;
    private List<Expression> value;

    public MouseStatement(Token order, List<Expression> value) {
        this.order = order;
        this.value = value;
    }

    public Token getOrder() {
        return order;
    }

    public List<Expression> getValue() {
        return value;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
