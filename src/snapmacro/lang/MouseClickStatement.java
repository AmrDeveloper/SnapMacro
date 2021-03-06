package snapmacro.lang;

public class MouseClickStatement extends Statement{

    private final Token value;

    public MouseClickStatement(Token value){
        this.value = value;
    }

    public Token getValue(){
        return value;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
