package tom.yang.homework.javaserializer;

import cn.com.netis.dp.commons.lang.NegativeArgumentException;
import cn.com.netis.dp.commons.lang.NonPositiveArgumentException;

import java.nio.ByteBuffer;

/**
 * The Class AsciiBuffer.
 *
 * @version 1.6
 * @since project 3.1.7 updates 1.5
 */
public final class AsciiBuffer {

    /**
     * The Constant REPLACE_CHAR.
     */
    public static final char REPLACE_CHAR = '.';

    /** The Constant BLANK_CHAR. */
    public static final char BLANK_CHAR = ' ';

    /**
     * The Constant MIN_BYTE.
     */
    public static final byte MIN_BYTE = 32;

    /**
     * The Constant MAX_BYTE.
     */
    public static final byte MAX_BYTE = 126;

    /**
     * Check get string.
     *
     * @param buffer the buffer
     * @param length the length
     * @param builder the builder
     * @return true, if successful
     */
    public static boolean checkGetString(final ByteBuffer buffer,
            final int length, final StringBuilder builder) {
        NegativeArgumentException.check(length, "length");
        builder.setLength(0);
        int pos = buffer.position();
        final int alignedLength = alignLength(buffer, length);
        for (int i = 0; i < alignedLength; i++) {
            final byte val = buffer.get();
            // check the null terminate for c style string
            if (val == 0) {
                // consume remaining buffer
                buffer.position(pos + alignedLength);
                break;
            }
            if (!isAsciiChar(val)) {
                buffer.position(buffer.position() - 1);
                return false;
            }
            builder.append((char) val);
        }
        return true;
    }

