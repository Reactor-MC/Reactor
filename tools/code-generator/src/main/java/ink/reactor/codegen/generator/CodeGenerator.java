package ink.reactor.codegen.generator;

import java.io.IOException;

public interface CodeGenerator {
    void generate(String packageName) throws IOException;
}
