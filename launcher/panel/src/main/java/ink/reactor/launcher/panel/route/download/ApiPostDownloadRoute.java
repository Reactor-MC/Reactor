package ink.reactor.launcher.panel.route.download;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@RequiredArgsConstructor
public final class ApiPostDownloadRoute implements Handler {
    private final Path dataFolder;

    @Override
    public void handle(@NotNull Context context) throws IOException, URISyntaxException {
        final String path = context.queryParam("path");
        if (path == null || path.isBlank()) {
            context.status(HttpStatus.BAD_REQUEST);
            return;
        }

        final String uri = context.queryParam("uri");
        if (uri == null || uri.isBlank()) {
            context.status(HttpStatus.BAD_REQUEST);
            return;
        }
        downloadPerUri(path, uri, context);
    }

    private void downloadPerUri(final String path, final String uriString, final Context context) throws IOException, URISyntaxException {
        final URI uri = new URI(uriString);
        final Path filePath = Path.of(dataFolder.toString(), path);

        Files.createDirectories(filePath);
        Files.copy(
            uri.toURL().openStream(),
            filePath,
            StandardCopyOption.REPLACE_EXISTING
        );
        context.status(HttpStatus.OK);
    }
}