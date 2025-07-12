package ink.reactor.launcher.console;

import org.jline.reader.Highlighter;
import org.jline.reader.LineReader;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

import java.util.regex.Pattern;

final class ConsoleHighlighter implements Highlighter {

    private final AttributedStyle unknownCommand = new AttributedStyle()
        .foreground(AttributedStyle.RED, AttributedStyle.RED, AttributedStyle.RED);

    @Override
    public AttributedString highlight(LineReader reader, String buffer) {
        // TODO: Command highlighter
        if (!buffer.startsWith("stop")) {
            return new AttributedString(buffer, unknownCommand);
        }
        return new AttributedString(buffer);
    }

    public void setErrorPattern(Pattern errorPattern) {}
    public void setErrorIndex(int errorIndex) {}
}
