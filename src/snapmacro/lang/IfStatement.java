package snapmacro.lang;

import java.util.List;

public class IfStatement extends Statement{

    private final Expression condition;
    private final List<Statement> body;

    public IfStatement(Expression condition, List<Statement> body){
        this.condition = condition;
        this.body = body;
    }

    public Expression getCondition(){
        return condition;
    }

    public List<Statement> getBody(){
        return body;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
