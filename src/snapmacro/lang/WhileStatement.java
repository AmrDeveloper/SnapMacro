package snapmacro.lang;

public class WhileStatement extends Statement {

    private Expression condition;
    private Statement loopBody;

    public WhileStatement(Expression condition, Statement loopBody) {
        this.condition = condition;
        this.loopBody = loopBody;
    }

    public Expression getCondition() {
        return condition;
    }

    public Statement getLoopBody() {
        return loopBody;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visit(this);
    }
}