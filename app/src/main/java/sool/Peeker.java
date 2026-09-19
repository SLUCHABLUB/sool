package sool;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface Peeker<T> extends SafeIterator<T> {

    Optional<T> peekNext();

    default void skipWhilePeeking(Predicate<T> shouldTake) {
        while (peekNext().map(shouldTake::test).orElse(false)) {
            skipNext();
        }
    }

    static <T> Peeker<T> fromStream(Stream<T> stream) {
        return new Wrapper<T>(SafeIterator.fromIterator(stream.iterator()));
    }

    final class Wrapper<T> implements Peeker<T> {
        private Optional<T> next = Optional.empty();
        private final SafeIterator<T> iterator;

        private Wrapper(SafeIterator<T> iterator) {
            this.iterator = iterator;
        }

        @Override
        public Optional<T> takeNext() {

            var result = next;
            next = Optional.empty();
            return result.or(iterator::takeNext);
        }

        @Override
        public Optional<T> peekNext() {
            if (next.isEmpty()) {
                next = iterator.takeNext();
            }

            return next;
        }
    }
}
