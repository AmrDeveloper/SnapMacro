package snapmacro.lang;

import java.util.List;

public class FunctionStatement extends Statement {

    private final Token name;
    private final List<Statement> functionBody;

    public FunctionStatement(Token name, List<Statement> functionBody) {
        this.name = name;
        this.functionBody = functionBody;
    }

    public Token getName(){
        return name;
    }

    public List<Statement> getFunctionBody() {
        return functionBody;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
