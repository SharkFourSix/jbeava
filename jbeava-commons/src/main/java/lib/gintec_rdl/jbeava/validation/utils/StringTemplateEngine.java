package lib.gintec_rdl.jbeava.validation.utils;

import java.util.*;

/**
 * <p>A template engine using simple expression parsing</p>
 */
public class StringTemplateEngine {
    public static final int SYMBOL_NAME_LENGTH = 30;
    private final int symbolNameLength;

    public StringTemplateEngine() {
        this(SYMBOL_NAME_LENGTH);
    }

    public StringTemplateEngine(int symbolNameLength) {
        this.symbolNameLength = symbolNameLength;
    }

    public String render(String template, Map<String, Object> model) {
        StringBuilder stringBuilder;
        final List<TokenGroup> tokenGroups;

        tokenGroups = this.tokenize(template);
        stringBuilder = new StringBuilder();
        Objects.requireNonNull(template, "Template string cannot be null.");

        // Here we use a pull approach that way values from the model cannot conflict with names and or values
        // of other variables.
        // Also, using regular expressions would lead to denial of service attacks due to iterative nature of `replaceAll`.
        for (TokenGroup tokenGroup : tokenGroups) {
            if (tokenGroup.isSymbol()) {
                stringBuilder.append(Objects.toString(model.get(tokenGroup.name), ""));
            } else {
                stringBuilder.append(tokenGroup.value);
            }
        }
        return stringBuilder.toString();
    }

    StringBuilder append(StringBuilder stringBuilder, char c) {
        if (stringBuilder == null) {
            stringBuilder = new StringBuilder();
        }
        return stringBuilder.append(c);
    }

    List<TokenGroup> tokenize(String template) {
        int literal;
        Token token;
        TokenIterator iterator;
        StringBuilder stringBuilder;
        List<TokenGroup> tokenGroups;

        literal = 1;
        stringBuilder = null;
        tokenGroups = new LinkedList<>();
        iterator = new TokenIterator(template);

        while (iterator.hasNext()) {
            token = iterator.next();
            if (token.literal == Token.TOKEN_ANCHOR) {
                if (iterator.hasNext()) {
                    if (iterator.peek().literal == Token.TOKEN_LBRACKET) {
                        StringBuilder builder;

                        // close token group
                        if (stringBuilder != null) {
                            tokenGroups.add(new Literal(format("literal#%d", literal++), stringBuilder.toString()));
                            stringBuilder = null;
                        }

                        builder = new StringBuilder();
                        iterator.skip(1);
                        while (iterator.hasNext()) {
                            token = iterator.next();
                            if (token.literal == Token.TOKEN_RBRACKET) {
                                break;
                            }
                            builder.append(token.literal);
                        }
                        String var = variable(builder.toString());
                        tokenGroups.add(new Symbol(var, var));
                    } else {
                        stringBuilder = append(stringBuilder, token.literal);
                    }
                } else {
                    stringBuilder = append(stringBuilder, token.literal);
                }
            } else {
                stringBuilder = append(stringBuilder, token.literal);
            }
        }
        if (stringBuilder != null) {
            tokenGroups.add(new Literal(format("literal#%d", literal), stringBuilder.toString()));
        }
        return tokenGroups;
    }

    private String variable(String string) {
        int length = string.length();
        if (!LocaleUtils.isNumberInRange(length, 1, symbolNameLength)) {
            throw new IllegalArgumentException(
                    format("Symbol name '%s' is too long. Must be within 1-%d chars.", string, symbolNameLength));
        }
        char c;
        for (int i = 0; i < length; i++) {
            c = string.charAt(i);
            if (!LocaleUtils.isNumberInRange((int) c, (int) 'a', (int) 'z')) {
                if (!LocaleUtils.isNumberInRange((int) c, (int) 'A', (int) 'Z')) {
                    if (c != '$' && c != '_') {
                        boolean isNumber = LocaleUtils.isNumberInRange((int) c, (int) '0', (int) '9');
                        if (i == 0 && isNumber || i > 0 && !isNumber) {
                            throw new IllegalArgumentException(
                                    format("Illegal character '%c' at offset %d in identifier '%s'.", c, i, string));
                        }
                    }
                }
            }
        }
        return string;
    }

    static abstract class TokenGroup {
        private final String name;
        private final String value;

        protected TokenGroup(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        abstract boolean isSymbol();

        public String getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TokenGroup)) return false;
            TokenGroup that = (TokenGroup) o;
            return name.equals(that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }

    private static class Literal extends TokenGroup {

        protected Literal(String name, String value) {
            super(name, value);
        }

        @Override
        boolean isSymbol() {
            return false;
        }
    }

    private static class Symbol extends TokenGroup {

        protected Symbol(String name, String value) {
            super(name, value);
        }

        @Override
        boolean isSymbol() {
            return true;
        }
    }

    private String format(String text, Object... args) {
        return String.format(Locale.US, text, args);
    }

    private static class Token {
        static final char TOKEN_LBRACKET = '{';
        static final char TOKEN_RBRACKET = '}';
        static final char TOKEN_ANCHOR = '$';

        private final int offset;
        private final char literal;

        Token(char literal, int offset) {
            this.literal = literal;
            this.offset = offset;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Token)) return false;
            Token token = (Token) o;
            return literal == token.literal;
        }

        @Override
        public int hashCode() {
            return Objects.hash(literal);
        }

        @Override
        public String toString() {
            return "Token{" +
                    "offset=" + offset +
                    ", literal=" + literal +
                    '}';
        }
    }

    static class TokenIterator implements Iterator<Token> {
        private int offset;
        private final int length;
        private final String input;

        TokenIterator(String input) {
            this.input = input;
            this.offset = 0;
            this.length = input.length();
        }

        @Override
        public boolean hasNext() {
            return offset < length;
        }

        public Token peek() {
            return at(offset);
        }

        @Override
        public Token next() {
            return at(offset++);
        }

        private Token at(int offset) {
            return new Token(input.charAt(offset), offset);
        }

        public void skip(int amount) {
            this.offset += amount;
        }
    }
}
