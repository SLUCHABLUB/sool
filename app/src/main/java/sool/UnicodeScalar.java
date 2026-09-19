package sool;

import java.util.Optional;
import java.util.stream.Stream;

public final class UnicodeScalar {

    private final int codePoint;

    private UnicodeScalar(int inner) {
        this.codePoint = inner;
    }

    public static Optional<UnicodeScalar> fromCodePoint(int number) {
        return Character.isDefined(number) ? Optional.of(new UnicodeScalar(number)) : Optional.empty();
    }

    public static Stream<UnicodeScalar> stream(String string) {
        return string.codePoints().mapToObj(UnicodeScalar::fromCodePoint).flatMap(Optional::stream);
    }

    public int toCodePoint() {
        return codePoint;
    }

    public int bmpCount() {
        return Character.charCount(this.codePoint);
    }

    public boolean isAlphabetic() {
        return Character.isAlphabetic(this.codePoint);
    }

    public boolean isDigit() {
        return Character.isDigit(this.codePoint);
    }

    public boolean isWhitespace() {
        return Character.isWhitespace(this.codePoint);
    }

    public boolean isNewline() {
        return this.codePoint == '\n';
    }

    public boolean isUnderscore() {
        return this.codePoint == '_';
    }
}
