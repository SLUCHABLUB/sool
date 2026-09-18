package sool;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class StreamUtilities {
    private StreamUtilities() {}

    public static <T> Stream<T> fromOptionSupplier(Supplier<Optional<T>> supplier) {
        var fused = Stream.iterate(supplier.get(), Optional::isPresent, last -> supplier.get());

        return fused.flatMap(Optional::stream);
    }
}