    /**
     * Check string. Return true if following length buffers are Ascii characters.
     *
     * @param buffer the buffer
     * @param length the length to check
     * @return true, if successful
     */
    public static boolean checkString(final ByteBuffer buffer, final int length) {
        NegativeArgumentException.check(length, "length");
        final int alignedLength = alignLength(buffer, length);
        for (int i = 0; i < alignedLength; i++) {
            final byte val = buffer.get();
            if (!isAsciiChar(val)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the c-style null string with replace char(.) and consume the buffer.
     *
     * @param buffer the buffer
     * @param length the length
     * @param builder the builder
     * @param replaceChar the replace char
     * @return the null string
     * @since 1.5 rename to getNullString
     */
    public static String getNullString(final ByteBuffer buffer, final int length,
            final StringBuilder builder, final char replaceChar) {
        NegativeArgumentException.check(length, "length");
        builder.setLength(0);
        int pos = buffer.position();
        final int alignedLength = alignLength(buffer, length);
        for (int i = 0; i < alignedLength; i++) {
            final byte val = buffer.get();
            if (val == 0) {
                // consume remaining buffer
                buffer.position(pos + alignedLength);
                break;
            }
            if (!isAsciiChar(val)) {
                builder.append(replaceChar);
                continue;
            }
            builder.append((char) val);
        }
        return builder.toString();
    }

    /**
     * Gets the c-style null string and consume the buffer.
     *
     * @param buffer the buffer
     * @param length the length
     * @return the null string
     * @since 1.5 rename to getNullString
     */
    public static String getNullString(final ByteBuffer buffer, final int length) {
        final StringBuilder builder = new StringBuilder();
        return getNullString(buffer, length, builder, REPLACE_CHAR);
    }

    /**
     * Gets the c-style null string with replace char(.) and consume the buffer.
     *
     * @param buffer the buffer
     * @param length the length
     * @param replaceChar the replace char
     * @return the null string
     * @since 1.5 rename to getNullString
     */
    public static String getNullString(final ByteBuffer buffer, final int length,
            final char replaceChar) {
        final StringBuilder builder = new StringBuilder();
        return getNullString(buffer, length, builder, replaceChar);
    }

    /**
     * Gets the string by pos.
     *
     * @param buffer the buffer
     * @param startPos the start pos
     * @param length the length
     * @return the string by pos
     */
    public static String getStringByPos(final ByteBuffer buffer, final int startPos,
            final int length) {
        final StringBuilder builder = new StringBuilder();
        final int alignedLength = alignLength(buffer, length);
        if (startPos + alignedLength >= buffer.limit()) {
            return builder.toString();
        }
        for (int i = startPos; i < startPos + alignedLength; i++) {
            final byte val = buffer.get(i);
            builder.append((char) val);
        }
        return builder.toString();
    }

    /**
     * Gets the string.
     *
     * @param buffer the buffer
     * @param length the length
     * @return the string
     */
    public static String getString(final ByteBuffer buffer, final int length) {
        final StringBuilder builder = new StringBuilder();
        return getString(buffer, length, builder, REPLACE_CHAR);
    }

    /**
     * Gets the string.
     *
     * @param buffer the buffer
     * @param length the length
     * @param replaceChar the replace char
     * @return the string
     */
    public static String getString(final ByteBuffer buffer, final int length,
            final char replaceChar) {
        final StringBuilder builder = new StringBuilder();
        return getString(buffer, length, builder, replaceChar);
    }

    /**
     * Gets the string.
     *
     * @param buffer the buffer
     * @param length the length
     * @param builder the builder
     * @param replaceChar the replace char
     * @return the string
     */
    public static String getString(final ByteBuffer buffer, final int length,
            final StringBuilder builder, final char replaceChar) {
        NegativeArgumentException.check(length, "length");
        builder.setLength(0);
        final int alignedLength = alignLength(buffer, length);
        for (int i = 0; i < alignedLength; i++) {
            final byte val = buffer.get();
            if (val == 0) {
                builder.append(BLANK_CHAR);
                continue;
            }
            if (!isAsciiChar(val)) {
                builder.append(replaceChar);
                continue;
            }
            builder.append((char) val);
        }
        return builder.toString();
    }

    /**
     * Gets the ascii string.
     *
     * @param length the length
     * @return the ascii string
     */
    public static String getString(final XorByteBuffer buffer, final int length) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            final byte val = buffer.get();
            if (val == 0) {
                builder.append(BLANK_CHAR);
                continue;
            }
            builder.append((char) val);
        }
        return builder.toString();
    }

    /**
     * Gets the integer.
     *
     * @param buffer the buffer
     * @param length the length
     * @return the integer
     */
    public static int getInt(final ByteBuffer buffer, final int length) {
        return Integer.parseInt(getIntString(buffer, length));
    }

    /**
     * Gets the integer.
     *
     * @param buffer the buffer
     * @param length the length
     * @param radix the radix
     * @return the integer
     * @since 1.1
     */
    public static int getInt(final ByteBuffer buffer, final int length, final int radix) {
        return Integer.parseInt(getIntString(buffer, length), radix);
    }

    /**
     * Gets the integer string.
     *
     * @param buffer the buffer
     * @param length the length
     * @return the integer string
     * @since 1.1
     */
    public static String getIntString(final ByteBuffer buffer, final int length) {
        NonPositiveArgumentException.check(length, "length");
        final StringBuilder builder = new StringBuilder();
        int pos = buffer.position();
        final int alignedLength = alignLength(buffer, length);
        for (int i = 0; i < alignedLength; i++) {
            final byte val = buffer.get();
            if (val == 0) {
                // consume remaining buffer
                buffer.position(pos + alignedLength);
                break;
            }
            builder.append((char) val);
        }
        // deal with something like "123  "
        return builder.toString().trim();
    }

    /**
     * Substring before the one of separators.
     *
     * @param buffer the buffer
     * @param separator the separator
     * @return the string before the separator, if not find separator return null
     * @since 1.4
     */
    public static String substringBefore(final ByteBuffer buffer, final byte separator) {
        final StringBuilder builder = new StringBuilder();
        int pos = buffer.position();
        for (int i = buffer.position(); i < buffer.limit(); ++i) {
            final byte val = buffer.get();
            if (val == separator) {
                return builder.toString();
            }
            if (val == 0) {
                builder.append(BLANK_CHAR);
                continue;
            }
            if (!isAsciiChar(val)) {
                builder.append(REPLACE_CHAR);
                continue;
            }
            builder.append((char) val);
        }
        return null;
    }

    /**
     * Substring before the one of separators.
     *
     * @param buffer the buffer
     * @param separator the separator
     * @param replaceChar the replace char
     * @return the string before the separator, if not find separator return null
     */
    public static String substringBefore(final ByteBuffer buffer, final Separatable separator
            , final char replaceChar) {
        final StringBuilder builder = new StringBuilder();
        int pos = buffer.position();
        for (int i = buffer.position(); i < buffer.limit(); ++i) {
            final byte val = buffer.get();
            if (separator.isSeparator(val, buffer)) {
                return builder.toString();
            }
            if (val == 0) {
                builder.append(BLANK_CHAR);
                continue;
            }
            if (!isAsciiChar(val)) {
                builder.append(replaceChar);
                continue;
            }
            builder.append((char) val);
        }
        return null;
    }

    /**
     * Check if val is ascii char.
     *
     * @param val value the check
     * @return true if the val is ascii char
     */
    private static boolean isAsciiChar(final byte val) {
        return val >= MIN_BYTE && val <= MAX_BYTE;
    }

    /**
     * Checks if is ascii char.
     *
     * @param buffer the buffer
     * @param startPos the start pos
     * @param length the length
     * @return true, if checks if is ascii char
     */
    public static boolean isAsciiChar(final ByteBuffer buffer, final int startPos, int length) {
        if (startPos + length >= buffer.limit()) {
            return false;
        }
        for (int i = startPos; i < startPos + length; i++) {
            if (!isAsciiChar(buffer.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Align the length to the minimum between buffer remaining and expected length.
     *
     * @param buffer the byte buffer
     * @param length the expected length
     * @return minimum between buffer remaining and expected length.
     */
    private static int alignLength(final ByteBuffer buffer, final int length) {
        return Math.min(buffer.remaining(), length);
    }

    /**
     * Prevents from instantiating.
     */
    private AsciiBuffer() {
    }
}
