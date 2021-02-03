package snapmacro.lang;

public class RepeatStatement extends Statement {

    private final Expression value;
    private final Statement loopBody;

    public RepeatStatement(Expression value, Statement loopBody) {
        this.value = value;
        this.loopBody = loopBody;
    }

    public Expression getValue() {
        return value;
    }

    public Statement getLoopBody() {
        return loopBody;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visit(this);
    }
}