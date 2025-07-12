package ink.reactor.launcher.console;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class Console {
    private final ConsoleLineReader lineReader;

    public void run() {
        lineReader.run();
    }

    public void stop() {
        lineReader.stop();
    }
}
