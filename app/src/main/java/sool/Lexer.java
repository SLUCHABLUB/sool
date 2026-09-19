package sool;

import java.util.Optional;
import java.util.stream.Stream;

public final class Lexer {

    private final UnicodeScalarStream scalars;
    private Cursor cursor = Cursor.START;

    public Lexer(String string) {
        this.scalars = new UnicodeScalarStream(string);
    }

    private void skipNextScalar() {
        var next = scalars.peekNext();

        next.ifPresent(scalar -> cursor = cursor.movedBy(scalar));
    }

    private void skipWhitespace() {
        while (scalars.peekNext().map(UnicodeScalar::isWhitespace).orElse(false)) {
            skipNextScalar();
        }
    }

    public Optional<Token> nextToken() {
        skipWhitespace();

        return scalars.peekNext().flatMap(Token.Kind::fromUnicodeScalar).flatMap(tokenKind -> {
            var string = scalars.takeWhile(tokenKind.characterFilter);

            return tokenKind.parseToken(string);
        });
    }

    public Stream<Token> toStream() {
        return StreamUtilities.fromOptionSupplier(this::nextToken);
    }
}
