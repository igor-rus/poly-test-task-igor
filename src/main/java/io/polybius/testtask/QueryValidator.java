package io.polybius.testtask;

import java.util.Arrays;

public class QueryValidator {

    public static void validateQuery(String query) {
        if(query.isEmpty() || !containsValidExpressions(query) ) {
            throw new IllegalArgumentException("invalid query!");
        }
    }

    private static boolean containsValidExpressions(String query) {
        if(hasBooleanOperators(query)) {
            return !Arrays.stream(query.split("\\s+")).anyMatch(t->!isBooleanOperator(t)&&!validExpression(t));
        }
        return validExpression(query);
    }

    private static boolean validExpression(String expression) {
        return expression.matches("[a-zA-Z]+(>|<|>=|<=|=)[\\w\\s]+");
    }

    public static boolean hasBooleanOperators(String query) {
        return query.contains("&&") || query.contains("||");
    }

    private static boolean isBooleanOperator(String token) {
        return token.equals("||") || token.equals("&&");
    }

}
