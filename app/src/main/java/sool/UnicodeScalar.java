package sool;

import java.util.Optional;

public final class UnicodeScalar {

    private final int inner;

    private UnicodeScalar(int inner) {
        this.inner = inner;
    }

    public static Optional<UnicodeScalar> fromNumber(int number) {
        return Character.isDefined(number) ? Optional.of(new UnicodeScalar(number)) : Optional.empty();
    }

    public int bmpCount() {
        return Character.charCount(this.inner);
    }

    public boolean isAlphabetic() {
        return Character.isAlphabetic(this.inner);
    }

    public boolean isWhitespace() {
        return Character.isWhitespace(this.inner);
    }

    public boolean isNewline() {
        return this.inner == '\n';
    }

    public boolean isUnderscore() {
        return this.inner == '_';
    }
}
