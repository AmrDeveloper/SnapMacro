package snapmacro.lang;

import java.util.List;

public class MousePointStatement extends Statement {

    private List<Expression> value;

    public MousePointStatement(List<Expression> value) {
        this.value = value;
    }

    public List<Expression> getValue() {
        return value;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
