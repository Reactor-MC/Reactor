package ink.reactor.launcher.logger;

import ink.reactor.kernel.logger.Logger;
import ink.reactor.launcher.ReactorLauncher;
import ink.reactor.launcher.logger.data.LoggerLevels;
import ink.reactor.launcher.logger.data.StyleLog;
import ink.reactor.launcher.logger.file.*;
import ink.reactor.launcher.logger.type.ConsoleLogger;
import ink.reactor.launcher.logger.type.FileLogger;
import ink.reactor.launcher.logger.type.NoneLogger;
import ink.reactor.launcher.logger.type.ReactorLogger;
import ink.reactor.microkernel.logger.JavaLoggerFormatter;
import ink.reactor.sdk.config.ConfigSection;
import ink.reactor.sdk.config.ConfigService;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;

@RequiredArgsConstructor
public final class LoggersLoader {

    private final PrintWriter consoleWriter;

    public Logger load(final ConfigService configService) {
        try {
            return load(configService.createIfAbsentAndLoad("logger.yml", getClass().getClassLoader()));
        } catch (final IOException e) {
            System.err.println("Can't load logger.yml");
            e.printStackTrace(System.err);
            return new NoneLogger();
        }
    }

    private Logger load(final ConfigSection section) {
        if (!section.getBoolean("enable")) {
            return new NoneLogger();
        }

        final ConsoleLogger console = loadConsoleLogger(section);
        final FileLogger fileLogger = loadFileLogger(section);
        if (console == null && fileLogger == null) {
            return new NoneLogger();
        }

        final ConfigSection prefix = section.getSection("prefix");
        final String dateFormatter = prefix.getString("date-formatter");
        return new ReactorLogger(
            new JavaLoggerFormatter(),
            console,
            fileLogger,
            prefix.getOrDefault("debug", ""),
            prefix.getOrDefault("log", ""),
            prefix.getOrDefault("info", ""),
            prefix.getOrDefault("warn", ""),
            prefix.getOrDefault("error", ""),
            dateFormatter == null ? null : DateTimeFormatter.ofPattern(dateFormatter)
        );
    }

    private FileLogger loadFileLogger(final ConfigSection section) {
        final ConfigSection logsSection = section.getSection("logs");
        final LoggerLevels loggerLevels = loadLevels(logsSection);
        if (loggerLevels == null) {
            return null;
        }

        final String logsFolder = logsSection.getOrDefault("logs-folder", "logs");
        final Path path = Path.of(logsFolder + "/latest.log");
        final FileChannel channel;

        channel = createLogChannel(path, logsSection);
        if (channel == null) return null;

        final FileWriter fileWriter = new FileWriter(
            logsSection.getLong("max-file-size"),
            logsSection.getOrDefault("bufferSection", 8192),
            channel
        );

        final ConfigSection autoFlush = logsSection.getSection("auto-flush");
        if (autoFlush != null && autoFlush.getBoolean("enable")) {
            FileAutoFlushTask.createThread(
                fileWriter,
                autoFlush.getInt("min-buffer-size-to-flush"),
                autoFlush.getInt("every-seconds")
            ).start();
        }

        ReactorLauncher.STOP_TASKS.add(fileWriter::flush);
        ReactorLauncher.STOP_TASKS.add(fileWriter::close);

        return new FileLogger(loggerLevels, fileWriter);
    }

    private FileChannel createLogChannel(Path path, ConfigSection logsSection) {
        final FileChannel channel;
        try {
            if (Files.exists(path) && Files.size(path) > 0) {
                final ConfigSection gzip = logsSection.getSection("gzip");
                final int level = gzip == null || !gzip.getBoolean("enable") ? -1 : gzip.getInt("level");
                FileGZIPCompressor.compress(path, level);
            }

            final Path parentDir = path.getParent();
            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }
            channel = FileChannel.open(path, EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.WRITE));
        } catch (IOException e) {
            System.err.println("Can't start file logger");
            e.printStackTrace(System.err);
            return null;
        }
        return channel;
    }

    private ConsoleLogger loadConsoleLogger(final ConfigSection section) {
        final ConfigSection consoleSection = section.getSection("console");
        final LoggerLevels loggerLevels = loadLevels(consoleSection);
        if (loggerLevels == null) {
            return null;
        }
        final ConfigSection styles = consoleSection.getSection("styles");
        if (styles == null) {
            final StyleLog noneStyle = new StyleLog("","","");
            return new ConsoleLogger(consoleWriter, loggerLevels, noneStyle, noneStyle, noneStyle, noneStyle, noneStyle);
        }
        return new ConsoleLogger(
            consoleWriter,
            loggerLevels,
            loadStyle(styles.getSection("debug")),
            loadStyle(styles.getSection("log")),
            loadStyle(styles.getSection("info")),
            loadStyle(styles.getSection("warn")),
            loadStyle(styles.getSection("error"))
        );
    }

    private LoggerLevels loadLevels(final ConfigSection logger) {
        if (logger == null || !logger.getBoolean("enable")) {
            return null;
        }
        final ConfigSection levels = logger.getSection("levels");
        if (levels == null) {
            return new LoggerLevels(true, true, true, true, true);
        }

        return new LoggerLevels(
            levels.getBoolean("debug"),
            levels.getBoolean("log"),
            levels.getBoolean("info"),
            levels.getBoolean("warn"),
            levels.getBoolean("error")
        );
    }

    private StyleLog loadStyle(final ConfigSection section) {
        if (section == null) {
            return new StyleLog("","","");
        }
        return new StyleLog(
            section.getOrDefault("prefix", ""),
            section.getOrDefault("text", ""),
            section.getOrDefault("after-text", ""));
    }
}
