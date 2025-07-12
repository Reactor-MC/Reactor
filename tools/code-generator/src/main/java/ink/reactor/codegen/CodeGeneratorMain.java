package ink.reactor.codegen;

import ink.reactor.codegen.generator.CodeGenerator;
import ink.reactor.codegen.generator.packet.PacketIDGenerator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class CodeGeneratorMain {
    private static final String PACKAGE_NAME = "ink.reactor.output";

    private static final Map<String, CodeGenerator> GENERATORS = new HashMap<>();
    static {
        GENERATORS.put("packet", new PacketIDGenerator());
    }

    public static void main(final String[] args) {
        String parserName;

        if (args.length == 0) {
            parserName = "packet";
            System.out.println("Using default generator " + parserName + ". Available options: " + GENERATORS.keySet());
        } else {
            parserName = args[0];
        }

        parserName = parserName.toLowerCase();

        if (parserName.equals("all")) {
            GENERATORS.forEach((key, value) -> load(value, key));
            return;
        }

        final CodeGenerator parser = GENERATORS.get(parserName);

        if (parser == null) {
            System.out.println("Can't found the parser "+ parserName);
            return;
        }
        load(parser, parserName);
    }

    private static void load(final CodeGenerator dataParser, final String generatorName) {
        try {
            long time = System.currentTimeMillis();

            dataParser.generate(PACKAGE_NAME);

            time = System.currentTimeMillis() - time;
            System.out.println("Generate " + generatorName + " in: " + time + "ms");
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

}
