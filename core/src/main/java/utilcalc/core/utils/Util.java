package utilcalc.core.utils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;

/** Utility class for data validation. */
public final class Util {

    private Util() {
        throw new AssertionError("Cannot instantiate Util");
    }

    public static <T> void ensureNonNull(T value, String name) {
        if (value == null) {
            throw new IllegalArgumentException(name + " must not be null");
        }
    }

    public static void ensureNonBlank(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
    }

    public static <T> void ensureNonEmpty(Collection<T> collection, String name) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(name + " must not be empty");
        }
    }

    public static <T> void ensureNoNullElements(Collection<T> collection, String name) {
        if (collection != null && collection.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException(name + " must not contain null elements");
        }
    }

    public static <T extends Comparable<T>> void ensureValidDateRange(T startDate, T endDate) {
        if (startDate != null && endDate != null && endDate.compareTo(startDate) < 0) {
            throw new IllegalArgumentException("endDate must not be before startDate");
        }
    }

    public static void ensureNotNegativeBigDecimalValue(BigDecimal value, String name) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(name + " must not be a negative value");
        }
    }

    public static <T> T castOrThrow(Object object, Class<T> clazz) {
        if (clazz.isInstance(object)) {
            return clazz.cast(object);
        }
        throw new IllegalArgumentException("Expected type "
                + clazz.getSimpleName()
                + ", but got: "
                + (object == null ? "null" : object.getClass().getSimpleName()));
    }
}
