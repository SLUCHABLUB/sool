package sool;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public final class Token {

    private static boolean mayStartIdentifier(UnicodeScalar scalar) {
        return scalar.isAlphabetic() || scalar.isUnderscore();
    }

    private static boolean isIdentifierPart(UnicodeScalar scalar) {
        return mayStartIdentifier(scalar) || scalar.isDigit();
    }

    public enum Kind {
        IDENTIFIER(Token::mayStartIdentifier, kind -> new Parser.Homogenous(kind, Token::isIdentifierPart)),
        NUMBER(UnicodeScalar::isDigit, kind -> new Parser.Homogenous(kind, UnicodeScalar::isDigit)),
        ERRONEOUS(_ -> true, Parser.SingleScalar::new);

        @SuppressWarnings("ImmutableEnumChecker")
        private final Predicate<UnicodeScalar> start;

        @SuppressWarnings("ImmutableEnumChecker")
        private final Function<Kind, Parser> parser;

        Kind(Predicate<UnicodeScalar> start, Function<Kind, Parser> parser) {
            this.start = start;
            this.parser = parser;
        }

        public final Parser parser() {
            return parser.apply(this);
        }

        private boolean mayStartWith(UnicodeScalar scalar) {
            return start.test(scalar);
        }

        public static Kind fromInitial(UnicodeScalar scalar) {
            return Arrays.stream(Kind.values())
                    .filter(kind -> kind.mayStartWith(scalar))
                    .findFirst()
                    .orElse(ERRONEOUS);
        }
    }

    interface Parser {

        boolean shouldAppend(UnicodeScalar scalar);

        Kind tokenKind();

        default Token parse(Peeker<UnicodeScalar> scalars) {
            var string = new StringBuilder();

            scalars.skipWhilePeeking(peek -> {
                var shouldAppend = shouldAppend(peek);

                if (shouldAppend) {
                    string.appendCodePoint(peek.toCodePoint());
                }

                return shouldAppend;
            });

            return new Token(tokenKind(), string.toString());
        }

        record Homogenous(Kind tokenKind, Predicate<UnicodeScalar> characterFilter) implements Parser {

            @Override
            public boolean shouldAppend(UnicodeScalar at) {
                return characterFilter.test(at);
            }
        }

        final class SingleScalar implements Parser {
            private boolean done = false;
            private final Kind tokenKind;

            SingleScalar(Kind tokenKind) {
                this.tokenKind = tokenKind;
            }

            @Override
            public boolean shouldAppend(UnicodeScalar at) {
                var shouldAppend = !done;
                done = true;
                return shouldAppend;
            }

            @Override
            public Kind tokenKind() {
                return tokenKind;
            }
        }
    }

    public final Kind kind;
    public final String string;

    private Token(Kind kind, String string) {
        this.kind = kind;
        this.string = string;
    }

    @Override
    public String toString() {
        return String.format("%s:%s", kind, string);
    }
}
