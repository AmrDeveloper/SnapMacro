package snapmacro.lang;


public interface ExpressionVisitor<R> {
    R visit(AssignExp expr);
    R visit(BinaryExp expr);
    R visit(LogicalExp expr);
    R visit(LiteralExp expr);
    R visit(Variable expr);
    R visit(UnaryExp expr);
    R visit(CallExp expr);
}
