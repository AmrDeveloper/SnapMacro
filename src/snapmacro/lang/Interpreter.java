package snapmacro.lang;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter implements
        ExpressionVisitor<Object>,
        StatementVisitor<Void> {

    private final Environment globals = new Environment();
    private Environment environment = globals;
    private final Map<Expression, Integer> locals = new HashMap<>();
    private final RobotController robotController = RobotController.getInstance();

    private StreamListener mStreamListener;
    private DebuggerListener mDebuggerListener;

    public void interpret(List<Statement> statements) {
        showDebugMessage("Start Interpreter", DebugType.WARN);
        try {
            for(Statement statement : statements){
                execute(statement);
            }
        } catch (ExitEvent event) {
            showDebugMessage("Exit Interpreter", DebugType.ERROR);
        }
    }

    @Override
    public Object visit(AssignExp expr) {
        Token name = expr.getName();
        Object value = evaluate(expr.getValue());

        Integer distance = locals.get(expr);
        if (distance != null) {
            environment.assignAt(distance, name, value);
        } else {
            globals.assign(name, value);
        }
        return value;
    }

    @Override
    public Object visit(BinaryExp expr) {
        Object left = evaluate(expr.getLeftExp());
        Object right = evaluate(expr.getRightExp());
        switch (expr.getOperator().getType()) {
            case PLUS: {
                //Math Plus
                if (left instanceof Double && right instanceof Double) {
                    return (double) left + (double) right;
                }
                //String Addition
                if (left instanceof String || right instanceof String) {
                    return left.toString() + right.toString();
                }
                //Character + Character
                if ((left instanceof Character && right instanceof Character)) {
                    return String.valueOf(left) + right;
                }
                showDebugMessage("Operands must be two numbers or two strings.", DebugType.ERROR);
                throw new ExitEvent();
            }
            case MINUS: {
                checkNumberOperands(expr.getOperator(), left, right);
                return (double) left - (double) right;
            }
            case STAR: {
                if ((left instanceof String || left instanceof Character)
                        && right instanceof Double) {
                    StringBuilder result = new StringBuilder(left.toString());
                    for (int i = 1; i < (double) right; i++) {
                        result.append(left.toString());
                    }
                    return result.toString();
                }
                checkNumberOperands(expr.getOperator(), left, right);
                return (double) left * (double) right;
            }
            case SLASH: {
                checkNumberOperands(expr.getOperator(), left, right);
                if ((double) right == 0) {
                    showDebugMessage("Can't use slash with zero double.", DebugType.ERROR);
                    throw new ExitEvent();
                }
                return (double) left / (double) right;
            }
            case GREATER: {
                checkNumberOperands(expr.getOperator(), left, right);
                return Double.parseDouble(left.toString()) > Double.parseDouble(right.toString());
            }
            case GREATER_EQUAL: {
                checkNumberOperands(expr.getOperator(), left, right);
                return Double.parseDouble(left.toString()) >= Double.parseDouble(right.toString());
            }
            case LESS: {
                checkNumberOperands(expr.getOperator(), left, right);
                return Double.parseDouble(left.toString()) < Double.parseDouble(right.toString());
            }
            case LESS_EQUAL: {
                checkNumberOperands(expr.getOperator(), left, right);
                return Double.parseDouble(left.toString()) <= Double.parseDouble(right.toString());
            }
            case BANG_EQUAL: {
                return !isEqual(left, right);
            }
            case EQUAL_EQUAL: {
                return isEqual(left, right);
            }
        }
        return null;
    }

    @Override
    public Object visit(LogicalExp expr) {
        Object left = evaluate(expr.getLeftExp());
        switch (expr.getOperator().getType()) {
            case AND: {
                Object right = evaluate(expr.getRightExp());
                return isTruthy(left) && isTruthy(right);
            }
            case OR: {
                Object right = evaluate(expr.getRightExp());
                return isTruthy(left) || isTruthy(right);
            }
            case XOR: {
                Object right = evaluate(expr.getRightExp());
                return isTruthy(left) ^ isTruthy(right);
            }
        }
        return false;
    }

    @Override
    public Object visit(LiteralExp expr) {
        return expr.getValue();
    }

    @Override
    public Object visit(Variable expr) {
        try{
            return lookUpVariable(expr.getName(), expr);
        }catch (RuntimeException e){
            showDebugMessage(e.getMessage(), DebugType.ERROR);
        }
        throw new ExitEvent();
    }

    @Override
    public Object visit(UnaryExp expr) {
        Object right = evaluate(expr.getRightExp());
        switch (expr.getOperator().getType()) {
            case MINUS: {
                checkNumberOperand(expr.getOperator(), right);
                return -(double) right;
            }
            case BANG: {
                return !isTruthy(right);
            }
            case PLUS_PLUS: {
                return (double) right + 1;
            }
            case MINUS_MINUS: {
                return (double) right - 1;
            }
        }
        return null;
    }

    @Override
    public Object visit(CallExp expr) {
        Object callee = evaluate(expr.getCallee());

        SnapCallable function = (SnapCallable) callee;

        return function.call(this);
    }

    @Override
    public Void visit(IfStatement statement) {
        Object conditionResult = evaluate(statement.getCondition());
        if (isTruthy(conditionResult)) {
            execute(statement.getBody(), environment);
        }
        return null;
    }

    @Override
    public Void visit(WhileStatement statement) {
        Environment whileEnvironment = new Environment(environment);
        Environment previous = this.environment;
        this.environment = whileEnvironment;

        while (isTruthy(evaluate(statement.getCondition()))) {
            execute(statement.getLoopBody());
        }
        this.environment = previous;
        return null;
    }

    @Override
    public Void visit(FunctionStatement statement) {
        SnapFunction function = new SnapFunction(statement, environment, false);
        environment.define(statement.getName().getLexeme(), function);
        return null;
    }

    @Override
    public Void visit(VarStatement statement) {
        Object value = null;
        if (statement.getInitializer() != null) {
            value = evaluate(statement.getInitializer());
        }
        environment.define(statement.getName().getLexeme(), value);
        return null;
    }

    @Override
    public Void visit(MouseStatement statement) {
        showDebugMessage("Mouse Event", DebugType.WARN);
        String order = statement.getOrder().getLexeme();
        List<Expression> values = statement.getValue();
        switch (order) {
            case "point":
                Expression xValue = values.get(0);
                Expression yValue = values.get(1);

                int xValNum = (int) evaluate(xValue);
                int yValNum = (int) evaluate(yValue);

                robotController.setCursorPosition(xValNum, yValNum);
                break;
            case "click":
                Expression button = values.get(0);
                String buttonName = String.valueOf(evaluate(button));

                if (buttonName.equals("right")) {
                    robotController.mouseRightClick();
                } else {
                    robotController.mouseLeftClick();
                }
                break;
        }
        return null;
    }

    @Override
    public Void visit(KeyboardStatement statement) {
        showDebugMessage("Keyboard Event", DebugType.WARN);
        String order = statement.getOrder().getLexeme();
        String value = statement.getValue().getLexeme();
        if (order.equals("press")) {
            robotController.keyboardPressKey(value.toUpperCase());
        } else {
            showDebugMessage("Invalid keyboard instruction", DebugType.ERROR);
            throw new ExitEvent();
        }
        return null;
    }

    @Override
    public Void visit(ScreenStatement statement) {
        showDebugMessage("Screen Event", DebugType.WARN);
        String order = statement.getOrder().getLexeme();
        String path = statement.getValue().getLexeme();
        if (order.equals("capture")) {
            robotController.captureScreen(path);
        } else {
            showDebugMessage("Invalid Screen Instruction", DebugType.ERROR);
            throw new ExitEvent();
        }
        return null;
    }

    @Override
    public Void visit(ExpressionStatement statement) {
        evaluate(statement.getExpression());
        return null;
    }

    @Override
    public Void visit(RepeatStatement statement) {
        Environment repeatEnvironment = new Environment(environment);
        Environment previous = this.environment;
        this.environment = repeatEnvironment;

        Object value = evaluate(statement.getValue());

        boolean isNotNumber = !(value instanceof Number);

        if (isNotNumber) {
            showDebugMessage("Repeat Counter must be number", DebugType.ERROR);
            throw new ExitEvent();
        }

        int counter = (int) Double.parseDouble(value.toString());
        for (int i = 0; i < counter; i++) {
            execute(statement.getLoopBody());
        }
        this.environment = previous;
        return null;
    }

    @Override
    public Void visit(EchoStatement statement) {
        Object value = evaluate(statement.getExpression());
        showStreamMessage(stringify(value));
        return null;
    }

    @Override
    public Void visit(ExitStatement statement) {
        showDebugMessage("Exit Event", DebugType.WARN);
        throw new ExitEvent();
    }

    @Override
    public Void visit(SleepStatement statement) {
        showDebugMessage("Sleep Event", DebugType.WARN);
        Object value = evaluate(statement.getValue());
        if (value instanceof Number) {
            long delay = ((Number) value).longValue();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            showDebugMessage("Sleep value must be number", DebugType.ERROR);
            throw new ExitEvent();
        }
        return null;
    }

    @Override
    public Void visit(BlockStatement statement) {
        execute(statement.getStatementList(), new Environment(environment));
        return null;
    }

    private Object evaluate(Expression expr) {
        return expr.accept(this);
    }

    private boolean isTruthy(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) return (boolean) object;
        return true;
    }

    private boolean isEqual(Object a, Object b) {
        // nil is only equal to nil.
        if (a == null && b == null) return true;
        if (a == null) return false;

        return a.equals(b);
    }

    private String stringify(Object object) {
        if (object == null) return "nil";
        // Hack. Work around Java adding ".0" to integer-valued doubles.
        if (object instanceof Double) {
            String text = object.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }

        return object.toString()
                .replaceAll("\\\\n", "\n")
                .replaceAll("\\\\t", "\t");
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) return;
        showDebugMessage("Operand must be a number." , DebugType.ERROR);
        throw new ExitEvent();
    }

    private void checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Number && right instanceof Number) return;
        showDebugMessage("Operands must the same type -> number.", DebugType.ERROR);
        throw new ExitEvent();
    }

    private void execute(Statement stmt) {
        stmt.accept(this);
    }

    public void execute(List<Statement> statementList, Environment localEnvironment) {
        Environment previous = this.environment;
        try {
            //Make current environment is block local not global
            this.environment = localEnvironment;
            //Execute every statement in block
            for (Statement statement : statementList) {
                execute(statement);
            }
        } finally {
            //Same like pop environment from stack
            this.environment = previous;
        }
    }

    private Object lookUpVariable(Token name, Expression expr) {
        Integer distance = locals.get(expr);
        if (distance != null) {
            //find variable value in locales score
            return environment.getAt(distance, name.getLexeme());
        } else {
            //If can't find distance in locales so it must be global variable
            return globals.get(name);
        }
    }

    public void setStreamListener(StreamListener listener){
        mStreamListener = listener;
    }

    public void removeStreamListener(){
        mStreamListener = null;
    }

    public void setDebuggerListener(DebuggerListener listener){
        mDebuggerListener = listener;
    }

    public void removeDebuggerListener(){
        mDebuggerListener = null;
    }

    private void showStreamMessage(String message){
        ListenerMessage.showStreamMessage(mStreamListener, message);
    }

    private void showDebugMessage(String message, DebugType type){
        ListenerMessage.showDebugMessage(mDebuggerListener, message, type);
    }
}