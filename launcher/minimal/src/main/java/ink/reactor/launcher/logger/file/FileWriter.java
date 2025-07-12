package ink.reactor.launcher.logger.file;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public final class FileWriter {
    private final long maxFileLength;
    private final int bufferSize;
    private final FileChannel channel;

    private byte[] buffer;
    private int position;
    private long currentFileLength;

    public FileWriter(long maxFileLength, int bufferSize, FileChannel channel) {
        this.maxFileLength = maxFileLength;
        this.bufferSize = bufferSize;
        this.channel = channel;
        this.buffer = new byte[bufferSize];
    }

    public boolean canWrite() {
        return currentFileLength < maxFileLength;
    }

    void write(final byte[] message) {
        if (currentFileLength + message.length >= maxFileLength) {
            return;
        }

        if (position + message.length > bufferSize) {
            flush();
        }

        currentFileLength += message.length;
        System.arraycopy(message, 0, buffer, position, message.length);
        position += message.length;
    }

    public void flush() {
        if (currentFileLength >= maxFileLength) {
            if (buffer == null) {
                return;
            }
        }
        try (final FileLock _ = channel.lock()) {
            channel.position(channel.size());
            channel.write(ByteBuffer.wrap(buffer, 0, position));
        } catch (IOException e) {
            e.printStackTrace(System.err);
        } finally {
            this.buffer = new byte[bufferSize];
            this.position = 0;
        }
    }

    public int getBufferPosition() {
        return position;
    }
}
