package ink.reactor.launcher.panel.route.files;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
public final class ApiFilesRoute implements Handler {

    private final BigInteger UNKNOWN_FILE_SIZE = BigInteger.valueOf(-1);
    private final String rootFolder;

    @Override
    public void handle(@NotNull Context context) throws IOException {
        String path = context.queryParam("path");
        if (path == null || path.isBlank()) {
            path = "";
        }

        final Path destinationPath = Path.of(rootFolder, path);
        if (!Files.isDirectory(destinationPath)) {
            context.status(HttpStatus.NOT_FOUND);
            return;
        }

        context.json(loadFiles(destinationPath));
        context.status(HttpStatus.OK);
    }

    private Collection<FileModel> loadFiles(final Path path) throws IOException {
        try (final DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            final Collection<FileModel> models = new ArrayList<>();
            for (final Path pathFile : stream) {
                models.add(toFileModel(pathFile));
            }
            return models;
        }
    }

    private FileModel toFileModel(final Path path) throws IOException {
        final BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
        final long modifiedTime = attributes.lastModifiedTime().toInstant().getEpochSecond();
        final String name = path.getFileName().toString();

        final BigInteger size;
        if (attributes.isRegularFile()) {
            size = BigInteger.valueOf(attributes.size());
        } else {
            size = UNKNOWN_FILE_SIZE;
        }

        return new FileModel(size, modifiedTime, name);
    }
}