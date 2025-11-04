package utilcalc.core.parser;

import utilcalc.core.model.DateRange;

public final class ParserUtil {

	public static final String GROUP_HEADER_START_CHAR = "[";
	public static final String GROUP_HEADER_END_CHAR = "]";

	private ParserUtil() {
	}

	static DateRange parseRange(String text) {
		return DateRangeParser.parse(text);
	}

	static GroupHeader parseGroupHeader(String line) {
		if (!line.startsWith(GROUP_HEADER_START_CHAR) || !line.endsWith(GROUP_HEADER_END_CHAR)) {
			throw new ParsingException("Invalid group header: " + line);
		}
		String inside = line.substring(1, line.length() - 1);
		String[] parts = inside.split(":", 2);
		String name = parts[0].strip();
		String title = (parts.length == 2) ? parts[1].strip() : null;
		return new GroupHeader(name, title);
	}

	record GroupHeader(String name, String title) {
	}

	static String titleOrDefault(GroupHeader header, String defaultValue) {
		if (header.title() != null) {
			return header.title();
		}
		return defaultValue;
	}
}
