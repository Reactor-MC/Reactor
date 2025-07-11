package ink.reactor.launcher.console;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.List;

final class ConsoleCompleter implements Completer {

    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        if (line.line().indexOf(' ') == -1) {
            // TODO: Command complete
            candidates.add(new Candidate("stop"));
        }
    }
}
