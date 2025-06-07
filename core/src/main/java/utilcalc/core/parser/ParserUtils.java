package utilcalc.core.parser;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.tomlj.TomlArray;
import org.tomlj.TomlTable;

final class ParserUtils {

    private ParserUtils() {}

    static void checkThatSectionContainsOnlyKnownFields(
            TomlTable section, Set<String> knownFields, String sectionName) {
        if (!knownFields.containsAll(section.keySet())) {
            List<String> unknownFields =
                    section.keySet().stream().filter(key -> !knownFields.contains(key)).toList();

            throw new ParsingException(
                    "Section " + sectionName + " contains unknown fields: " + unknownFields);
        }
    }

    static TomlTable requireTable(TomlTable table, String key) {
        return requireNonNull(table.getTable(key), key, "table");
    }

    static LocalDate requireLocalDate(TomlTable table, String key) {
        return requireNonNull(table.getLocalDate(key), key, "date");
    }

    static String requireString(TomlTable table, String key) {
        return requireNonNull(table.getString(key), key, "string");
    }

    static <T> List<T> requireList(TomlTable table, String key, Class<T> clazz) {
        TomlArray array = requireNonNull(table.getArray(key), key, "array");
        try {
            return array.toList().stream().map(clazz::cast).toList();
        } catch (ClassCastException e) {
            throw new ParsingException("Array contains " + clazz + " elements: " + key, e);
        }
    }

    static <T> List<T> optionalList(TomlTable table, String key, Class<T> clazz) {
        TomlArray array = table.getArray(key);
        if (array == null) {
            return List.of();
        }
        try {
            return array.toList().stream().map(clazz::cast).toList();
        } catch (ClassCastException e) {
            throw new ParsingException("Array contains " + clazz + " elements: " + key, e);
        }
    }

    private static <T> T requireNonNull(T input, String key, String dataType) {
        if (input == null) {
            throw new ParsingException("Missing required " + dataType + " field: " + key);
        }
        return input;
    }
}
