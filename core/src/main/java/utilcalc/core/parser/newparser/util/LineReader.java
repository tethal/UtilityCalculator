package utilcalc.core.parser.newparser.util;

import static utilcalc.core.parser.newparser.util.ParserUtil.GROUP_HEADER_START_CHAR;
import static utilcalc.core.utils.Util.ensureNonNull;

import java.util.ArrayList;
import java.util.List;
import utilcalc.core.parser.ParsingException;

public class LineReader {
    private final List<String> lines;
    private int index = 0;

    public LineReader(final List<String> lines) {
        ensureNonNull(lines, "lines");
        this.lines = List.copyOf(lines);
    }

    public boolean hasMore() {
        return index < lines.size();
    }

    public String nextLine() {
        if (!hasMore()) {
            throw new ParsingException("Unexpected end of file");
        }
        return lines.get(index++);
    }

    public String peek() {
        if (!hasMore()) {
            throw new ParsingException("Unexpected end of file");
        }
        return lines.get(index);
    }

    public List<String> readAllUntilNextGroup() {
        List<String> result = new ArrayList<>();
        while (hasMore() && !peek().startsWith(GROUP_HEADER_START_CHAR)) {
            result.add(nextLine());
        }
        return result;
    }
}
