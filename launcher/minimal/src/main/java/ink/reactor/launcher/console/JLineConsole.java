package ink.reactor.launcher.console;

import ink.reactor.launcher.ReactorLauncher;
import lombok.experimental.UtilityClass;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;

@UtilityClass
public final class JLineConsole {

    public static Console createConsole() {
        final Terminal terminal;
        try {
            terminal = TerminalBuilder.builder()
                .system(true)
                .build();
        } catch (final IOException e) {
            System.err.println("Error on creating terminal");
            e.printStackTrace(System.err);
            return null;
        }

        final LineReader reader = LineReaderBuilder.builder()
            .terminal(terminal)
            .highlighter(new ConsoleHighlighter())
            .completer(new ConsoleCompleter())
            .build();

        final Console console = new Console(terminal, reader);
        ReactorLauncher.STOP_TASKS.add(console::stop);

        return console;
    }
}
