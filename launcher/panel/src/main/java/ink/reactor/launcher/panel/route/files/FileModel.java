package ink.reactor.launcher.panel.route.files;

import java.math.BigInteger;

public record FileModel(
    BigInteger weight,
    long modifiedTime,
    String fileName
) {
}
