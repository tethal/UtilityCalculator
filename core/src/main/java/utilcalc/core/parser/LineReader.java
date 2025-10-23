package utilcalc.core.parser;

import static utilcalc.core.parser.ParserUtil.GROUP_HEADER_START_CHAR;
import static utilcalc.core.utils.Util.ensureNonNull;

import java.util.ArrayList;
import java.util.List;

class LineReader {
    private final List<String> lines;
    private int index = 0;

    LineReader(String input) {
        ensureNonNull(input, "input");
        this.lines = preprocess(input);
    }

    boolean hasMore() {
        return index < lines.size();
    }

    String nextLine() {
        if (!hasMore()) {
            throw new ParsingException("Unexpected end of file");
        }
        return lines.get(index++);
    }

    String peek() {
        if (!hasMore()) {
            throw new ParsingException("Unexpected end of file");
        }
        return lines.get(index);
    }

    List<String> readAllUntilNextGroup() {
        List<String> result = new ArrayList<>();
        while (hasMore() && !peek().startsWith(GROUP_HEADER_START_CHAR)) {
            result.add(nextLine());
        }
        return result;
    }

    private static List<String> preprocess(String input) {
        return input.lines()
                .map(LineReader::normalizeLine)
                .filter(line -> !line.isEmpty())
                .toList();
    }

    private static String normalizeLine(String line) {
        String removeComments = line.replaceAll("#.*$", "");
        return removeComments.strip();
    }
}
