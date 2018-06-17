package io.polybius.testtask.predicates;

import com.google.gson.JsonObject;
import io.polybius.testtask.Expression;

import java.util.function.Predicate;

public class LessThanPredicateComposer implements PredicateComposer {
    @Override
    public Predicate<JsonObject> composePredicate(Expression expression) {
       return p -> p.has(expression.getOperand())
                && p.get(expression.getOperand()).getAsInt() < Integer.valueOf(expression.getValue());
    }
}
