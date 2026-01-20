package ink.reactor.launcher.logger.file;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public final class FileWriter {
    private final long maxFileLength;
    private final FileChannel channel;
    private final ByteBuffer internalBuffer;

    private final BlockingQueue<byte[]> queue = new ArrayBlockingQueue<>(2048);
    private long currentFileLength;

    public FileWriter(long maxFileLength, int bufferSize, FileChannel channel) {
        this.maxFileLength = maxFileLength;
        this.channel = channel;
        this.internalBuffer = ByteBuffer.allocateDirect(bufferSize);
    }

    public BlockingQueue<byte[]> getQueue() {
        return queue;
    }

    public boolean canWrite() {
        return currentFileLength < maxFileLength && channel.isOpen();
    }

    public void write(final byte[] message) {
        if (message == null || !canWrite()) {
            return;
        }

        if (!this.queue.offer(message)) {
            System.err.println("[File Logger] Queue full, dropping log message");
        }
    }

    public void processQueue() {
        byte[] data;
        try {
            while ((data = queue.poll()) != null) {
                if (data.length > internalBuffer.remaining()) {
                    flushToDisk();
                }

                if (data.length > internalBuffer.capacity()) {
                    channel.write(ByteBuffer.wrap(data));
                    currentFileLength += data.length;
                    continue;
                }

                internalBuffer.put(data);
                internalBuffer.put((byte) '\n');
                currentFileLength += data.length + 1;
            }
            flushToDisk();
        } catch (IOException e) {
            System.err.println("Critical error writing to log file");
            e.printStackTrace(System.err);
        }
    }

    private void flushToDisk() throws IOException {
        if (internalBuffer.position() == 0) {
            return;
        }

        internalBuffer.flip();
        channel.write(internalBuffer);
        internalBuffer.clear();
    }

    public void close() {
        processQueue();
        try {
            channel.close();
        } catch (IOException e) {
            System.err.println("Error on close file channel");
            e.printStackTrace(System.err);
        }
    }
}
