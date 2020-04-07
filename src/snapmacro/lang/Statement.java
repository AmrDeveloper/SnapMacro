package snapmacro.lang;

public abstract class Statement {
    public abstract <R> R accept(StatementVisitor<R> visitor);
}
