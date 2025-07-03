package ink.reactor.launcher.panel;

import ink.reactor.launcher.panel.route.download.ApiGetDownloadRoute;
import ink.reactor.launcher.panel.route.download.ApiPostDownloadRoute;
import ink.reactor.launcher.panel.route.files.ApiFilesRoute;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;

import java.nio.file.Path;

public final class PanelMain {

    public static void main(String[] args) {
        final Javalin javalin = Javalin.create((config) -> {
            config.jsonMapper(new JavalinJackson());
            config.staticFiles.add(staticFiles -> {
                staticFiles.directory = "/common";
                staticFiles.hostedPath = "/common";
            });
            config.staticFiles.add(staticFiles -> {
                staticFiles.directory = "/pages";
                staticFiles.hostedPath = "/pages";
            });
            config.staticFiles.add(staticFiles -> {
                staticFiles.directory = "/themes";
                staticFiles.hostedPath = "/themes";
            });
        });

        javalin.get("/", ctx -> {
            ctx.redirect("pages/file");
        });

        final Path rootDirectory = Path.of(System.getProperty("user.dir"));

        javalin.post("api/download", new ApiPostDownloadRoute(rootDirectory));
        javalin.get("api/download", new ApiGetDownloadRoute(rootDirectory));

        javalin.get("api/files", new ApiFilesRoute(rootDirectory.toString()));

        javalin.start(8080);
    }
}