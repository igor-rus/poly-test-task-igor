package io.polybius.testtask;

import java.util.Arrays;
public enum Operator {

    EQUAL("=", 1) ,
    GREATER_THAN(">", 1),
    LESS_THAN("<", 1),
    GREATER_OR_EQUAL(">=", 1),
    LESS_OR_EQUAL("<=", 1),
    AND("&&", 2),
    OR("||", 1);

    private String symbol;
    private int precedence;

    Operator(String symbol, int precedence) {
        this.symbol = symbol;
        this.precedence = precedence;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getPrecedence() {
        return precedence;
    }

    public static Operator fromString(String text) {
        return Arrays.stream(Operator.values()).filter(op -> op.symbol.equalsIgnoreCase(text)).findAny().get();
    }

}
