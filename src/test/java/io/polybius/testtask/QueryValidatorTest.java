package io.polybius.testtask;
import org.junit.Test;

public class QueryValidatorTest {

    @Test(expected = IllegalArgumentException.class)
    public void emptyQueryStringThrowsException() throws IllegalArgumentException {

        String query = "";
        QueryValidator.validateQuery(query);
    }

    @Test (expected = IllegalArgumentException.class)
    public void invalidExpressionShouldThowException() throws IllegalArgumentException {

        String query = "name=Bob && age>";
        QueryValidator.validateQuery(query);
    }

}