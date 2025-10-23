package utilcalc.core.parser;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class ExprParserTest {

    @ParameterizedTest(name = "{index} ⇒ {0} = {1}")
    @CsvSource(
            textBlock =
                    """
        1+2, 3.0
        5-3, 2.0
        4*2, 8.0
        8/4, 2.0
        1+2*3, 7.0
        (1+2)*3, 9.0
        6-3-1, 2.0
        9+8-7*(6/5), 8.6
        '  1 +  2 * ( 3  +  1 ) ', 9.0
        1.5+2.3, 3.8
    """)
    @DisplayName("Parses and evaluates valid expressions")
    void evaluatesValidExpressions(String expr, double expected) {
        BigDecimal result = ExprParser.parse(expr);
        assertEquals(
                BigDecimal.valueOf(expected).stripTrailingZeros(),
                result.stripTrailingZeros(),
                () -> "Expression: " + expr);
    }

    @ParameterizedTest(name = "{index} ⇒ throws ArithmeticException: {0}")
    @ValueSource(strings = {"5/0", "10/(2-2)"})
    @DisplayName("Division by zero should throw")
    void divisionByZeroThrows(String expr) {
        assertThrows(ArithmeticException.class, () -> ExprParser.parse(expr));
    }

    @ParameterizedTest(name = "{index} ⇒ throws Exception: {0}")
    @ValueSource(strings = {"abc", "1+", "+2", "(1+2", "1+2)"})
    @DisplayName("Invalid expressions should throw")
    void invalidExpressionsThrow(String expr) {
        assertThrows(ParsingException.class, () -> ExprParser.parse(expr));
    }
}
