package io.polybius.testtask.predicates;

import com.google.gson.JsonObject;
import io.polybius.testtask.Expression;
import java.util.function.Predicate;

public interface PredicateComposer {
    Predicate<JsonObject> composePredicate(Expression expression);
}
