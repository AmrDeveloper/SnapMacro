package snapmacro.lang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static snapmacro.lang.SnapRuntime.error;
import static snapmacro.lang.TokenType.*;

public class SnapParser {

    private int currentIndex;
    private final List<Token> tokens;

    public SnapParser(List<Token> tokens){
        this.tokens = tokens;
    }


    public List<Statement> parse() {
        List<Statement> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(declaration());
        }
        return statements;
    }

    private Statement statement() {
        if (match(IF)) return ifStatement();
        if (match(WHILE)) return whileStatement();
        if (match(REPEAT)) return repeatStatement();
        if (match(ECHO)) return echoStatement();
        if (match(MOUSE)) return mouseStatement();
        if (match(SCREEN)) return screenStatement();
        if (match(KEYBOARD)) return keyboardStatement();
        if (match(SLEEP)) return sleepStatement();
        if (match(EXIT)) return exitStatement();
        if (match(LEFT_BRACE)) return new BlockStatement(block());
        return expressionStatement();
    }

    private Statement declaration() {
        try {
            if (match(FUNCTION)) return funcDeclaration();
            if (match(VAR)) return varDeclaration();
            return statement();
        } catch (ParserError error) {
            synchronize();
            return null;
        }
    }

    private Statement expressionStatement() {
        Expression expr = expression();
        return new ExpressionStatement(expr);
    }

    private Statement mouseStatement(){
        //mouse point 1 1
        Token order = consume(IDENTIFIER, "Expect Mouse Instruction.");
        if(order.getLexeme().equals("point")){
            Expression xValue = expression();
            Expression yValue = expression();
            List<Expression> values = Arrays.asList(xValue, yValue);
            return new MouseStatement(order, values);
        }else{
            Expression value = expression();
            List<Expression> values = Collections.singletonList(value);
            return new MouseStatement(order, values);
        }
    }

    private Statement screenStatement(){
        Token order = consume(IDENTIFIER, "Expect Screen Instruction.");
        Token value = consume(IDENTIFIER, "Expect Screen Value.");
        return new ScreenStatement(order, value);
    }

    private Statement keyboardStatement(){
        Token order = consume(IDENTIFIER, "Expect Keyboard Instruction.");
        Token value = consume(IDENTIFIER, "Expect Keyboard Value.");
        return new KeyboardStatement(order, value);
    }

    private Statement ifStatement(){
        consume(LEFT_PAREN, "Expect '(' after 'if'.");
        Expression condition = expression();
        consume(RIGHT_PAREN, "Expect ')' after if condition.");
        consume(LEFT_BRACE, "Expect '{' to start if body.");
        List<Statement> thenBranch = new ArrayList<>();
        while (!check(RIGHT_BRACE) && !isAtEnd()) {
            thenBranch.add(declaration());
        }
        consume(RIGHT_BRACE, "Expect '}' to end if body.");
        return new IfStatement(condition, thenBranch);
    }

    private Statement whileStatement(){
        consume(LEFT_PAREN, "Expect '(' after 'while'.");
        Expression condition = expression();
        consume(RIGHT_PAREN, "Expect ')' after while condition.");
        Statement loopBody = statement();
        return new WhileStatement(condition, loopBody);
    }

    private Statement repeatStatement(){
        consume(LEFT_PAREN, "Expect '(' after 'repeat'.");
        Expression value = expression();
        consume(RIGHT_PAREN, "Expect ')' after repeat value.");
        Statement loopBody = statement();
        return new RepeatStatement(value,loopBody);
    }

    private Statement funcDeclaration(){
        Token name = consume(IDENTIFIER, "Expect function name.");
        consume(LEFT_BRACE, "Expect '{' before function body.");
        List<Statement> body = block();
        return new FunctionStatement(name, body);
    }

    private Statement varDeclaration(){
        Token name = consume(IDENTIFIER, "Expect variable name.");

        Expression initializer = null;
        if (match(EQUAL)) {
            initializer = expression();
        }

        return new VarStatement(name, initializer);
    }

    private Statement echoStatement(){
        consume(LEFT_PAREN, "Expect '(' after 'while'.");
        Expression value = expression();
        consume(RIGHT_PAREN, "Expect ')' after while condition.");
        return new EchoStatement(value);
    }

    private Statement sleepStatement(){
        Expression value = expression();
        return new SleepStatement(value);
    }

    private Statement exitStatement(){
        return new ExitStatement();
    }

    private List<Statement> block() {
        List<Statement> statements = new ArrayList<>();
        while (!check(RIGHT_BRACE) && !isAtEnd()) {
            statements.add(declaration());
        }
        consume(RIGHT_BRACE, "Expect '}' after block.");
        return statements;
    }

    private Expression expression() {
        return assignment();
    }

    private Expression assignment() {
        //equality lower than || lower than &&
        Expression expr = equality();

        if (match(EQUAL)) {
            Token equals = previous();
            Expression value = assignment();

            if (expr instanceof Variable) {
                Token name = ((Variable) expr).getName();
                return new AssignExp(name, value);
            }
            error(equals, "Invalid assignment target.");
        }
        return expr;
    }

    private Expression equality() {
        Expression expr = comparison();

        while (match(BANG_EQUAL, EQUAL_EQUAL)) {
            Token operator = previous();
            Expression right = comparison();
            expr = new BinaryExp(expr, operator, right);
        }
        return expr;
    }

    private Expression comparison() {
        Expression expr = addition();

        while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
            Token operator = previous();
            Expression right = addition();
            expr = new BinaryExp(expr, operator, right);
        }

        return expr;
    }

    private Expression addition() {
        Expression expr = multiplication();
        while (match(MINUS, PLUS)) {
            Token operator = previous();
            Expression right = multiplication();
            expr = new BinaryExp(expr, operator, right);
        }
        return expr;
    }

    private Expression multiplication() {
        Expression expr = unary();

        while (match(SLASH, STAR)) {
            Token operator = previous();
            Expression right = unary();
            expr = new BinaryExp(expr, operator, right);
        }
        return expr;
    }

    private Expression unary() {
        if (match(BANG, MINUS, PLUS_PLUS, MINUS_MINUS)) {
            Token operator = previous();
            Expression right = unary();
            return new UnaryExp(operator, right);
        }
        return call();
    }

    private Expression call() {
        Expression expr = primary();
        while (true) {
            if (match(LEFT_PAREN)) {
                expr = finishCall(expr);
            } else {
                break;
            }
        }
        return expr;
    }

    private Expression finishCall(Expression callee) {
        List<Expression> arguments = new ArrayList<>();
        Token paren = consume(RIGHT_PAREN, "Expect ')' after arguments.");
        return new CallExp(callee, paren, arguments);
    }

    private Expression primary() {
        if (match(FALSE)) return new LiteralExp(false);
        if (match(TRUE)) return new LiteralExp(true);
        if (match(NUMBER, STRING, CHAR)) return new LiteralExp(previous().getLiteral());
        if (match(IDENTIFIER)) return new Variable(previous());
        System.out.println(previous());
        throw new RuntimeException("Invalid Primary Exception");
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }

    private Token advance() {
        if (!isAtEnd()) currentIndex++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(currentIndex);
    }

    private Token previous() {
        return tokens.get(currentIndex - 1);
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        throw new RuntimeException(message);
    }

    private void synchronize() {
        advance();

        while (!isAtEnd()) {
            switch (peek().getType()) {
                case FUNCTION:
                case VAR:
                case IF:
                case WHILE:
                    return;
            }
            advance();
        }
    }
}
