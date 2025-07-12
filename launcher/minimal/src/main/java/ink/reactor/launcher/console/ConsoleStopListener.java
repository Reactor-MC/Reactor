package ink.reactor.launcher.console;

import ink.reactor.kernel.event.Listener;
import ink.reactor.kernel.event.common.StopEvent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ConsoleStopListener {

    private final ConsoleLineReader lineReader;

    @Listener
    public void onServerStop(final StopEvent event) {
        lineReader.stop();
    }
}
