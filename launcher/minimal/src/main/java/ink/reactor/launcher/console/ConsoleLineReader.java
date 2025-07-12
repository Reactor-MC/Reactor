package ink.reactor.launcher.console;

import lombok.RequiredArgsConstructor;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.MaskingCallback;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

import java.io.IOException;

@RequiredArgsConstructor
final class ConsoleLineReader {

    private final Terminal terminal;
    private final LineReader reader;

    private boolean run = true;

    void run() {
        while(run) {
            final String line;
            try {
                line = reader.readLine("> ", null, (MaskingCallback) null, null).trim();
            } catch (UserInterruptException | EndOfFileException e) {
                stop();
                continue;
            }

            if (line.isEmpty()) {
                continue;
            }

            switch (line) {
                case "stop":
                    stop();
                    return;
                case "clear":
                    terminal.puts(InfoCmp.Capability.clear_screen);
                    break;
            }
        }
    }

    void stop() {
        try {
            terminal.close();
        } catch (final IOException e) {
            System.err.println("Error trying to stop the terminal");
            e.printStackTrace(System.err);
        } finally {
            run = false;
        }
    }
}
