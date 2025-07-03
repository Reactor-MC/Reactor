package ink.reactor.launcher.panel.route.download;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

@RequiredArgsConstructor
public final class ApiGetDownloadRoute implements Handler {
    private final Path dataFolder;

    @Override
    public void handle(@NotNull Context context) throws IOException {
        final String path = context.queryParam("path");
        if (path == null || path.isBlank()) {
            context.status(HttpStatus.BAD_REQUEST);
            return;
        }
        sendFile(path, context);
    }

    private void sendFile(final String path, final Context context) throws IOException {
        final Path filePath = Path.of(dataFolder.toString(), path);
        if (!Files.exists(filePath)) {
            context.status(HttpStatus.NOT_FOUND);
            return;
        }

        final BasicFileAttributes attributes = Files.readAttributes(filePath, BasicFileAttributes.class);
        if (attributes.isDirectory()) {
            context.status(HttpStatus.BAD_REQUEST).result("Downloading directories is not supported");
            return;
        }

        context.result(Files.newInputStream(filePath));
        context.status(HttpStatus.OK);
    }
}