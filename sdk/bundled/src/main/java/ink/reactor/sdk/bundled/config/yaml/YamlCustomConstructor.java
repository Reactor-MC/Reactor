package ink.reactor.sdk.bundled.config.yaml;

import ink.reactor.sdk.config.section.MapConfigSection;
import org.snakeyaml.engine.v2.api.LoadSettings;
import org.snakeyaml.engine.v2.constructor.StandardConstructor;
import org.snakeyaml.engine.v2.exceptions.YamlEngineException;
import org.snakeyaml.engine.v2.nodes.MappingNode;
import org.snakeyaml.engine.v2.nodes.Node;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// We need create a custom constructor for make all keys strings and transform map into ConfigSection
final class YamlCustomConstructor extends StandardConstructor {
    public YamlCustomConstructor(final LoadSettings settings) {
        super(settings);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Map<Object, Object> constructMapping(final MappingNode node) {
        final Map<Object, Object> result = new LinkedHashMap<>();
        final List<NodeTuple> nodeTuples = node.getValue();
        for (final NodeTuple tuple : nodeTuples) {
            final ScalarNode keyNode = (ScalarNode)tuple.getKeyNode();
            final Node valueNode = tuple.getValueNode();

            final String key = keyNode.getValue();
            Object value = constructObject(valueNode);

            if (value instanceof Map<?,?>) {
                value = new MapConfigSection((Map<String, Object>) value);
            }

            if (keyNode.isRecursive()) {
                if (!this.settings.getAllowRecursiveKeys()) {
                    throw new YamlEngineException("Recursive key for mapping is detected but it is not configured to be allowed.");
                }
                this.postponeMapFilling(result, key, value);
            } else {
                result.put(key, value);
            }
        }
        return result;
    }
}
