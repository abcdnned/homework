package tom.yang.homework.javaserializer;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * The interface coherence deserializer.
 *
 * @version 1.0
 * @since v3.4
 */
public interface CohDeserializer {


    /**
     * Read object list.
     *
     * @param buffer the buffer
     * @param printClazz true for print class name
     * @param printValue true for print value
     * @param limit the limit -1 for unlimited
     * @return the list of printable fields
     * @throws IllegalArgumentException the illegal argument exception if buffer is invalid
     */
    List<PrintableField> readObject(ByteBuffer buffer, boolean printClazz, boolean printValue,
            int limit) throws IllegalArgumentException;

    /**
     * Skip object.
     *
     * @param buffer the buffer
     * @throws IllegalArgumentException the illegal argument exception if buffer is invalid
     */
    void skipObject(ByteBuffer buffer) throws IllegalArgumentException;

    /**
     * Read object list.
     *
     * @param buffer the buffer
     * @return the list of printable fields
     * @throws IllegalArgumentException the illegal argument exception if buffer is invalid
     */
    default List<PrintableField> readObject(ByteBuffer buffer) throws IllegalArgumentException {
        return this.readObject(buffer, true, true, -1);
    }
}
