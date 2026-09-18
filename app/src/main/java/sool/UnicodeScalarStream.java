package sool;

import java.util.Optional;
import java.util.function.Predicate;

public final class UnicodeScalarStream {

    private int nextIndex = 0; // >= 0
    private final String string;

    public UnicodeScalarStream(String string) {
        this.string = string;
    }

    public Optional<UnicodeScalar> peekNext() {
        return nextIndex < string.length() ? UnicodeScalar.fromNumber(string.codePointAt(nextIndex)) : Optional.empty();
    }

    public void skipNext() {
        peekNext().ifPresent(codePoint -> nextIndex += codePoint.bmpCount());
    }

    public Optional<UnicodeScalar> takeNext() {
        var next = peekNext();

        skipNext();

        return next;
    }

    public String takeWhile(Predicate<UnicodeScalar> predicate) {
        var startIndex = nextIndex;

        while (peekNext().map(predicate::test).orElse(false)) {
            skipNext();
        }

        var endIndex = nextIndex;

        return string.substring(startIndex, endIndex);
    }
}
