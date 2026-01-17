package ink.reactor.launcher.logger.file;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.zip.GZIPOutputStream;

public final class FileGZIPCompressor {

    public static void compress(final Path filePath, int gzipLevel) throws IOException {
        final Path parentPath = filePath.getParent();

        final BasicFileAttributes attributes = Files.readAttributes(filePath, BasicFileAttributes.class);
        final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US)
            .withZone(ZoneId.systemDefault());

        final String formattedDate = formatter.format(attributes.lastModifiedTime().toInstant());

        final String fileName = (parentPath == null)
            ? formattedDate
            : parentPath.toString() + '/' + formattedDate;

        if (gzipLevel == -1) {
            Files.move(filePath, Path.of(fileName + ".log"));
            return;
        }

        if (gzipLevel > 9) {
            gzipLevel = 9;
        }

        try (FileChannel sourceChannel = FileChannel.open(filePath, StandardOpenOption.READ)) {
            try (
                final OutputStream fos = Files.newOutputStream(Path.of(fileName + ".gzip"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                final GZIPOutputStream gzipOut = new CustomGZIPOutputStream(fos, 8192, gzipLevel)
            ) {
                final ByteBuffer buffer = ByteBuffer.allocate(8192);
                while (sourceChannel.read(buffer) > 0) {
                    buffer.flip();
                    gzipOut.write(buffer.array(), 0, buffer.limit());
                    buffer.clear();
                }
            }
        }

        Files.delete(filePath);
    }

    private static class CustomGZIPOutputStream extends GZIPOutputStream {
        public CustomGZIPOutputStream(final OutputStream out, final int size, final int level) throws IOException {
            super(out, size);
            def.setLevel(level);
        }
    }
}
