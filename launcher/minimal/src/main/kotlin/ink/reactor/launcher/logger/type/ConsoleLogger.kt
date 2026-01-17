package ink.reactor.launcher.logger.type

import ink.reactor.launcher.logger.ConsoleStyle
import ink.reactor.launcher.logger.LoggerLevels
import java.io.PrintWriter

class ConsoleLogger(
    val writer: PrintWriter,
    val loggerLevels: LoggerLevels,

    val debugStyle: ConsoleStyle,
    val logStyle: ConsoleStyle,
    val infoStyle: ConsoleStyle,
    val warnStyle: ConsoleStyle,
    val errorStyle: ConsoleStyle,
) {

    fun debug(prefix: String, message: String) {
        if (loggerLevels.debug) {
            print(debugStyle, prefix, message)
        }
    }

    fun log(prefix: String, message: String) {
        if (loggerLevels.log) {
            print(logStyle, prefix, message)
        }
    }

    fun info(prefix: String, message: String) {
        if (loggerLevels.info) {
            print(infoStyle, prefix, message)
        }
    }

    fun warn(prefix: String, message: String) {
        if (loggerLevels.warn) {
            print(warnStyle, prefix, message)
        }
    }

    fun error(prefix: String, message: String) {
        if (loggerLevels.error) {
            print(errorStyle, prefix, message)
        }
    }

    fun error(prefix: String, message: String, throwable: Throwable) {
        if (loggerLevels.error) {
            print(errorStyle, prefix, message)
            throwable.printStackTrace(System.err)
        }
    }

    private fun print(style: ConsoleStyle, prefix: String, message: String) =
        writer.println(style.prefix + prefix + style.text + message + style.afterText);
}
