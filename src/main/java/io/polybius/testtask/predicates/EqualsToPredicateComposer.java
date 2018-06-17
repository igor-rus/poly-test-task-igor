package io.polybius.testtask.predicates;

import com.google.gson.JsonObject;
import io.polybius.testtask.Expression;

import java.util.function.Predicate;

public class EqualsToPredicateComposer implements PredicateComposer {
    @Override
    public Predicate<JsonObject> composePredicate(Expression expression) {
        Predicate<JsonObject> predicate;
        if(!expression.isValueNumeric()) {
            predicate = p -> p.has(expression.getOperand())
                    && p.get(expression.getOperand()).getAsString().contains(expression.getValue());
        } else {
            predicate =  p -> p.has(expression.getOperand())
                    && p.get(expression.getOperand()).getAsInt() == Integer.valueOf(expression.getValue());
        }
        return predicate;
    }
}
