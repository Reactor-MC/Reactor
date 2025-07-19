package ink.reactor.launcher.console;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.MaskingCallback;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;

import java.io.IOException;

@RequiredArgsConstructor
@Getter
public final class Console {
    private final Terminal terminal;
    private final LineReader reader;
    private boolean run = true;

    public void run() {
        while (run) {
            final String line;
            try {
                line = reader.readLine("> ", null, (MaskingCallback) null, null).trim();
            } catch (UserInterruptException | EndOfFileException e) {
                stop();
                break;
            }
            if (line.equals("stop")) {
                System.out.println("Stopping server...");
                stop();
                break;
            }
            System.out.println("Execute: " + line);
        }
    }

    public void stop() {
        if (!run) {
            return;
        }
        try {
            terminal.close();
        } catch (final IOException e) {
            System.err.println("Error trying to stop the terminal");
            e.printStackTrace(System.err);
        } finally {
            run = false;
            Runtime.getRuntime().exit(0);
        }
    }
}
