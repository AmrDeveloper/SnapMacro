package snapmacro.lang;

import java.util.HashMap;
import java.util.Map;

public class Environment {

    public final Environment enclosing;
    private final Map<String, Object> valuesMap = new HashMap<>();

    public Environment() {
        enclosing = null;
    }

    public Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    public Object get(Token name) {
        if (valuesMap.containsKey(name.getLexeme())) {
            return valuesMap.get(name.getLexeme());
        }

        //If the variable isn’t found in this scope, we simply try the enclosing one
        if (enclosing != null) return enclosing.get(name);

        throw new RuntimeException("Undefined variable '" + name.getLexeme() + "'.");
    }

    public void define(String name, Object value) {
        valuesMap.put(name, value);
    }

    public void assign(Token name, Object value) {
        if (valuesMap.containsKey(name.getLexeme())) {
            valuesMap.put(name.getLexeme(), value);
            return;
        }
        //Again, if the variable isn’t in this environment, it checks the outer one, recursively.
        if (enclosing != null) {
            enclosing.assign(name, value);
            return;
        }
        throw new RuntimeException("Undefined variable '" + name.getLexeme() + "'.");
    }

    void assignAt(int distance, Token name, Object value) {
        ancestor(distance).valuesMap.put(name.getLexeme(), value);
    }

    public Object getAt(int distance, String name) {
        return ancestor(distance).valuesMap.get(name);
    }

    Environment ancestor(int distance) {
        Environment environment = this;
        for (int i = 0; i < distance; i++) {
            environment = environment.enclosing;
        }

        return environment;
    }
}