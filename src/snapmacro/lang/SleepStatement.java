package snapmacro.lang;

public class SleepStatement extends Statement {

    private final Expression value;

    public SleepStatement(Expression value){
        this.value = value;
    }

    public Expression getValue(){
        return value;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
