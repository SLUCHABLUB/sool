package sool;

import static java.util.Locale.ROOT;

public final record Cursor(int line, int row) {

    public static final Cursor START = new Cursor(1, 1);

    public Cursor movedBy(UnicodeScalar scalar) {
        return scalar.isNewline() ? this.movedToNextLine() : this.movedForward();
    }

    public Cursor movedForward() {
        return new Cursor(line, row + 1);
    }

    public Cursor movedToNextLine() {
        return new Cursor(line + 1, 0);
    }

    @Override
    public final String toString() {
        return String.format(ROOT, "%d:%d", line, row);
    }
}
