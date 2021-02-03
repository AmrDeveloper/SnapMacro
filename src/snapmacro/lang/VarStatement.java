package snapmacro.lang;

public class VarStatement extends Statement {

    private final Token name;
    private final Expression initializer;

    public VarStatement(Token name, Expression initializer) {
        this.name = name;
        this.initializer = initializer;
    }

    public Token getName() {
        return name;
    }

    public Expression getInitializer() {
        return initializer;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visit(this);
    }
}