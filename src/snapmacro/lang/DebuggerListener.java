package snapmacro.lang;

@FunctionalInterface
public interface DebuggerListener {
    void getDebugMessages(String message, DebugType type);
}
