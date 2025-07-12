package ink.reactor.launcher.logger.file;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FileLogUpdater implements Runnable {
    private final FileWriter fileWriter;
    private final int minBufferSizeToFlush;

    @Override
    public void run() {
        if (!fileWriter.canWrite()) {
            return;
        }

        final int bufferPosition = fileWriter.getBufferPosition();

        if (bufferPosition >= minBufferSizeToFlush) {
            Thread.ofVirtual().start(fileWriter::flush);
        }
    }
}
