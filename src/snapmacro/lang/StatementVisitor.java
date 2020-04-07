package snapmacro.lang;

public interface StatementVisitor<R> {
    R visit(IfStatement statement);
    R visit(WhileStatement statement);
    R visit(FunctionStatement statement);
    R visit(VarStatement statement);
    R visit(ExpressionStatement statement);
    R visit(RepeatStatement statement);
    R visit(BlockStatement statement);

    R visit(MouseStatement statement);
    R visit(KeyboardStatement statement);
    R visit(ScreenStatement statement);
    R visit(EchoStatement statement);
    R visit(ExitStatement statement);
    R visit(SleepStatement statement);
}
