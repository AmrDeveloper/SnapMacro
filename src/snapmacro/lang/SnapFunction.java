package snapmacro.lang;


public class SnapFunction implements SnapCallable {

    private FunctionStatement declaration;
    private final Environment closure;

    private final boolean isInitializer;

    public SnapFunction(FunctionStatement declaration, Environment closure, boolean isInitializer){
        this.closure = closure;
        this.declaration = declaration;
        this.isInitializer = isInitializer;
    }

    @Override
    public Object call(Interpreter interpreter) {
        Environment environment = new Environment(closure);
        interpreter.execute(declaration.getFunctionBody(), environment);
        if (isInitializer) return closure.getAt(0, "this");
        return null;
    }
}
