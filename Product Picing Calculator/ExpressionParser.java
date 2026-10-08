public class ExpressionParser {

    private String text;
    private int pos;

    public static double evaluate(String input) {
        if (input == null || input.trim().isEmpty()) {
            return Double.NaN;
        }
        ExpressionParser p = new ExpressionParser();
        p.text = input;
        p.pos = 0;
        try {
            double result = p.expression();
            p.skipSpaces();
            if (p.pos != p.text.length() || Double.isNaN(result) || Double.isInfinite(result)) {
                return Double.NaN;
            }
            return result;
        } catch (RuntimeException e) {
            return Double.NaN;
        }
    }

    private double expression() {
        double value = term();
        skipSpaces();
        while (pos < text.length() && (text.charAt(pos) == '+' || text.charAt(pos) == '-')) {
            char op = text.charAt(pos++);
            double next = term();
            if (op == '+') {
                value += next;
            } else {
                value -= next;
            }
            skipSpaces();
        }
        return value;
    }

    private double term() {
        double value = factor();
        skipSpaces();
        while (pos < text.length() && (isTimes(text.charAt(pos)) || isDivide(text.charAt(pos)))) {
            char op = text.charAt(pos++);
            double next = factor();
            if (isTimes(op)) {
                value *= next;
            } else {
                if (next == 0) {
                    throw new IllegalArgumentException("division by zero");
                }
                value /= next;
            }
            skipSpaces();
        }
        return value;
    }

    private boolean isTimes(char c) {
        return c == '*' || c == 'x' || c == 'X' || c == '\u00D7';
    }

    private boolean isDivide(char c) {
        return c == '/' || c == '\u00F7';
    }

    private double factor() {
        skipSpaces();
        if (pos >= text.length()) {
            throw new IllegalArgumentException("ran out of text");
        }
        char c = text.charAt(pos);

        if (c == '-') {
            pos++;
            return -factor();
        }
        if (c == '+') {
            pos++;
            return factor();
        }
        if (c == '(') {
            pos++;
            double value = expression();
            skipSpaces();
            if (pos >= text.length() || text.charAt(pos) != ')') {
                throw new IllegalArgumentException("missing )");
            }
            pos++;
            return value;
        }

        int start = pos;
        while (pos < text.length() && (Character.isDigit(text.charAt(pos)) || text.charAt(pos) == '.')) {
            pos++;
        }
        if (start == pos) {
            throw new IllegalArgumentException("expected a number");
        }
        return Double.parseDouble(text.substring(start, pos));
    }

    private void skipSpaces() {
        while (pos < text.length() && Character.isWhitespace(text.charAt(pos))) {
            pos++;
        }
    }
}
