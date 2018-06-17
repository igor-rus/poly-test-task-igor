package io.polybius.testtask;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class Expression {

    private String operand;
    private Operator operator;
    private String value;
    private boolean isValueNumeric;

    public String getOperand() {
        return operand;
    }

    public String getValue() {
        return value;
    }

    public Operator getOperator() {
        return operator;
    }

    public boolean isValueNumeric() {
        return isValueNumeric;
    }


	Expression(String query) {
		Operator operator = Arrays.stream(Operator.values())
                .filter(op -> query.contains(op.getSymbol()))
                .reduce((first, second) -> second)
                .get();
        int beginIndex = query.indexOf(operator.getSymbol()) + operator.getSymbol().length();
        this.operand = query.substring(0, query.indexOf(operator.getSymbol()));
        this.value = query.substring(beginIndex, query.length());
        this.isValueNumeric = StringUtils.isNumeric(query.substring(beginIndex, query.length()));
        this.operator = operator;
    }
}
