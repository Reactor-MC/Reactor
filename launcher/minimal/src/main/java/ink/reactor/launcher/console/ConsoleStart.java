package ink.reactor.launcher.console;

import ink.reactor.kernel.event.EventBus;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.io.PrintWriter;

public final class ConsoleStart {

    public static TerminalHolder createTerminal() {
        try {
            final Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .build();
            return new TerminalHolder(terminal, terminal.writer());
        } catch (final IOException e) {
            System.err.println("Error on creating terminal");
            e.printStackTrace(System.err);
            return null;
        }
    }

    public static Console createConsole(final TerminalHolder terminalHolder, final EventBus eventBus) {
         final LineReader reader = LineReaderBuilder.builder()
            .terminal(terminalHolder.terminal)
            .highlighter(new ConsoleHighlighter())
            .completer(new ConsoleCompleter())
            .build();

        final ConsoleLineReader lineReader = new ConsoleLineReader(terminalHolder.terminal, reader);
        eventBus.register(new ConsoleStopListener(lineReader));

        return new Console(lineReader);
    }

    public record TerminalHolder(Terminal terminal, PrintWriter writer){}
}
