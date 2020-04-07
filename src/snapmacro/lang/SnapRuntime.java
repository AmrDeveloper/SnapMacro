package snapmacro.lang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class SnapRuntime {

    private static boolean hadError = false;
    private static boolean hadRuntimeError = false;
    private static final Interpreter interpreter = new Interpreter();

    private StreamListener mStreamListener;
    private DebuggerListener mDebuggerListener;

    public SnapRuntime(){

    }

    public SnapRuntime(StreamListener listener){
        mStreamListener = listener;
        interpreter.setStreamListener(mStreamListener);
    }

    public SnapRuntime(DebuggerListener listener){
        mDebuggerListener = listener;
        interpreter.setDebuggerListener(mDebuggerListener);
    }

    public SnapRuntime(StreamListener stream, DebuggerListener debugger){
        mStreamListener = stream;
        interpreter.setStreamListener(mStreamListener);

        mDebuggerListener = debugger;
        interpreter.setDebuggerListener(mDebuggerListener);
    }

    public void runSnapFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        runSnapCode(new String(bytes, Charset.defaultCharset()));
        if (hadError) System.exit(65);
        if (hadRuntimeError) System.exit(70);
    }

    public void runSnapTerminal() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        System.out.println("Welcome in Tank Programming lang");
        while (true) {
            System.out.print("> ");
            runSnapCode(reader.readLine());
            hadError = false;
        }
    }

    public void runSnapCode(String source) {
        SnapLexer tankLexer = new SnapLexer(source);
        List<Token> tokens = tankLexer.scanTokens();
        SnapParser parser = new SnapParser(tokens, mDebuggerListener);
        List<Statement> statements = parser.parse();

        //Start Tank Interpreter
        interpreter.interpret(statements);
    }

    public void setStreamListener(StreamListener listener){
        mStreamListener = listener;
        interpreter.setStreamListener(mStreamListener);
    }

    public void removeStreamListener(){
        interpreter.removeStreamListener();
    }

    public void setDebuggerListener(DebuggerListener listener){
        mDebuggerListener = listener;
        interpreter.setDebuggerListener(mDebuggerListener);
    }

    public void removeDebuggerListener(){
        interpreter.removeDebuggerListener();
    }

    public static void error(int line, String message) {
        report(line, "", message);
    }

    public static void error(Token token, String message) {
        if (token.getType() == TokenType.EOF) {
            report(token.getLine(), " at end", message);
        } else {
            report(token.getLine(), " at '" + token.getLexeme() + "'", message);
        }
    }

    private static void report(int line, String where, String message) {
        System.err.println(
                "[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }

    public static void runtimeError(RuntimeError error) {
        System.err.println(error.getMessage() +" \n[line " + error.getToken().getLine() + "]");
        hadRuntimeError = true;
    }

    public void stopInterpreter(){
        interpreter.visit(new ExitStatement());
    }
}
