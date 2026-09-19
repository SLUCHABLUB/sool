package sool;

import java.util.Optional;
import java.util.stream.Stream;

public final class Lexer {

    private final FileIterator scalars;

    public Lexer(String string) {
        this.scalars = new FileIterator(string);
    }

    private void skipWhitespace() {
        while (scalars.peekNext().map(UnicodeScalar::isWhitespace).orElse(false)) {
            scalars.skipNext();
        }
    }

    public Optional<Token> nextToken() {
        skipWhitespace();

        return scalars.peekNext()
                .map(Token.Kind::fromInitial)
                .map(Token.Kind::parser)
                .map(parser -> parser.parse(scalars));
    }

    public Stream<Token> toStream() {
        return Streams.fromOptionSupplier(this::nextToken);
    }
}
