package sool;

import java.util.Iterator;
import java.util.Optional;

public interface SafeIterator<T> {
    Optional<T> takeNext();

    default void skipNext() {
        var _ = takeNext();
    }

    static <T> SafeIterator<T> fromIterator(Iterator<T> iterator) {
        return new Wrapper<T>(iterator);
    }

    record Wrapper<T>(Iterator<T> iterator) implements SafeIterator<T> {
        @Override
        public Optional<T> takeNext() {
            return iterator.hasNext() ? Optional.of(iterator.next()) : Optional.empty();
        }
    }
}
