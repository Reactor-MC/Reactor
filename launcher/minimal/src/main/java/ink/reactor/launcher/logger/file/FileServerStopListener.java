package ink.reactor.launcher.logger.file;

import ink.reactor.kernel.event.Listener;
import ink.reactor.kernel.event.common.StopEvent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FileServerStopListener {

    private final FileWriter fileWriter;

    @Listener
    public void onServerStop(final StopEvent event) {
        fileWriter.flush();
        fileWriter.close();
    }
}
