package ink.reactor.sdk.bundled.config.json.decoder;

import ink.reactor.protocol.api.buffer.writer.DynamicSizeBuffer;
import ink.reactor.sdk.bundled.config.json.decoder.exception.JsonDecoderException;
import ink.reactor.sdk.config.ConfigSection;
import ink.reactor.sdk.config.GenericConfigSection;
import lombok.Getter;

import java.util.*;

@Getter
public class JsonDecoder {
    private final JsonNumberDecoder numberDecoder;
    private final byte[] buffer;
    private int index;

    public JsonDecoder(byte[] buffer) {
        this.buffer = buffer;
        this.numberDecoder = new JsonNumberDecoder(buffer);
    }

    public ConfigSection decode() {
        return readSection();
    }

    private GenericConfigSection readSection() {
        final GenericConfigSection section = new GenericConfigSection(new LinkedHashMap<>());

        skipUntilFindValidChar();
        if (buffer[index++] != '{') {
            throw new JsonDecoderException("Invalid json format. Don't start with '{'");
        }

        skipUntilFindValidChar();
        if (buffer[index] == '}') {
            index++;
            return section; // empty section
        }

        while (index < buffer.length) {
            skipUntilFindValidChar();

            final String key = readString();
            skipUntilFindValidChar();
            skipUntilFind(':');
            skipUntilFindValidChar();

            final Object value = readValue(buffer[index]);
            section.set(key, value);

            skipUntilFindValidChar();
            if (buffer[index] == '}') {
                index++;
                break;
            } else if (buffer[index] == ',') {
                index++;
            } else {
                throw new JsonDecoderException("Expected ',' or '}' after key-value pair");
            }
        }

        return section;
    }

    private String readString() {
        skipUntilFind('"');
        final int start = index;

        final DynamicSizeBuffer dynamicSizeBuffer = new DynamicSizeBuffer(32);
        loop: while (index < buffer.length) {
            final byte character = buffer[index++];
            switch (character) {
                case '\n' -> throw new JsonDecoderException("Can't start a new line if key isn't closed");
                case '"' -> {
                    break loop;
                }
                case '\\' -> dynamicSizeBuffer.writeByte(readSpecialChar());
                default -> dynamicSizeBuffer.writeByte((char)character);
            }
        }
        if (index == buffer.length) {
            index = start;
            throw new JsonDecoderException("Found a string without close quote mark");
        }
        return new String(dynamicSizeBuffer.compress());
    }

    private char readSpecialChar() {
        return switch (buffer[index++]) {
            case '"' -> '"';
            case '\\' -> '\\';
            case '/' -> '/';
            case 'n' -> '\n';
            case 'b' -> '\b';
            case 'f' -> '\f';
            case 'r' -> '\r';
            case 't' -> '\t';
            case 'u' -> {
                if (buffer.length < index+4) {
                    throw new JsonDecoderException("Not enough space in the buffer for read hex codepoint (\\u0000)");
                }
                int result = HexFormat.fromHexDigit(buffer[index++]);
                result = (result << 4) | HexFormat.fromHexDigit(buffer[index++]);
                result = (result << 4) | HexFormat.fromHexDigit(buffer[index++]);
                result = (result << 4) | HexFormat.fromHexDigit(buffer[index++]);
                yield (char) result;
            }
            default -> throw new JsonDecoderException("Invalid Special char: " + buffer[index-1]);
        };
    }

    private Object readValue(final byte firstCharacter) {
        return switch (firstCharacter) {
            case '\"' -> readString();
            case 't' -> readTrueValue();
            case 'f' -> readFalseValue();
            case 'n' -> readNullValue();
            case '[' -> readArray();
            case '{' -> readSection();
            case '-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                numberDecoder.setIndex(index);
                final Object number = numberDecoder.readNumber();
                this.index = numberDecoder.getIndex();
                yield number;
            }
            default -> throw new JsonDecoderException("Unknown value first character: " + (char)firstCharacter);
        };
    }

    private boolean readTrueValue() {
        if (buffer.length < index+4) {
            throw new JsonDecoderException("Possible \"true\" value found. But don't have enough space in the buffer");
        }
        final int start = index;
        if (
            buffer[index++] == 't' &&
            buffer[index++] == 'r' &&
            buffer[index++] == 'u' &&
            buffer[index++] == 'e'
        ) {
            return true;
        }
        throw new JsonDecoderException("Invalid true boolean value. Found: " + new String(Arrays.copyOfRange(buffer, start, index)));
    }

    private boolean readFalseValue() {
        if (buffer.length < index+5) {
            throw new JsonDecoderException("Possible \"false\" value found. But don't have enough space in the buffer");
        }
        if (
            buffer[index++] == 'f' &&
            buffer[index++] == 'a' &&
            buffer[index++] == 'l' &&
            buffer[index++] == 's' &&
            buffer[index++] == 'e'
        ) {
            return false;
        }
        throw new JsonDecoderException("Invalid false boolean value. Found: " + new String(Arrays.copyOfRange(buffer, index-5, index-1)));
    }

    private Object readNullValue() {
        if (buffer.length < index+4) {
            throw new JsonDecoderException("Possible \"null\" value found. But don't have enough space in the buffer");
        }
        final int start = index;
        if (
            buffer[index++] == 'u' &&
            buffer[index++] == 'l' &&
            buffer[index++] == 'l'
        ) {
            return null;
        }
        index = start;
        throw new JsonDecoderException("Invalid null value. Found: " + new String(Arrays.copyOfRange(buffer, start, index)));
    }

    private Collection<Object> readArray() {
        final ArrayList<Object> list = new ArrayList<>();
        index++; // skip '['

        skipUntilFindValidChar();
        if (buffer[index] == ']') {
            index++;
            return list; // empty array
        }

        skipUntilFindValidChar();
        while (index < buffer.length) {
            final byte character = buffer[index];
            if (character == ',') {
                throw new JsonDecoderException("Expected a value in the array. Not a single character ','");
            }
            list.add(readValue(character));

            skipUntilFindValidChar();
            final byte nextCharacter = buffer[index++];

            if (nextCharacter == ']') {
                break;
            }
            if (nextCharacter != ',') {
                throw new JsonDecoderException("Can't found ',' character to continue read the array");
            }
            skipUntilFindValidChar();
        }
        return list;
    }

    private void skipUntilFindValidChar() {
        while (index < buffer.length) {
            final byte character = buffer[index];
            if (character != ' ' && character != '\n' && character != '\r' && character != '\t') {
                break;
            }
            index++;
        }
    }

    private void skipUntilFind(final char character) {
        while (index < buffer.length) {
            if (buffer[index++] == character) {
                break;
            }
        }
    }
}
