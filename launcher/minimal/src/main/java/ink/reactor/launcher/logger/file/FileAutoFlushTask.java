package ink.reactor.launcher.logger.file;

public final class FileAutoFlushTask implements Runnable {
    private final FileWriter fileWriter;
    private final int minBufferSizeToFlush;
    private final int intervalSeconds;

    private FileAutoFlushTask(FileWriter fileWriter, int minBufferSizeToFlush, int intervalSeconds) {
        this.fileWriter = fileWriter;
        this.minBufferSizeToFlush = minBufferSizeToFlush;
        this.intervalSeconds = intervalSeconds;
    }

    public static Thread createThread(final FileWriter fileWriter, final int minBufferSizeToFlush, final int intervalSeconds) {
        final FileAutoFlushTask fileAutoFlushTask = new FileAutoFlushTask(
            fileWriter,
            Math.max(1, minBufferSizeToFlush),
            Math.max(1, intervalSeconds));

        return Thread.ofVirtual().name("log-flusher", 0)
            .factory()
            .newThread(fileAutoFlushTask);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (!fileWriter.canWrite()) {
                    return;
                }
                final int bufferPosition = fileWriter.getBufferPosition();

                if (bufferPosition >= minBufferSizeToFlush) {
                    fileWriter.flush();
                }
                Thread.sleep(intervalSeconds * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.println("Error in auto-flush task: " + e.getMessage());
            }
        }
    }
}
