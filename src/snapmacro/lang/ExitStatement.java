package snapmacro.lang;

public class ExitStatement extends Statement{

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
