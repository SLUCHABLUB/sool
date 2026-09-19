package sool;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

public final class Token {

    public enum Kind {
        IDENTIFIER(scalar -> scalar.isAlphabetic() || scalar.isUnderscore());

        @SuppressWarnings("Immutable")
        public final Predicate<UnicodeScalar> characterFilter;

        Kind(Predicate<UnicodeScalar> characterFilter) {
            this.characterFilter = characterFilter;
        }

        public static Optional<Kind> fromUnicodeScalar(UnicodeScalar scalar) {
            return Arrays.stream(Kind.values())
                    .filter(kind -> kind.characterFilter.test(scalar))
                    .findFirst();
        }

        @SuppressWarnings("HiddenField") // False positive
        public Optional<Token> parseToken(String string) {
            if (!string.codePoints()
                    .allMatch(codePoint -> UnicodeScalar.fromNumber(codePoint)
                            .map(this.characterFilter::test)
                            .orElse(false))) {
                return Optional.empty();
            }

            return Optional.of(new Token(this, string));
        }
    }

    public final Kind kind;
    public final String string;

    private Token(Kind kind, String string) {
        this.kind = kind;
        this.string = string;
    }
}
