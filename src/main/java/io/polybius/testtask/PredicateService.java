package io.polybius.testtask;

import com.google.gson.JsonObject;
import io.polybius.testtask.predicates.EqualsToPredicateComposer;
import io.polybius.testtask.predicates.GreaterOrEqualPredicateComposer;
import io.polybius.testtask.predicates.GreaterThanPredicateComposer;
import io.polybius.testtask.predicates.LessOrEqualPredicateComposer;
import io.polybius.testtask.predicates.LessThanPredicateComposer;
import io.polybius.testtask.predicates.PredicateComposer;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.Predicate;

public class PredicateService {


    private Map<Operator,PredicateComposer> predicateComposersByOperator =  new HashMap<>();

    PredicateService() {
        predicateComposersByOperator.put(Operator.EQUAL, new EqualsToPredicateComposer());
        predicateComposersByOperator.put(Operator.GREATER_OR_EQUAL, new GreaterOrEqualPredicateComposer());
        predicateComposersByOperator.put(Operator.GREATER_THAN, new GreaterThanPredicateComposer());
        predicateComposersByOperator.put(Operator.LESS_OR_EQUAL, new LessOrEqualPredicateComposer());
        predicateComposersByOperator.put(Operator.LESS_THAN, new LessThanPredicateComposer());
    }

    public Predicate<JsonObject> getPredicate(String query) {

        Predicate<JsonObject> predicate;

        if(!QueryValidator.hasBooleanOperators(query)) {
            predicate = createPredicateFromSingleExpression(query);
        } else {
            predicate = createPredicateFromBooleanExpression(query);
        }
        return predicate;
    }


    /**
     * Create predicate from query that has only one expression
     */
    private Predicate<JsonObject> createPredicateFromSingleExpression(String query) {

        Expression expression = new Expression(query);
        Operator operator = expression.getOperator();
        Predicate<JsonObject> predicate;

        if(!expression.isValueNumeric() & !operator.equals(Operator.EQUAL)) {
            throw new UnsupportedOperationException("cannot apply operator " +
                    expression.getOperator().getSymbol() + " to " + expression.getValue());
        }

        predicate = predicateComposersByOperator.get(operator).composePredicate(expression);
        return predicate;

    }

    /**
     * Creates one large compound predicate from query string, containing boolean expression by splitting
     * expression into tokens and using two stacks one for predicates and one for operators.
     *
     * @param query query string containing boolean expression
     */
    private Predicate<JsonObject> createPredicateFromBooleanExpression(String query) {

        Stack<Operator> operatorStack = new Stack<>();
        Stack<Predicate<JsonObject>> predicateStack = new Stack<>();
        Predicate<JsonObject> predicate;

        String[] split = query.split("\\s+");

        for (String subquery : split) {
            if(QueryValidator.hasBooleanOperators(subquery)) {
                Operator op = Operator.fromString(subquery);
                collapseTop(predicateStack,operatorStack,op);
                operatorStack.push(op);
            } else {
                //we have single expression so lets make predicate from it immediately
                Predicate<JsonObject> pred = createPredicateFromSingleExpression(subquery);
                predicateStack.push(pred);
            }
        }

        collapseTop(predicateStack, operatorStack, Operator.EQUAL); //small trick to use operator with small precedence
        if (predicateStack.size() == 1 && operatorStack.size() == 0) {
            predicate = predicateStack.pop();
        } else {
            predicate = null;
        }

        return predicate;
    }

    /**
     * Depending of a stacks sizes, continues to checks if current top element in operator stack has higher
     * or equal precedence and if true applies it to two operands from predicate stack,
     * collapsing those and pushing result back
     *
     */
    private static void collapseTop(Stack<Predicate<JsonObject>> predicateStack, Stack<Operator> operatorStack,
                                    Operator futureTop){
        while (predicateStack.size() >= 2 && operatorStack.size() >= 1) {

            if (futureTop.getPrecedence() <= operatorStack.peek().getPrecedence()) {

                Predicate<JsonObject> second = predicateStack.pop();
                Predicate<JsonObject> first = predicateStack.pop();
                Operator op = operatorStack.pop();

                Predicate<JsonObject> result = makeCompoundPredicate(first, op, second);
                predicateStack.push(result);

            } else {
                break;
            }
        }
    }

    /**
     * Makes either AND or OR compound predicate from two predicates depending on operator type
     */
    private static Predicate<JsonObject> makeCompoundPredicate(Predicate<JsonObject> first, Operator op,
                                                               Predicate<JsonObject> second) {
        Predicate<JsonObject> predicate;

        switch(op) {
            case AND:
                predicate = first.and(second);
                break;
            case OR:
                predicate = first.or(second);
                break;
            default:
                throw new UnsupportedOperationException("cannot make compound predicate from: " + op.getSymbol());
        }

        return predicate;
    }



}
