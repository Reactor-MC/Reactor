package ink.reactor.launcher.logger.file;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.concurrent.locks.ReentrantLock;

import java.util.concurrent.locks.Lock;

public final class FileWriter {
    private final long maxFileLength;

    private final FileChannel channel;
    private final Lock lock = new ReentrantLock();
    private final ByteBuffer buffer;

    private long currentFileLength;

    public FileWriter(long maxFileLength, int bufferSize, FileChannel channel) {
        this.maxFileLength = maxFileLength;
        this.channel = channel;
        this.buffer = ByteBuffer.allocateDirect(bufferSize);
    }

    public boolean canWrite() {
        return currentFileLength < maxFileLength && channel.isOpen();
    }

    public void write(final byte[] message) {
        if ((message == null || message.length == 0) || currentFileLength >= maxFileLength) {
            return;
        }

        lock.lock();
        try {
            if (message.length > buffer.remaining()) {
                flushInternal();
            }
            buffer.put(message);
            currentFileLength += message.length;
        } finally {
            lock.unlock();
        }
    }

    public void flush() {
        lock.lock();
        try {
            flushInternal();
        } finally {
            lock.unlock();
        }
    }

    private void flushInternal() {
        buffer.flip();
        try (final FileLock _ = channel.tryLock()) {
            channel.position(channel.size());
            channel.write(buffer);
        } catch (IOException e) {
            System.err.println("Failed to flush buffer to file channel");
            e.printStackTrace(System.err);
        } finally {
            this.buffer.clear();
        }
    }

    public int getBufferPosition() {
        lock.lock();
        try {
            return this.buffer.position();
        } finally {
            lock.unlock();
        }
    }

    public void close() {
        lock.lock();
        try {
            channel.close();
        } catch (IOException e) {
            System.err.println("Error on closing file channel");
            e.printStackTrace(System.err);
        } finally {
            lock.unlock();
        }
    }
}
