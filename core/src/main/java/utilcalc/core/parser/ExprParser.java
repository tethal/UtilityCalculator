package utilcalc.core.parser;

import java.math.BigDecimal;
import java.math.MathContext;

class ExprParser {

    private static final char PLUS_SYMBOL = '+';
    private static final char MINUS_SYMBOL = '-';
    private static final char MULTIPLY_SYMBOL = '*';
    private static final char DIVIDE_SYMBOL = '/';

    private static final char OPEN_PAREN_SYMBOL = '(';
    private static final char CLOSE_PAREN_SYMBOL = ')';

    private final String input;
    private int current;

    private ExprParser(String input) {
        this.input = input;
        this.current = 0;
    }

    static BigDecimal parse(String text) {
        ExprParser parser = new ExprParser(text.strip());
        BigDecimal result = parser.parseExpr();
        parser.skipWhitespace();
        if (parser.current != parser.input.length()) {
            throw new ParsingException("Unexpected input: " + parser.remaining());
        }
        return result;
    }

    private BigDecimal parseExpr() {
        BigDecimal value = parseTerm();
        while (true) {
            skipWhitespace();
            if (check(PLUS_SYMBOL)) {
                advance();
                value = value.add(parseTerm());
            } else if (check(MINUS_SYMBOL)) {
                advance();
                value = value.subtract(parseTerm());
            } else {
                break;
            }
        }
        return value;
    }

    private BigDecimal parseTerm() {
        BigDecimal value = parseFactor();
        while (true) {
            skipWhitespace();
            if (check(MULTIPLY_SYMBOL)) {
                advance();
                value = value.multiply(parseFactor());
            } else if (check(DIVIDE_SYMBOL)) {
                advance();
                BigDecimal divisor = parseFactor();
                value = value.divide(divisor, MathContext.DECIMAL64);
            } else {
                break;
            }
        }
        return value;
    }

    private BigDecimal parseFactor() {
        skipWhitespace();
        if (check(OPEN_PAREN_SYMBOL)) {
            advance();
            BigDecimal value = parseExpr();
            skipWhitespace();
            expect(CLOSE_PAREN_SYMBOL);
            return value;
        }
        return parseNumber();
    }

    private BigDecimal parseNumber() {
        skipWhitespace();

        int start = current;

        if (isNotAtEnd() && (input.charAt(current) == '-' || input.charAt(current) == '+')) {
            advance();
        }

        boolean hasDigits = false;
        while (isNotAtEnd() && (Character.isDigit(input.charAt(current)) || input.charAt(current) == '.')) {
            hasDigits = true;
            advance();
        }

        if (!hasDigits) {
            throw new ParsingException("Expected number at: " + remaining());
        }

        String numberStr = input.substring(start, current);

        try {
            return new BigDecimal(numberStr);
        } catch (NumberFormatException e) {
            throw new ParsingException("Invalid number: " + numberStr);
        }
    }

    private void skipWhitespace() {
        while (isNotAtEnd() && Character.isWhitespace(peek())) {
            advance();
        }
    }

    private boolean check(char c) {
        return isNotAtEnd() && peek() == c;
    }

    private char peek() {
        return input.charAt(current);
    }

    private boolean isNotAtEnd() {
        return current < input.length();
    }

    private void expect(char c) {
        if (!check(c)) {
            throw new ParsingException("Expected '" + c + "' at: " + remaining());
        }
        advance();
    }

    private void advance() {
        current++;
    }

    private String remaining() {
        return input.substring(current);
    }
}
