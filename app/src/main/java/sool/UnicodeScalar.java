package sool;

import java.util.Optional;

public final class UnicodeScalar {

    private final int inner;

    private UnicodeScalar(int inner) {
        this.inner = inner;
    }

    public static final Optional<UnicodeScalar> fromNumber(int number) {
        return Character.isDefined(number) ? Optional.of(new UnicodeScalar(number)) : Optional.empty();
    }

    public final int bmpCount() {
        return Character.charCount(this.inner);
    }

    public final boolean isAlphabetic() {
        return Character.isAlphabetic(this.inner);
    }

    public final boolean isWhitespace() {
        return Character.isWhitespace(this.inner);
    }

    public final boolean isNewline() {
        return this.inner == '\n';
    }

    public final boolean isUnderscore() {
        return this.inner == '_';
    }
}
