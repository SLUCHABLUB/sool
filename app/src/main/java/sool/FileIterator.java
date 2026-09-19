package sool;

import java.util.Optional;

public final class FileIterator implements Peeker<UnicodeScalar> {

    private Cursor cursor = Cursor.START;
    private final Peeker<UnicodeScalar> scalars;

    public FileIterator(String file) {
        this.scalars = Peeker.fromStream(UnicodeScalar.stream(file));
    }

    public Cursor getCursor() {
        return cursor;
    }

    @Override
    public Optional<UnicodeScalar> takeNext() {
        var next = scalars.takeNext();

        next.ifPresent(scalar -> cursor = cursor.movedBy(scalar));

        return next;
    }

    @Override
    public Optional<UnicodeScalar> peekNext() {
        return scalars.peekNext();
    }
}
