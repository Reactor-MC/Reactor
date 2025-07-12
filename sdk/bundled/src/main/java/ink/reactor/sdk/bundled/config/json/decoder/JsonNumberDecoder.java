package ink.reactor.sdk.bundled.config.json.decoder;

import ink.reactor.sdk.config.exception.ConfigLoadException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Setter
@Getter
final class JsonNumberDecoder {
    private final byte[] buffer;
    private int index;

    Object readNumber() {
        final int start = index;
        final StringBuilder numberBuilder = new StringBuilder(16);
        boolean isDouble = false;
        boolean isNegative = false;

        if (buffer[index] == '-') {
            numberBuilder.append('-');
            isNegative = true;
            index++;
        }

        if (index >= buffer.length) {
            throw new ConfigLoadException("Unexpected end while reading number");
        }

        if (buffer[index] == '0') {
            numberBuilder.append('0');
            index++;

            if (index < buffer.length && isDigit(buffer[index])) {
                throw new ConfigLoadException("Invalid number format: leading zeros");
            }
        } else if (isDigit(buffer[index])) {
            while (index < buffer.length && isDigit(buffer[index])) {
                numberBuilder.append((char) buffer[index++]);
            }
        } else {
            throw new ConfigLoadException("Invalid number format at position: " + index);
        }

        if (index < buffer.length && buffer[index] == '.') {
            isDouble = true;
            numberBuilder.append('.');
            index++;

            if (index >= buffer.length || !isDigit(buffer[index])) {
                throw new ConfigLoadException("Invalid fraction part in number");
            }

            while (index < buffer.length && isDigit(buffer[index])) {
                numberBuilder.append((char) buffer[index++]);
            }
        }

        if (index < buffer.length && (buffer[index] == 'e' || buffer[index] == 'E')) {
            isDouble = true;
            numberBuilder.append((char) buffer[index++]);

            if (index < buffer.length && (buffer[index] == '+' || buffer[index] == '-')) {
                numberBuilder.append((char) buffer[index++]);
            }

            if (index >= buffer.length || !isDigit(buffer[index])) {
                throw new ConfigLoadException("Invalid exponent part in number");
            }

            while (index < buffer.length && isDigit(buffer[index])) {
                numberBuilder.append((char) buffer[index++]);
            }
        }

        final String numStr = numberBuilder.toString();
        try {
            if (isDouble) {
                return Double.parseDouble(numStr);
            }
            long value = Long.parseLong(numStr);
            if (isNegative) {
                if (value >= Integer.MIN_VALUE) { // Don't change to a ternary operator. Autoboxing only works if you make: (Object)((int)value)
                    return (int)value;
                }
                return value;
            }
            if (value <= Integer.MAX_VALUE) {
                return (int)value;
            }
            return value;
        } catch (NumberFormatException e) {
            index = start;
            throw new ConfigLoadException("Invalid number format: " + numStr, e);
        }
    }

    private static boolean isDigit(byte b) {
        return b >= '0' && b <= '9';
    }
}
